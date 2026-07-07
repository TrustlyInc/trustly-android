package net.trustly.android.sdk.util.storage

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import net.trustly.android.sdk.util.error.TrustlyExceptionHandler
import org.conscrypt.Conscrypt
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.PrivateKey
import java.security.Security
import java.util.Calendar
import java.util.concurrent.atomic.AtomicBoolean
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.GCMParameterSpec
import javax.security.auth.x500.X500Principal

class TrustlyCryptoEngine(
    context: Context,
    private val keyAlias: String = DEFAULT_RSA_KEY_ALIAS
) {
    private val appContext = context.applicationContext ?: context
    private val keyPrefs: SharedPreferences? =
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun encrypt(plainText: String): String? {
        return try {
            val aesKey = getOrCreateAesKeyForEncrypt() ?: return null
            val cipher = createAesCipher()
            cipher.init(Cipher.ENCRYPT_MODE, aesKey)

            val iv = cipher.iv
            if (iv.size != GCM_IV_BYTES) {
                showLogError("Unexpected IV length")
                return null
            }

            val cipherText = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
            val payload = ByteArray(iv.size + cipherText.size)
            System.arraycopy(iv, 0, payload, 0, iv.size)
            System.arraycopy(cipherText, 0, payload, iv.size, cipherText.size)

            Base64.encodeToString(payload, BASE64_FLAGS)
        } catch (e: Exception) {
            showLogError("Unexpected encryption error: ${e.message}")
            null
        }
    }

    fun decrypt(encodedPayload: String): String? {
        val decodedPayload = try {
            Base64.decode(encodedPayload, BASE64_FLAGS)
        } catch (_: IllegalArgumentException) {
            showLogError("Invalid encrypted payload format")
            return null
        }
        if (decodedPayload == null) {
            showLogError("Invalid encrypted payload bytes")
            return null
        }

        if (decodedPayload.size <= GCM_IV_BYTES) {
            showLogError("Encrypted payload too short")
            return null
        }

        val iv = decodedPayload.copyOfRange(0, GCM_IV_BYTES)
        val cipherText = decodedPayload.copyOfRange(GCM_IV_BYTES, decodedPayload.size)

        return try {
            val aesKey = getAesKeyForDecrypt() ?: return null
            val cipher = createAesCipher()
            cipher.init(
                Cipher.DECRYPT_MODE,
                aesKey,
                GCMParameterSpec(GCM_TAG_BITS, iv)
            )

            val plainTextBytes = cipher.doFinal(cipherText)
            String(plainTextBytes, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            showLogError("Unexpected decryption error: ${e.message}")
            null
        }
    }

    private fun createAesCipher(): Cipher {
        installConscryptIfNeeded()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Cipher.getInstance(AES_TRANSFORMATION)
        } else {
            Cipher.getInstance(AES_TRANSFORMATION, CONSCRYPT_PROVIDER)
        }
    }

    private fun installConscryptIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP || providerInstalled.get()) {
            return
        }

        synchronized(providerLock) {
            if (providerInstalled.get()) return
            val existingProvider = Security.getProvider(CONSCRYPT_PROVIDER)
            if (existingProvider == null) {
                Security.insertProviderAt(Conscrypt.newProvider(), 1)
            }
            providerInstalled.set(true)
        }
    }

    private fun getOrCreateAesKeyForEncrypt(): SecretKey? {
        val existing = loadAesKey()
        if (existing != null) return existing

        val generated = generateAesKey()
        val wrapped = wrapAesKey(generated) ?: return null
        persistWrappedAesKey(wrapped)
        return generated
    }

    private fun getAesKeyForDecrypt(): SecretKey? = loadAesKey()

    private fun loadAesKey(): SecretKey? {
        val wrappedAesKey = getWrappedAesKey() ?: return null
        return try {
            val unwrapped = unwrapAesKey(wrappedAesKey)
            SecretKeySpec(unwrapped, AES_KEY_ALGORITHM)
        } catch (e: Exception) {
            showLogError("Unexpected error while unwrapping AES key: ${e.message}")
            null
        }
    }

    private fun generateAesKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(AES_KEY_ALGORITHM)
        keyGenerator.init(AES_KEY_SIZE_BITS)
        return keyGenerator.generateKey()
    }

    private fun wrapAesKey(secretKey: SecretKey): ByteArray? {
        return try {
            val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getOrCreateRsaKeyPair().public)
            cipher.doFinal(secretKey.encoded)
        } catch (_: GeneralSecurityException) {
            showLogError("Unable to wrap AES key")
            null
        }
    }

    private fun unwrapAesKey(wrappedKey: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateRsaKeyPair().private)
        return cipher.doFinal(wrappedKey)
    }

    private fun getWrappedAesKey(): ByteArray? {
        val encoded = keyPrefs?.getString(PREF_WRAPPED_AES_KEY, null) ?: return null
        return try {
            Base64.decode(encoded, BASE64_FLAGS) ?: run {
                showLogError("Invalid wrapped AES key bytes")
                clearWrappedAesKey()
                null
            }
        } catch (_: IllegalArgumentException) {
            showLogError("Invalid wrapped AES key encoding")
            clearWrappedAesKey()
            null
        }
    }

    private fun showLogError(message: String) {
        TrustlyExceptionHandler().uncaughtException(
            Thread.currentThread(),
            Exception(Throwable(message))
        )
    }

    private fun persistWrappedAesKey(wrappedKey: ByteArray) {
        val encoded = Base64.encodeToString(wrappedKey, BASE64_FLAGS)
        keyPrefs?.edit()?.putString(PREF_WRAPPED_AES_KEY, encoded)?.apply()
    }

    private fun clearWrappedAesKey() {
        keyPrefs?.edit()?.remove(PREF_WRAPPED_AES_KEY)?.apply()
    }

    private fun getOrCreateRsaKeyPair(): KeyPair {
        val keyStore = loadKeyStore()
        val privateKey = keyStore.getKey(keyAlias, null) as? PrivateKey
        val publicKey = keyStore.getCertificate(keyAlias)?.publicKey
        if (privateKey != null && publicKey != null) {
            return KeyPair(publicKey, privateKey)
        }

        generateRsaKeyPair()
        val refreshedStore = loadKeyStore()
        val refreshedPrivateKey = refreshedStore.getKey(keyAlias, null) as? PrivateKey
            ?: throw KeyStoreException("RSA private key not available after generation")
        val refreshedPublicKey = refreshedStore.getCertificate(keyAlias)?.publicKey
            ?: throw KeyStoreException("RSA public key not available after generation")
        return KeyPair(refreshedPublicKey, refreshedPrivateKey)
    }

    private fun generateRsaKeyPair() {
        val generator = KeyPairGenerator.getInstance(RSA_ALGORITHM, ANDROID_KEYSTORE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val spec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build()
            generator.initialize(spec)
        } else {
            val startDate = Calendar.getInstance()
            val endDate = Calendar.getInstance().apply { add(Calendar.YEAR, CERT_VALID_YEARS) }
            val spec = KeyPairGeneratorSpec.Builder(appContext)
                .setAlias(keyAlias)
                .setSubject(X500Principal("CN=$keyAlias"))
                .setSerialNumber(CERT_SERIAL_NUMBER)
                .setStartDate(startDate.time)
                .setEndDate(endDate.time)
                .build()
            generator.initialize(spec)
        }
        generator.generateKeyPair()
    }

    private fun loadKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return keyStore
    }

    private companion object {
        const val DEFAULT_RSA_KEY_ALIAS = "TrustlyRSAKey"
        const val PREFS_NAME = "trustly_storage_crypto"
        const val PREF_WRAPPED_AES_KEY = "wrapped_aes_key"
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val AES_TRANSFORMATION = "AES/GCM/NoPadding"
        const val RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding"
        const val AES_KEY_ALGORITHM = "AES"
        const val RSA_ALGORITHM = "RSA"
        const val AES_KEY_SIZE_BITS = 256
        const val GCM_IV_BYTES = 12
        const val GCM_TAG_BITS = 128
        const val BASE64_FLAGS = Base64.URL_SAFE or Base64.NO_WRAP
        const val CONSCRYPT_PROVIDER = "Conscrypt"
        const val CERT_VALID_YEARS = 20
        val CERT_SERIAL_NUMBER = java.math.BigInteger.ONE

        val providerInstalled = AtomicBoolean(false)
        val providerLock = Any()
    }
}
