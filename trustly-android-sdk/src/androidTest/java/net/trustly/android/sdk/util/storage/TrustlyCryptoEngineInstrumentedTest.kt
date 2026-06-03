package net.trustly.android.sdk.util.storage

import android.os.Build
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
    fun shouldEncryptAndDecryptOnSupportedApis() {
        val cryptoEngine = TrustlyCryptoEngine()
        val plainText = "bank_preference"

        val encrypted = cryptoEngine.encrypt(plainText)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            assertNotNull(encrypted)
            val decrypted = cryptoEngine.decrypt(encrypted!!)
            assertEquals(plainText, decrypted)
        } else {
            assertNull(encrypted)
        }
    }

    @Test
    fun shouldReturnNullForTamperedPayload() {
        val cryptoEngine = TrustlyCryptoEngine()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            assertNull(cryptoEngine.decrypt("invalid"))
            return
        }

        val encrypted = cryptoEngine.encrypt("value")
        assertNotNull(encrypted)

        val tamperedPayload = encrypted!!.dropLast(2) + "aa"
        val decrypted = cryptoEngine.decrypt(tamperedPayload)

        assertNull(decrypted)
    }
}

