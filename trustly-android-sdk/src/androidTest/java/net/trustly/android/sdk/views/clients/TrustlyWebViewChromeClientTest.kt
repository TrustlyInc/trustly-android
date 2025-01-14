package net.trustly.android.sdk.views.clients

import android.os.Message
import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.views.TrustlyView
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class TrustlyWebViewChromeClientTest : TrustlyActivityTest() {

    private lateinit var trustlyView: TrustlyView

    @Test
    fun shouldValidateTrustlyViewChromeClientInstance() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyViewChromeClient = TrustlyWebViewChromeClient(trustlyView)
            assertNotNull(trustlyViewChromeClient)
        }
    }

    @Test(expected = NullPointerException::class)
    fun shouldValidateTrustlyViewChromeClientOnCreateWindowMethodNullMessage() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val trustlyViewChromeClient = TrustlyWebViewChromeClient(trustlyView)
            trustlyViewChromeClient.onCreateWindow(webView, false, false, null)
        }
    }

    @Test(expected = ClassCastException::class)
    fun shouldValidateTrustlyViewChromeClientOnCreateWindowMethod() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val webView = WebView(activity.applicationContext)
            val message = Message.obtain()
            message.obj = Any()
            val trustlyViewChromeClient = TrustlyWebViewChromeClient(trustlyView)
            trustlyViewChromeClient.onCreateWindow(webView, false, false, message)
            assertNotNull(trustlyViewChromeClient)
        }
    }

}