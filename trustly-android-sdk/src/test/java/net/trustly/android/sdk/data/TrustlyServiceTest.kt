package net.trustly.android.sdk.data

import net.trustly.android.sdk.BuildConfig
import net.trustly.android.sdk.data.model.Settings
import net.trustly.android.sdk.data.model.StrategySetting
import net.trustly.android.sdk.data.model.Tracking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection
import java.net.URL

class TrustlyServiceTest {

    private var settingsResult: Settings? = null
    private val trackingFake = Tracking(BuildConfig.SDK_VERSION, "Android", "19", "Galaxy S25", "2025-04-23 14:37:12.701",
        "error", "Caused by: java.lang.NullPointerException at TrustlyWidget.updateEstablishData(establishData)")

    @Mock
    private lateinit var mockURL: URL

    @Mock
    private lateinit var mockConnection: HttpURLConnection

    @Mock
    private lateinit var mockTrustlyUrlFetcher: TrustlyUrlFetcher

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        `when`(mockURL.openConnection()).thenReturn(mockConnection)
    }

    @After
    fun tearDown() {
        settingsResult = null

        clearInvocations(mockURL, mockConnection, mockTrustlyUrlFetcher)
    }

    @Test
    fun testGetSettingDataWhenReturnSuccess() {
        val settingsFake = Settings(StrategySetting("in-app-browser"))

        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK)
        `when`(mockTrustlyUrlFetcher.isUrlAvailable()).thenReturn(true)
        `when`(mockTrustlyUrlFetcher.getResponse()).thenReturn("{'settings': {'integrationStrategy': 'in-app-browser'}}")

        TrustlyService(mockTrustlyUrlFetcher).getSettingsData(URL, TOKEN) { assertEquals(settingsFake, it) }
    }

    @Test
    fun testGetSettingDataWhenReturnSuccessForbidden() {
        val settingsFake = Settings(StrategySetting("webview"))

        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN)
        `when`(mockTrustlyUrlFetcher.isUrlAvailable()).thenReturn(false)
        `when`(mockTrustlyUrlFetcher.getErrorResponse()).thenReturn("Forbidden")

        TrustlyService(mockTrustlyUrlFetcher).getSettingsData(URL, TOKEN) { assertEquals(settingsFake, it) }
    }

    @Test
    fun testGetSettingDataWhenReturnSuccessWithNullResponse() {
        val settingsFake = Settings(StrategySetting("webview"))

        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK)
        `when`(mockTrustlyUrlFetcher.isUrlAvailable()).thenThrow(NullPointerException("Null pointer exception"))
        `when`(mockTrustlyUrlFetcher.getResponse()).thenReturn(null)

        TrustlyService(mockTrustlyUrlFetcher).getSettingsData(URL, TOKEN) { assertEquals(settingsFake, it) }
    }

    @Test
    fun testPostTrackingDataErrorWhenReturnSuccess() {
        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK)
        `when`(mockTrustlyUrlFetcher.isUrlAvailable()).thenReturn(true)
        `when`(mockTrustlyUrlFetcher.getResponse()).thenReturn("{'trustlySdkVersion': '${BuildConfig.SDK_VERSION}', 'deviceSystem': 'Android', 'deviceVersion': '19', 'deviceModel': 'Galaxy S25', 'createdAt': '2025-04-23 14:37:12.701', 'type': 'error', 'message': 'Caused by: java.lang.NullPointerException at TrustlyWidget.updateEstablishData(establishData)'}")

        TrustlyService(mockTrustlyUrlFetcher).postTrackingData(URL, trackingFake) { assertEquals(trackingFake, it) }
    }

    @Test
    fun testPostTrackingDataErrorWhenReturnSuccessForbidden() {
        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN)
        `when`(mockTrustlyUrlFetcher.isUrlAvailable()).thenReturn(false)
        `when`(mockTrustlyUrlFetcher.getResponse()).thenReturn("Forbidden")

        TrustlyService(mockTrustlyUrlFetcher).postTrackingData(URL, trackingFake) { assertEquals(trackingFake, it) }
    }

    @Test
    fun testPostTrackingDataErrorWhenReturnSuccessNullResponse() {
        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK)
        `when`(mockTrustlyUrlFetcher.isUrlAvailable()).thenThrow(NullPointerException("Null pointer exception"))
        `when`(mockTrustlyUrlFetcher.getResponse()).thenReturn("Forbidden")

        TrustlyService(mockTrustlyUrlFetcher).postTrackingData(URL, trackingFake) { assertEquals(trackingFake, it) }
    }

    companion object {

        const val URL = "https://www.trustly.com"
        const val TOKEN = "RXN0YWJsaXNoRGF0YVN0cmluZw=="

    }

}