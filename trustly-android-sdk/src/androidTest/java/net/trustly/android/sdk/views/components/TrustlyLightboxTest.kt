package net.trustly.android.sdk.views.components

import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.data.Settings
import net.trustly.android.sdk.data.StrategySetting
import net.trustly.android.sdk.util.api.APIRequestManager
import net.trustly.android.sdk.util.api.APIRequestStorage
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.MockitoAnnotations
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyLightboxTest : TrustlyActivityTest() {

    @Mock
    lateinit var webView: WebView

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.openMocks(this)
    }

    @After
    override fun tearDown() {
        super.tearDown()

        clearInvocations(webView)
    }

    @Test
    fun shouldValidateTrustlyLightboxInstance() {
        scenario.onActivity { activity ->
            val trustlyLightbox = TrustlyLightbox(activity, webView, "returnUrl", "cancelUrl", {}, {})
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceEmptyEstablishData() {
        scenario.onActivity { activity ->
            val trustlyLightbox = TrustlyLightbox(activity, webView, "returnUrl", "cancelUrl", {}, {})
            trustlyLightbox.updateEstablishData(mapOf(), 0)
            assertNotNull(trustlyLightbox)
        }
        waitToCloseCustomTabs()
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowser() {
        scenario.onActivity { activity ->
            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = TrustlyLightbox(activity, webView, "returnUrl", "cancelUrl", {}, {})
            trustlyLightbox.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            waitToCloseCustomTabs(4L)
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowserWithUrlScheme() {
        scenario.onActivity { activity ->
            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = TrustlyLightbox(activity, webView, "returnUrl", "cancelUrl", {}, {})

            val establishData = EstablishDataMock.getEstablishDataValues()
            establishData["metadata.urlScheme"] = "urlscheme://"

            trustlyLightbox.updateEstablishData(establishData, 0)
            waitToCloseCustomTabs(4L)
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowserWithUrlSchemeError() {
        scenario.onActivity { activity ->
            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = TrustlyLightbox(activity, webView, "returnUrl", "cancelUrl", {}, {})

            val establishData = EstablishDataMock.getEstablishDataValues()
            establishData["metadata.urlScheme"] = "urlscheme://"
            establishData["env"] = "error"

            trustlyLightbox.updateEstablishData(establishData, 0)
            waitToCloseCustomTabs(4L)
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationWebView() {
        scenario.onActivity { activity ->
            APIRequestStorage.saveData(activity, "API_REQUEST" ,Calendar.getInstance().timeInMillis.toString())

            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("webview")))

            val trustlyLightbox = TrustlyLightbox(activity, webView, "returnUrl", "cancelUrl", {}, {})
            trustlyLightbox.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            assertNotNull(trustlyLightbox)
        }
    }

}