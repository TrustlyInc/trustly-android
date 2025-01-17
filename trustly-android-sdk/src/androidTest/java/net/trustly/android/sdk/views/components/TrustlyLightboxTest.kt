package net.trustly.android.sdk.views.components

import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.data.APIMethod
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
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyLightboxTest : TrustlyActivityTest() {

    @Mock
    lateinit var webView: WebView

    @Mock
    private lateinit var mockAPIMethod: APIMethod

    @Mock
    private lateinit var mockCall: Call<Settings>

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.openMocks(this)

        `when`(webView.postUrl(anyString(), any())).then {  }
        `when`(mockAPIMethod.getSettings(anyString())).thenReturn(mockCall)
    }

    @After
    override fun tearDown() {
        super.tearDown()

        clearInvocations(webView, mockAPIMethod, mockCall)
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
            assertNotNull(trustlyLightbox)
        }
        waitToCloseCustomTabs()
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowserWithUrlScheme() {
        scenario.onActivity { activity ->
            val settingsFake = Settings(StrategySetting("in-app-browser"))
            val mockResponse = Response.success(settingsFake)
            mockCallbackResponse(mockResponse)

            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = TrustlyLightbox(activity, webView, "returnUrl", "cancelUrl", {}, {})

            val establishData = EstablishDataMock.getEstablishDataValues()
            establishData["metadata.urlScheme"] = "urlscheme://"

            trustlyLightbox.updateEstablishData(establishData, 0)
        }
        waitToCloseCustomTabs()
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowserWithUrlSchemeError() {
        scenario.onActivity { activity ->
            val mockResponse = Throwable("Error 401")
            mockCallbackFailure(mockResponse)

            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = TrustlyLightbox(activity, webView, "returnUrl", "cancelUrl", {}, {})

            val establishData = EstablishDataMock.getEstablishDataValues()
            establishData["metadata.urlScheme"] = "urlscheme://"

            trustlyLightbox.updateEstablishData(establishData, 0)
        }
        waitToCloseCustomTabs()
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

    @Suppress("UNCHECKED_CAST")
    private fun mockCallbackResponse(mockResponse: Response<Settings>) {
        `when`(mockCall.enqueue(any())).then {
            val callback = it.arguments.first() as retrofit2.Callback<Settings>
            callback.onResponse(mockCall, mockResponse)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun mockCallbackFailure(mockResponse: Throwable) {
        `when`(mockCall.enqueue(any())).then {
            val callback = it.arguments.first() as retrofit2.Callback<Settings>
            callback.onFailure(mockCall, mockResponse)
        }
    }

}