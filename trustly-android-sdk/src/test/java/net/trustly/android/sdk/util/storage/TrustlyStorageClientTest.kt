package net.trustly.android.sdk.util.storage

import android.content.Context
import android.webkit.CookieManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class TrustlyStorageClientTest {

    private lateinit var context: Context
    private lateinit var cookieManager: CookieManager
    private lateinit var cryptoEngine: TrustlyCryptoEngine

    private val cookieJar = linkedMapOf<String, String>()
    private val storageUrl = "https://storage.trustly.com"
    private var legacySyncCallCount = 0

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        cookieManager = mock(CookieManager::class.java)
        cryptoEngine = mock(TrustlyCryptoEngine::class.java)
        legacySyncCallCount = 0

        `when`(cookieManager.getCookie(anyString())).thenAnswer {
            if (cookieJar.isEmpty()) {
                null
            } else {
                cookieJar.entries.joinToString("; ") { "${it.key}=${it.value}" }
            }
        }

        `when`(cookieManager.setCookie(anyString(), anyString())).thenAnswer { invocation ->
            val cookieValue = invocation.getArgument<String>(1)
            val keyValue = cookieValue.substringBefore(';')
            val name = keyValue.substringBefore('=')
            val value = keyValue.substringAfter('=', "")

            if (cookieValue.contains("Max-Age=0")) {
                cookieJar.remove(name)
            } else {
                cookieJar[name] = value
            }
            null
        }
    }

    @Test
    fun shouldSetAndGetItemAcrossTwoClientInstances() {
        `when`(cryptoEngine.encrypt("Chase")).thenReturn("enc_chase")
        `when`(cryptoEngine.decrypt("enc_chase")).thenReturn("Chase")

        val appA = buildClient()
        val appB = buildClient()

        val setResult = appA.setItem("bankPreference", "Chase")
        val readResult = appB.getItem("bankPreference")

        assertTrue(setResult)
        assertEquals("Chase", readResult)
        assertEquals(1, legacySyncCallCount)
    }

    @Test
    fun shouldReturnNullWhenItemMissing() {
        val client = buildClient()

        val result = client.getItem("missing")

        assertNull(result)
        verify(cryptoEngine, never()).decrypt(anyString())
    }

    @Test
    fun shouldCleanupCookieWhenDecryptReturnsNull() {
        cookieJar["bank_key"] = "tampered_payload"
        `when`(cryptoEngine.decrypt("tampered_payload")).thenReturn(null)

        val client = buildClient()

        val result = client.getItem("bank_key")

        assertNull(result)
        assertFalse(cookieJar.containsKey("bank_key"))
        verify(cookieManager, times(1)).setCookie(storageUrl, "bank_key=; Max-Age=0; Path=/; Secure; HttpOnly; SameSite=Strict")
        assertEquals(1, legacySyncCallCount)
    }

    @Test
    fun shouldPreserveUtf8Value() {
        val utf8Value = "Crédito São Paulo 🌎"
        `when`(cryptoEngine.encrypt(utf8Value)).thenReturn("utf8_payload")
        `when`(cryptoEngine.decrypt("utf8_payload")).thenReturn(utf8Value)

        val client = buildClient()

        val setResult = client.setItem("last_used", utf8Value)
        val restored = client.getItem("last_used")

        assertTrue(setResult)
        assertEquals(utf8Value, restored)
    }

    @Test
    fun shouldReturnFalseWhenEncryptionFails() {
        `when`(cryptoEngine.encrypt("value")).thenReturn(null)

        val client = buildClient()

        val result = client.setItem("k", "value")

        assertFalse(result)
        verify(cookieManager, never()).setCookie(anyString(), anyString())
        assertEquals(0, legacySyncCallCount)
    }

    @Test
    fun shouldReturnNullWhenCookieAccessThrowsSecurityException() {
        `when`(cookieManager.getCookie(anyString())).thenThrow(SecurityException("blocked"))

        val client = buildClient()

        val result = client.getItem("bank")

        assertNull(result)
    }

    private fun buildClient(): TrustlyStorageClient {
        return TrustlyStorageClient(
            context = context,
            storageUrl = storageUrl,
            cryptoEngine = cryptoEngine,
            cookieManagerProvider = { cookieManager },
            legacyCookieSync = { legacySyncCallCount += 1 }
        )
    }
}
