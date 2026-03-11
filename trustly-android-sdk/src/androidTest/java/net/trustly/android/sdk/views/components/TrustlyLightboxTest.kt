package net.trustly.android.sdk.views.components

import android.content.Context
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.data.Settings
import net.trustly.android.sdk.data.StrategySetting
import net.trustly.android.sdk.interfaces.TrustlyEvents
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.util.api.APIRequestManager
import net.trustly.android.sdk.util.api.APIRequestStorage
import net.trustly.android.sdk.util.last_used.LastUsedBankManager
import net.trustly.android.sdk.views.TrustlyView
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
    private lateinit var mockWebView: WebView

    @Mock
    private lateinit var mockWebSettings: WebSettings

    @Mock
    private lateinit var mockTrustlyEvents: TrustlyEvents

    @Mock
    private lateinit var mockContext: Context

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.openMocks(this)

        `when`(mockWebView.settings).thenReturn(mockWebSettings)
    }

    @After
    override fun tearDown() {
        super.tearDown()

        clearInvocations(mockWebView, mockWebSettings, mockTrustlyEvents, mockContext)
    }

    @Test
    fun shouldValidateTrustlyLightboxInstance() {
        scenario.onActivity { activity ->
            val trustlyLightbox = getTrustlyLightboxInstance(activity)
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceEmptyEstablishData() {
        scenario.onActivity { activity ->
            val trustlyLightbox = getTrustlyLightboxInstance(activity)
            trustlyLightbox.updateEstablishData(mapOf(), 0)
            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowser() {
        scenario.onActivity { activity ->
            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = getTrustlyLightboxInstance(activity)
            trustlyLightbox.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowserWithUrlScheme() {
        scenario.onActivity { activity ->
            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = getTrustlyLightboxInstance(activity)

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

            val trustlyLightbox = getTrustlyLightboxInstance(activity)

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

            val trustlyLightbox = getTrustlyLightboxInstance(activity)
            trustlyLightbox.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowserWithMetadataFlowType() {
        scenario.onActivity { activity ->
            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = getTrustlyLightboxInstance(activity)
            val establishData = EstablishDataMock.getEstablishDataValues()
            establishData["metadata.flowType"] = "webview"

            trustlyLightbox.updateEstablishData(establishData, 0)
            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataIntegrationInAppBrowserCustomerFromCanada() {
        scenario.onActivity { activity ->
            APIRequestManager.saveAPIRequestSettings(activity, Settings(StrategySetting("in-app-browser")))

            val trustlyLightbox = getTrustlyLightboxInstance(activity)
            val establishData = EstablishDataMock.getEstablishDataValues()
            establishData["customer.address.country"] = "CA"

            trustlyLightbox.updateEstablishData(establishData, 0)
            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataWithLastUsedBank() {
        scenario.onActivity { activity ->
            LastUsedBankManager.saveLastUsedBank(activity, "eyJsYXN0VXNlZCI6eyJVUyI6IjEyMzQ1Njc4OSJ9fQ==")

            val trustlyLightbox = getTrustlyLightboxInstance(activity)
            val establishData = EstablishDataMock.getEstablishDataValues()
            trustlyLightbox.updateEstablishData(establishData, 0)

            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

    @Test
    fun shouldValidateTrustlyLightboxInstanceWithEstablishDataWithLastUsedBankNull() {
        scenario.onActivity { activity ->
            `when`(
                mockContext.getSharedPreferences(
                    "LAST_USED_BANK", Context.MODE_PRIVATE
                )
            ).thenReturn(null)

            val trustlyLightbox = getTrustlyLightboxInstance(activity)
            val establishData = EstablishDataMock.getEstablishDataValues()
            trustlyLightbox.updateEstablishData(establishData, 0)

            waitToCloseCustomTabs()
            assertNotNull(trustlyLightbox)
        }
    }

    private fun getTrustlyLightboxInstance(activity: MockActivity): TrustlyLightbox {
        return TrustlyLightbox(
            TrustlyView(activity),
            activity,
            mockWebView,
            "returnUrl",
            "cancelUrl",
            mockTrustlyEvents
        )
    }

}