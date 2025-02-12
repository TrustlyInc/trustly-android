package net.trustly.android.sdk.views.clients

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.interfaces.TrustlyCallback
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.views.TrustlyView
import net.trustly.android.sdk.views.events.TrustlyEvents
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
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

    @Mock
    private lateinit var mockOnCancelCallback: TrustlyCallback<Trustly, Map<String, String>>

    private lateinit var trustlyEvents: TrustlyEvents
    private lateinit var trustlyView: TrustlyView

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.openMocks(this)

        trustlyEvents = TrustlyEvents()
    }

    @After
    override fun tearDown() {
        super.tearDown()

        clearInvocations(mockWebView, mockOnCancelCallback)
    }

    @Test
    fun shouldValidateTrustlyWebViewClientInstance() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithUrlString() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
            assertFalse(result)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithWebResourceRequest() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, getWebResourceRequest())
            assertNotNull(trustlyWebViewClient)
            assertFalse(result)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinished() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            trustlyWebViewClient.onPageFinished(webView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleNull() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn(null)
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, TrustlyEvents())
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleNotNull() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn("title1234567890")
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, TrustlyEvents())
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleFour() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn("title400")
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, TrustlyEvents())
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleFourOnCancel() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn("title400")
            trustlyEvents.setOnCancelCallback(mockOnCancelCallback)
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
            verify(mockOnCancelCallback, times(1)).handle(trustlyView, HashMap())
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleFive() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn("title500")
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, TrustlyEvents())
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnPageFinishedWebViewTitleFiveOnCancel() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockWebView.title).thenReturn("title500")
            trustlyEvents.setOnCancelCallback(mockOnCancelCallback)
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            trustlyWebViewClient.onPageFinished(mockWebView, URL)
            assertNotNull(trustlyWebViewClient)
            verify(mockOnCancelCallback, times(1)).handle(trustlyView, HashMap())
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceived() {
        scenario.onActivity { activity: MockActivity ->
            trustlyEvents.setOnCancelCallback(mockOnCancelCallback)
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL)
            assertNotNull(trustlyWebViewClient)
            verify(mockOnCancelCallback, times(1)).handle(trustlyView, HashMap())
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedOnCancelLocalEnvironmentJPG() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyEvents.setOnCancelCallback(mockOnCancelCallback)
            TrustlyView.setIsLocalEnvironment(true)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/image.jpg")
            verify(mockOnCancelCallback, times(0)).handle(trustlyView, HashMap())
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelJPG() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyEvents.setOnCancelCallback(mockOnCancelCallback)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/image.jpg")
            verify(mockOnCancelCallback, times(0)).handle(trustlyView, HashMap())
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallNullOnCancelDOC() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyEvents.setOnCancelCallback(mockOnCancelCallback)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/document.doc")
            verify(mockOnCancelCallback, times(1)).handle(trustlyView, HashMap())
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelDOC() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyEvents.setOnCancelCallback(mockOnCancelCallback)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/document.doc")
            verify(mockOnCancelCallback, times(1)).handle(trustlyView, HashMap())
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelLocalEnvironmentDOC() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyEvents.setOnCancelCallback(mockOnCancelCallback)
            TrustlyView.setIsLocalEnvironment(true)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/document.doc")
            verify(mockOnCancelCallback, times(0)).handle(trustlyView, HashMap())
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelLocalEnvironment() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyEvents.setOnCancelCallback(mockOnCancelCallback)
            TrustlyView.setIsLocalEnvironment(true)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/image.jpg")
            verify(mockOnCancelCallback, times(0)).handle(trustlyView, HashMap())
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelNotLocalEnvironmentOnCancel() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyEvents.setOnCancelCallback(mockOnCancelCallback)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            trustlyWebViewClient.onReceivedError(webView, 0, "", "$URL/image.doc")
            verify(mockOnCancelCallback, times(1)).handle(trustlyView, HashMap())
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithReturnUrl() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://return?www.url.com")
            assertTrue(result)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithReturnNullUrl() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://return?www.url.com")
            assertTrue(result)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithCancelUrl() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://cancel?www.url.com")
            assertTrue(result)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithCancelNullUrl() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyView.onCancel(null)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://cancel?www.url.com")
            assertTrue(result)
            assertNotNull(trustlyWebViewClient)
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
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://push?PayWithMyBank.createTransaction|123456")
            assertTrue(result)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithPushUrlCreateTransactionWithNoBankId() {
        scenario.onActivity { activity: MockActivity ->
            val values = mapOf(
                "key1" to "value1",
                "key2" to "value2",
                "key3" to "value3"
            )

            trustlyView = TrustlyView(activity.applicationContext)
            trustlyView.establish(values)

            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://push?PayWithMyBank.createTransaction")
            assertTrue(result)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithPushUrlCreateTransactionWithEmptyBankId() {
        scenario.onActivity { activity: MockActivity ->
            val values = mapOf(
                "key1" to "value1",
                "key2" to "value2",
                "key3" to "value3"
            )

            trustlyView = TrustlyView(activity.applicationContext)
            trustlyView.establish(values)

            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://push?PayWithMyBank.createTransaction!")
            assertTrue(result)
            assertNotNull(trustlyWebViewClient)
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
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://push?PayWithMyBank.createTransaction|123456")
            assertTrue(result)
            assertNotNull(trustlyWebViewClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithPushUrlWidgetLoaded() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewClient = TrustlyWebViewClient(trustlyView, RETURN_URL, CANCEL_URL, trustlyEvents)
            TrustlyView.setIsLocalEnvironment(false)
            val result = trustlyWebViewClient.shouldOverrideUrlLoading(webView, "msg://push?PayWithMyBank.widgetLoaded|123456")
            assertTrue(result)
            assertNotNull(trustlyWebViewClient)
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