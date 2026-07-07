package net.trustly.android.sdk.util.storage

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class TrustlyCryptoEngineTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        val sharedPreferences = mock(SharedPreferences::class.java)
        `when`(context.applicationContext).thenReturn(context)
        `when`(
            context.getSharedPreferences(
                "trustly_storage_crypto",
                Context.MODE_PRIVATE
            )
        ).thenReturn(sharedPreferences)
    }

    @Test
    fun shouldReturnNullOnDecryptWhenPayloadIsNotBase64() {
        val cryptoEngine = TrustlyCryptoEngine(context)
        assertNull(cryptoEngine.decrypt("%%invalid%%"))
    }

    @Test
    fun shouldReturnNullOnDecryptWhenPayloadTooShort() {
        val cryptoEngine = TrustlyCryptoEngine(context)
        assertNull(cryptoEngine.decrypt("aGVsbG8"))
    }
}
