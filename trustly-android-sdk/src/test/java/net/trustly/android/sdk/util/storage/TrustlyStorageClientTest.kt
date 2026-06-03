package net.trustly.android.sdk.util.storage

import android.webkit.CookieManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class TrustlyStorageClientTest {

    private lateinit var cookieManager: CookieManager
    private lateinit var cryptoEngine: TrustlyCryptoEngine

    private val cookieJar = linkedMapOf<String, String>()
    private val storageUrl = "https://storage.trustly.com"

    @Before
    fun setUp() {
        cookieManager = mock(CookieManager::class.java)
        cryptoEngine = mock(TrustlyCryptoEngine::class.java)

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

        val appA = TrustlyStorageClient(storageUrl, cryptoEngine) { cookieManager }
        val appB = TrustlyStorageClient(storageUrl, cryptoEngine) { cookieManager }

        val setResult = appA.setItem("bankPreference", "Chase")
        val readResult = appB.getItem("bankPreference")

        assertTrue(setResult)
        assertEquals("Chase", readResult)
        verify(cookieManager, times(1)).flush()
    }

    @Test
    fun shouldReturnNullWhenItemMissing() {
        val client = TrustlyStorageClient(storageUrl, cryptoEngine) { cookieManager }

        val result = client.getItem("missing")

        assertNull(result)
        verify(cryptoEngine, never()).decrypt(anyString())
    }

    @Test
    fun shouldCleanupCookieWhenDecryptReturnsNull() {
        cookieJar["bank_key"] = "tampered_payload"
        `when`(cryptoEngine.decrypt("tampered_payload")).thenReturn(null)

        val client = TrustlyStorageClient(storageUrl, cryptoEngine) { cookieManager }

        val result = client.getItem("bank_key")

        assertNull(result)
        assertFalse(cookieJar.containsKey("bank_key"))
        verify(cookieManager, times(1)).setCookie(storageUrl, "bank_key=; Max-Age=0; Path=/; Secure; HttpOnly; SameSite=Strict")
        verify(cookieManager, atLeastOnce()).flush()
    }

    @Test
    fun shouldPreserveUtf8Value() {
        val utf8Value = "Crédito São Paulo 🌎"
        `when`(cryptoEngine.encrypt(utf8Value)).thenReturn("utf8_payload")
        `when`(cryptoEngine.decrypt("utf8_payload")).thenReturn(utf8Value)

        val client = TrustlyStorageClient(storageUrl, cryptoEngine) { cookieManager }

        val setResult = client.setItem("last_used", utf8Value)
        val restored = client.getItem("last_used")

        assertTrue(setResult)
        assertEquals(utf8Value, restored)
    }

    @Test
    fun shouldReturnFalseWhenEncryptionFails() {
        `when`(cryptoEngine.encrypt("value")).thenReturn(null)

        val client = TrustlyStorageClient(storageUrl, cryptoEngine) { cookieManager }

        val result = client.setItem("k", "value")

        assertFalse(result)
        verify(cookieManager, never()).setCookie(anyString(), anyString())
        verify(cookieManager, never()).flush()
    }

    @Test
    fun shouldReturnNullWhenCookieAccessThrowsSecurityException() {
        `when`(cookieManager.getCookie(anyString())).thenThrow(SecurityException("blocked"))

        val client = TrustlyStorageClient(storageUrl, cryptoEngine) { cookieManager }

        val result = client.getItem("bank")

        assertNull(result)
    }
}

