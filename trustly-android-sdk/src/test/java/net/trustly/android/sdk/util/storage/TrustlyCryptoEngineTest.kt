package net.trustly.android.sdk.util.storage

import org.junit.Assert.assertNull
import org.junit.Test

class TrustlyCryptoEngineTest {

    @Test
    fun shouldReturnNullOnEncryptWhenApiLevelDoesNotSupportKeystoreAes() {
        val cryptoEngine = TrustlyCryptoEngine()

        val encrypted = cryptoEngine.encrypt("plain_text")

        assertNull(encrypted)
    }

    @Test
    fun shouldReturnNullOnDecryptWhenApiLevelDoesNotSupportKeystoreAes() {
        val cryptoEngine = TrustlyCryptoEngine()

        val decrypted = cryptoEngine.decrypt("invalid")

        assertNull(decrypted)
    }
}

