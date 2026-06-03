package net.trustly.android.sdk.util.storage

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class TrustlyCryptoEngine(private val keyAlias: String = DEFAULT_KEY_ALIAS) {

    fun encrypt(plainText: String): String? {
        if (isSecureStorageAvailable()) return null

        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKeyApi23())

            val iv = cipher.iv
            if (iv.size != GCM_IV_BYTES) {
                Log.w(TAG, "Unexpected IV length")
                return null
            }

            val cipherText = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
            val payload = ByteArray(iv.size + cipherText.size)
            System.arraycopy(iv, 0, payload, 0, iv.size)
            System.arraycopy(cipherText, 0, payload, iv.size, cipherText.size)

            Base64.encodeToString(payload, BASE64_FLAGS)
        } catch (_: KeyPermanentlyInvalidatedException) {
            Log.w(TAG, "Storage key invalidated; resetting key")
            resetKeySafelyApi23()
            null
        } catch (_: GeneralSecurityException) {
            Log.w(TAG, "Encryption failure")
            null
        }
    }

    fun decrypt(encodedPayload: String): String? {
        if (isSecureStorageAvailable()) return null

        val decodedPayload = try {
            Base64.decode(encodedPayload, BASE64_FLAGS)
        } catch (_: IllegalArgumentException) {
            Log.w(TAG, "Invalid encrypted payload format")
            return null
        }

        if (decodedPayload.size <= GCM_IV_BYTES) {
            Log.w(TAG, "Encrypted payload too short")
            return null
        }

        val iv = decodedPayload.copyOfRange(0, GCM_IV_BYTES)
        val cipherText = decodedPayload.copyOfRange(GCM_IV_BYTES, decodedPayload.size)

        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(
                Cipher.DECRYPT_MODE,
                getOrCreateSecretKeyApi23(),
                GCMParameterSpec(GCM_TAG_BITS, iv)
            )

            val plainTextBytes = cipher.doFinal(cipherText)
            String(plainTextBytes, StandardCharsets.UTF_8)
        } catch (_: KeyPermanentlyInvalidatedException) {
            Log.w(TAG, "Storage key invalidated during decrypt; resetting key")
            resetKeySafelyApi23()
            null
        } catch (_: AEADBadTagException) {
            Log.w(TAG, "Encrypted payload failed integrity check")
            null
        } catch (_: GeneralSecurityException) {
            Log.w(TAG, "Decryption failure")
            null
        }
    }

    private fun isSecureStorageAvailable(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.w(TAG, "Secure storage unavailable on this Android API level")
            return true
        }
        return false
    }

    private fun getOrCreateSecretKeyApi23(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val existingKey = keyStore.getEntry(keyAlias, null) as? KeyStore.SecretKeyEntry
        if (existingKey != null) {
            return existingKey.secretKey
        }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val spec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(AES_KEY_SIZE_BITS)
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    private fun resetKeySafelyApi23() {
        try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)
            if (keyStore.containsAlias(keyAlias)) {
                keyStore.deleteEntry(keyAlias)
            }
        } catch (e: Exception) {
            when (e) {
                is KeyStoreException, is NoSuchAlgorithmException, is CertificateException, is IOException -> {
                    Log.w(TAG, "Unable to reset storage key", e)
                }
                else -> throw e
            }
        }
    }

    private companion object {
        const val TAG = "TrustlyCryptoEngine"
        const val DEFAULT_KEY_ALIAS = "TrustlyStorageKey"
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val AES_KEY_SIZE_BITS = 256
        const val GCM_IV_BYTES = 12
        const val GCM_TAG_BITS = 128
        const val BASE64_FLAGS = Base64.URL_SAFE or Base64.NO_WRAP
    }
}


