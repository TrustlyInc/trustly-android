package net.trustly.android.sdk.util.storage

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.security.KeyStore

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyCryptoEngineInstrumentedTest {

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        
        // Clear shared preferences
        val prefs = context.getSharedPreferences("trustly_storage_crypto", android.content.Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        
        // Clear KeyStore
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        try {
            keyStore.deleteEntry("TrustlyRSAKey")
        } catch (_: Exception) {
            // Key doesn't exist yet, which is fine
        }
    }

    @Test
    fun shouldEncryptAndDecryptOnApi19AndAbove() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val cryptoEngine = TrustlyCryptoEngine(context)
        val plainText = "bank_preference"

        val encrypted = cryptoEngine.encrypt(plainText)
        assertNotNull(encrypted)

        val decrypted = cryptoEngine.decrypt(encrypted!!)
        assertEquals(plainText, decrypted)
    }

    @Test
    fun shouldReturnNullForTamperedPayload() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val cryptoEngine = TrustlyCryptoEngine(context)

        val encrypted = cryptoEngine.encrypt("value")
        assertNotNull(encrypted)

        val tamperedPayload = encrypted!!.dropLast(2) + "aa"
        val decrypted = cryptoEngine.decrypt(tamperedPayload)

        assertNull(decrypted)
    }
}
