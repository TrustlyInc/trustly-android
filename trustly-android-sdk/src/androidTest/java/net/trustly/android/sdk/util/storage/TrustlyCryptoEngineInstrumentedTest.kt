package net.trustly.android.sdk.util.storage

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyCryptoEngineInstrumentedTest {

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
