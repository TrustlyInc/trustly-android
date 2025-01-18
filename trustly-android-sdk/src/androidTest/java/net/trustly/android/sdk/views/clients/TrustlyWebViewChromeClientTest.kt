package net.trustly.android.sdk.views.clients

import android.os.Handler
import android.os.Message
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.views.TrustlyView
import net.trustly.android.sdk.views.events.TrustlyEvents
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
@MediumTest
class TrustlyWebViewChromeClientTest : TrustlyActivityTest() {

    @Mock
    private lateinit var mockWebView: WebView

    @Mock
    private lateinit var mockHitTestResult: WebView.HitTestResult

    @Mock
    private lateinit var mockWebViewTransport: WebViewTransport

    @Mock
    private lateinit var mockHandler: Handler

    private lateinit var trustlyView: TrustlyView

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.openMocks(this)

        `when`(mockWebView.hitTestResult).thenReturn(mockHitTestResult)
        `when`(mockHandler.sendMessageDelayed(any(), anyLong())).thenReturn(true)
    }

    @After
    override fun tearDown() {
        super.tearDown()

        clearInvocations(mockWebView, mockHitTestResult, mockWebViewTransport, mockHandler)
    }

    @Test
    fun shouldValidateTrustlyWebViewChromeClientInstance() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewChromeClient = TrustlyWebViewChromeClient(activity, trustlyView, TrustlyEvents())
            assertNotNull(trustlyWebViewChromeClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewChromeClientInstanceExternalUrlCallback() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.setOnExternalUrlCallback { _, _ -> }
            val trustlyWebViewChromeClient = TrustlyWebViewChromeClient(activity, trustlyView, trustlyEvents)
            assertNotNull(trustlyWebViewChromeClient)
        }
    }

    @Test(expected = NullPointerException::class)
    fun shouldValidateTrustlyWebViewChromeClientOnCreateWindowMethodEmptyMessage() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyWebViewChromeClient = TrustlyWebViewChromeClient(activity, trustlyView, TrustlyEvents())
            trustlyWebViewChromeClient.onCreateWindow(webView,
                isDialog = false,
                isUserGesture = false,
                resultMsg = Message()
            )
        }
    }

    @Test(expected = ClassCastException::class)
    fun shouldValidateTrustlyWebViewChromeClientOnCreateWindowMethod() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val message = Message.obtain()
            message.obj = Any()
            val trustlyWebViewChromeClient = TrustlyWebViewChromeClient(activity, trustlyView, TrustlyEvents())
            trustlyWebViewChromeClient.onCreateWindow(webView,
                isDialog = false,
                isUserGesture = false,
                resultMsg = message
            )
            assertNotNull(trustlyWebViewChromeClient)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewChromeClientHandleWebChromeClientOnCreateWindowMethodWithUrl() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockHitTestResult.type).thenReturn(1)
            `when`(mockHitTestResult.extra).thenReturn("www.trustly.com")

            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewChromeClient = TrustlyWebViewChromeClient(activity, trustlyView, TrustlyEvents())
            trustlyWebViewChromeClient.onCreateWindow(mockWebView,
                isDialog = false,
                isUserGesture = false,
                resultMsg = Message()
            )
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewChromeClientHandleWebChromeClientOnCreateWindowMethodWithEmptyUrl() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockHitTestResult.type).thenReturn(1)

            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewChromeClient = TrustlyWebViewChromeClient(activity, trustlyView, TrustlyEvents())
            val result = trustlyWebViewChromeClient.onCreateWindow(mockWebView,
                isDialog = false,
                isUserGesture = false,
                resultMsg = Message()
            )
            assertFalse(result)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewChromeClientHandleWebChromeClientOnCreateWindowMethodWithNullUrl() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockHitTestResult.type).thenReturn(1)
            `when`(mockHitTestResult.extra).thenReturn(null)

            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyWebViewChromeClient = TrustlyWebViewChromeClient(activity, trustlyView, TrustlyEvents())
            val result = trustlyWebViewChromeClient.onCreateWindow(mockWebView,
                isDialog = false,
                isUserGesture = false,
                resultMsg = Message()
            )
            assertFalse(result)
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewChromeClientHandleWebChromeClientOnCreateWindowMethodWithUrlExternalUrlCallback() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockHitTestResult.type).thenReturn(1)
            `when`(mockHitTestResult.extra).thenReturn("www.trustly.com")

            trustlyView = TrustlyView(activity)
            trustlyView.onExternalUrl { _: Trustly, params: Map<String, String>? ->
                assertEquals("www.trustly.com", params?.get("url"))
            }
            val trustlyWebViewChromeClient = TrustlyWebViewChromeClient(activity, trustlyView, TrustlyEvents())
            trustlyWebViewChromeClient.onCreateWindow(mockWebView,
                isDialog = false,
                isUserGesture = false,
                resultMsg = Message()
            )
        }
    }

    @Test
    fun shouldValidateTrustlyWebViewChromeClientHandleWebChromeClientOnCreateWindowMethodWindowOpen() {
        scenario.onActivity { activity: MockActivity ->
            `when`(mockHitTestResult.type).thenReturn(0)

            trustlyView = TrustlyView(activity)

            val message = Message()
            message.obj = mockWebViewTransport
            message.target = mockHandler
            val trustlyWebViewChromeClient = TrustlyWebViewChromeClient(activity, trustlyView, TrustlyEvents())
            val result = trustlyWebViewChromeClient.onCreateWindow(mockWebView,
                isDialog = false,
                isUserGesture = false,
                resultMsg = message
            )
            assertTrue(result)
        }
    }

}