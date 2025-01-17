package net.trustly.android.sdk.views.clients

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.views.TrustlyView
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyWebViewClientTest : TrustlyActivityTest() {

    private val URL: String = "www.trustly.com"
    private val RETURN_URL = "msg://return"
    private val CANCEL_URL = "msg://cancel"

    @Mock
    private lateinit var mockWebView: WebView

    private lateinit var trustlyView: TrustlyView

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.openMocks(this)
    }

    @After
    override fun tearDown() {
        super.tearDown()

        clearInvocations(mockWebView)
    }

    @Test
    fun shouldValidateTrustlyWebViewClientInstance() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithUrlString() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.shouldOverrideUrlLoading(webView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithWebResourceRequest() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.shouldOverrideUrlLoading(webView, getWebResourceRequest())
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinished() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onPageFinished(webView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleNull() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn(null)

            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {
                assert(true)
            }, {})
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleNotNull() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn("title1234567890")

            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {
                assert(true)
            }, {})
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleFour() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn("title4")

            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {
                assert(true)
            }, {})
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleFourOnCancel() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn("title4")

            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, { _: Trustly, data: Map<String, String>? ->
                assertNotNull(data)
            }, {
                assert(true)
            }, {})
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleFive() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn("title5")

            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {
                assert(true)
            }, {})
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleFiveOnCancel() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn("title5")

            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, { _: Trustly, data: Map<String, String>? ->
                assertNotNull(data)
            }, {
                assert(true)
            }, {})
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceived() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedOnCancel() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, { _: Trustly, data: Map<String, String>? ->
                assertNotNull(data)
            }, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedOnCancelLocalEnvironment() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            TrustlyView.setIsLocalEnvironment(true)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, { _: Trustly, data: Map<String, String>? ->
                assertNotNull(data)
            }, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedOnCancelLocalEnvironmentJPG() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            TrustlyView.setIsLocalEnvironment(true)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, { _: Trustly, data: Map<String, String>? ->
                assertNotNull(data)
            }, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/image.jpg")
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancel() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyView.onCancel { trustly: Trustly?, _: Map<String, String?>? -> assertNotNull(trustly) }
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", null)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelNotLocalEnvironment() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            TrustlyView.setIsLocalEnvironment(false)
            trustlyView.onCancel { trustly: Trustly?, _: Map<String, String?>? -> assertNotNull(trustly) }
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelJPG() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/image.jpg")
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelDOC() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/document.doc")
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelLocalEnvironment() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            TrustlyView.setIsLocalEnvironment(true)
            trustlyView.onCancel { trustly: Trustly?, _: Map<String, String?>? -> assertNotNull(trustly) }
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/image.jpg")
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelNullLocalEnvironment() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            TrustlyView.setIsLocalEnvironment(true)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/image.jpg")
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelLocalEnvironmentDOC() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            TrustlyView.setIsLocalEnvironment(true)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/document.doc")
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelNotLocalEnvironmentDOC() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            TrustlyView.setIsLocalEnvironment(false)
            trustlyView.onCancel { trustly: Trustly?, _: Map<String, String?>? -> assertNotNull(trustly) }
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/document.doc")
        }
    }

    @Test(expected = NullPointerException::class)
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedNullPointerExceptionOnCancel() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", null)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedFailureJPG() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/image.jpg")
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedFailureDOC() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/document.doc")
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedWithWebResourceRequest() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            trustlyWebViewClient.onReceivedError(webView, getWebResourceRequest(), null)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithReturnUrl() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://return?www.url.com")
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithReturnNullUrl() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyView.onReturn(null)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://return?www.url.com")
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithCancelUrl() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://cancel?www.url.com")
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithCancelNullUrl() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyView.onCancel(null)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {
                assertNotNull(it)
            })
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://cancel?www.url.com")
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithPushUrlCreateTransaction() {
        scenario.onActivity { activity: MockActivity ->
            val values = mapOf(
                "key1" to "value1",
                "key2" to "value2",
                "key3" to "value3"
            )

            trustlyView = TrustlyView(activity.applicationContext)
            trustlyView.establish(values)

            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://push?PayWithMyBank.createTransaction|123456")
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithPushUrlCreateTransactionWidgetBankSelectedNull() {
        scenario.onActivity { activity: MockActivity ->
            val values = mapOf(
                "key1" to "value1",
                "key2" to "value2",
                "key3" to "value3"
            )

            trustlyView = TrustlyView(activity.applicationContext)
            trustlyView.onBankSelected(null)
            trustlyView.establish(values)

            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://push?PayWithMyBank.createTransaction|123456")
            assertTrue(result)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithPushUrlWidgetLoaded() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, null, {}, {})
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://push?PayWithMyBank.widgetLoaded|123456")
            assertTrue(result)
        }
    }

    private fun getWebResourceRequest(): WebResourceRequest {
        return object : WebResourceRequest {
            override fun getUrl() = Uri.parse(URL)

            override fun isForMainFrame() = false

            override fun isRedirect() = false

            override fun hasGesture() = false

            override fun getMethod() = "GET"

            override fun getRequestHeaders(): Map<String, String> = emptyMap()
        }
    }


}