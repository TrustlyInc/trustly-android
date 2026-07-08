package net.trustly.android.sdk.util.storage

import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyStorageClientTest {

    private val storageUrl = "https://storage.trustly.com"
    private val key = "trustly_storage_it_key"

    private lateinit var storageClient: TrustlyStorageClient
    private lateinit var cookieManager: CookieManager

    @Before
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.runOnMainSync {
            WebView(instrumentation.targetContext)
        }

        cookieManager = CookieManager.getInstance()
        storageClient = TrustlyStorageClient(instrumentation.targetContext, storageUrl)
        clearCookieForKey()
    }

    @After
    fun tearDown() {
        clearCookieForKey()
    }

    @Test
    fun shouldSetAndGetOnApi19AndAbove() {
        val setResult = storageClient.setItem(key, "Chase")
        val restored = storageClient.getItem(key)

        assertTrue(setResult)
        assertEquals("Chase", restored)
    }

    @Test
    fun shouldReturnNullWhenKeyNotFound() {
        val result = storageClient.getItem("missing_${System.currentTimeMillis()}")

        assertNull(result)
    }

    @Test
    fun shouldClearTamperedPayload() {
        val setResult = storageClient.setItem(key, "sensitive")
        assertTrue(setResult)

        cookieManager.setCookie(
            storageUrl,
            "$key=invalid_payload; Max-Age=31536000; Path=/; Secure; HttpOnly; SameSite=Strict"
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush()
        } else {
            CookieSyncManager.createInstance(InstrumentationRegistry.getInstrumentation().targetContext)
            CookieSyncManager.getInstance().sync()
        }

        val restored = storageClient.getItem(key)
        val cookieHeader = cookieManager.getCookie(storageUrl)

        assertNull(restored)
        assertFalse(cookieHeader?.contains("$key=") == true)
    }

    private fun clearCookieForKey() {
        cookieManager.setCookie(
            storageUrl,
            "$key=; Max-Age=0; Path=/; Secure; HttpOnly; SameSite=Strict"
        )
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush()
        } else {
            CookieSyncManager.createInstance(InstrumentationRegistry.getInstrumentation().targetContext)
            CookieSyncManager.getInstance().sync()
        }
    }
}
