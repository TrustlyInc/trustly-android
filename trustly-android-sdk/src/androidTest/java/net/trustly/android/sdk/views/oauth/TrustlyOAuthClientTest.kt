package net.trustly.android.sdk.views.oauth

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.interfaces.TrustlyEvents
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.views.TrustlyView
import net.trustly.android.sdk.views.events.TrustlyEventsImpl
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyOAuthClientTest : TrustlyActivityTest() {

    private lateinit var trustlyEvents: TrustlyEvents

    @Before
    override fun setUp() {
        super.setUp()

        trustlyEvents = TrustlyEventsImpl
    }

    @Test
    fun shouldValidateTrustlyOAuthClientInstance() {
        scenario.onActivity { activity ->
            val trustlyOAuthClient = TrustlyOAuthClient(
                TrustlyView(activity.applicationContext), trustlyEvents
            )
            assertNotNull(trustlyOAuthClient)
        }
    }

    @Test
    fun shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithEmptyUrl() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyOAuthClient(
                TrustlyView(activity.applicationContext), trustlyEvents
            ).shouldOverrideUrlLoading(WebView(activity), "")
        }
    }

    @Test
    fun shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithUrl() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyView.setIsLocalEnvironment(false)
            val result = TrustlyOAuthClient(
                TrustlyView(activity.applicationContext), trustlyEvents
            ).shouldOverrideUrlLoading(WebView(activity), "www.url.com")
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithPayWithMyBankUrl() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyView.setIsLocalEnvironment(false)
            val result = TrustlyOAuthClient(
                TrustlyView(activity.applicationContext), trustlyEvents
            ).shouldOverrideUrlLoading(WebView(activity), getWebResourceRequest("www.paywithmybank.com"))
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithBothPayWithMyBankUrlAndOAuthLoginPath() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyView.setIsLocalEnvironment(false)
            val result = TrustlyOAuthClient(
                TrustlyView(activity.applicationContext), trustlyEvents
            ).shouldOverrideUrlLoading(WebView(activity), getWebResourceRequest("www.paywithmybank.com/oauth/login/1223456"))
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithTrustlyOneUrl() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyView.setIsLocalEnvironment(false)
            val result = TrustlyOAuthClient(
                TrustlyView(activity.applicationContext), trustlyEvents
            ).shouldOverrideUrlLoading(WebView(activity), getWebResourceRequest("www.trustly.one"))
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithBothTrustlyOneUrlAndOAuthLoginPath() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyView.setIsLocalEnvironment(false)
            val result = TrustlyOAuthClient(
                TrustlyView(activity.applicationContext), trustlyEvents
            ).shouldOverrideUrlLoading(WebView(activity), getWebResourceRequest("www.trustly.one/oauth/login/1223456"))
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithOAuthLoginPath() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyView.setIsLocalEnvironment(false)
            val result = TrustlyOAuthClient(
                TrustlyView(activity.applicationContext), trustlyEvents
            ).shouldOverrideUrlLoading(WebView(activity), getWebResourceRequest("www.url.com/oauth/login/1223456"))
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithIsLocalEnvironment() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyView.setIsLocalEnvironment(true)
            val result = TrustlyOAuthClient(
                TrustlyView(activity.applicationContext), trustlyEvents
            ).shouldOverrideUrlLoading(WebView(activity), getWebResourceRequest("www.url.com"))
            assertTrue(result)
        }
    }

    private fun getWebResourceRequest(url: String): WebResourceRequest {
        return object : WebResourceRequest {
            override fun getUrl() = Uri.parse(url)

            override fun isForMainFrame() = false

            override fun isRedirect() = false

            override fun hasGesture() = false

            override fun getMethod() = "GET"

            override fun getRequestHeaders(): Map<String, String> = emptyMap()
        }
    }

}