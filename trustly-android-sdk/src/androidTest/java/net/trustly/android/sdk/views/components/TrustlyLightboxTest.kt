package net.trustly.android.sdk.views.components

import android.webkit.WebSettings
import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.data.Settings
import net.trustly.android.sdk.data.StrategySetting
import net.trustly.android.sdk.util.api.APIRequestManager
import net.trustly.android.sdk.util.api.APIRequestStorage
import net.trustly.android.sdk.views.events.TrustlyEvents
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyLightboxTest : TrustlyActivityTest() {

    @Mock
    lateinit var mockWebView: WebView

    @Mock
    lateinit var mockWebSettings: WebSettings

    private lateinit var trustlyEvents: TrustlyEvents

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.openMocks(this)

        trustlyEvents = TrustlyEvents()

        `when`(mockWebView.settings).thenReturn(mockWebSettings)
    }

    @After
    override fun tearDown() {
        super.tearDown()

        clearInvocations(mockWebView, mockWebSettings)
    }

    @Test
    fun shouldValidateTrustlyLightboxInstance() {
        scenario.onActivity { activity ->
            val trustlyLightbox = TrustlyLightbox(activity, mockWebView, "returnUrl", "cancelUrl", trustlyEvents)
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceEmptyEstablishData() {
        scenario.onActivity { activity ->
            val trustlyLightbox = TrustlyLightbox(activity, mockWebView, "returnUrl", "cancelUrl", trustlyEvents)
            trustlyLightbox.updateEstablishData(mapOf(), 0)
            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowser() {
        scenario.onActivity { activity ->
            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = TrustlyLightbox(activity, mockWebView, "returnUrl", "cancelUrl", trustlyEvents)
            trustlyLightbox.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowserWithUrlScheme() {
        scenario.onActivity { activity ->
            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = TrustlyLightbox(activity, mockWebView, "returnUrl", "cancelUrl", trustlyEvents)

            val establishData = EstablishDataMock.getEstablishDataValues()
            establishData["metadata.urlScheme"] = "urlscheme://"

            trustlyLightbox.updateEstablishData(establishData, 0)
            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowserWithUrlSchemeError() {
        scenario.onActivity { activity ->
            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = TrustlyLightbox(activity, mockWebView, "returnUrl", "cancelUrl", trustlyEvents)

            val establishData = EstablishDataMock.getEstablishDataValues()
            establishData["metadata.urlScheme"] = "urlscheme://"
            establishData["env"] = "error"

            trustlyLightbox.updateEstablishData(establishData, 0)
            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationWebView() {
        scenario.onActivity { activity ->
            APIRequestStorage.saveData(activity, "API_REQUEST" ,Calendar.getInstance().timeInMillis.toString())

            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("webview")))

            val trustlyLightbox = TrustlyLightbox(activity, mockWebView, "returnUrl", "cancelUrl", trustlyEvents)
            trustlyLightbox.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

}