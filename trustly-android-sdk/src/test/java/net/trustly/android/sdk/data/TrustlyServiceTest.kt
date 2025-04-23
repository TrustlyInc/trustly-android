package net.trustly.android.sdk.data

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

    private val URL = "https://www.trustly.com"
    private var settingsResult: Settings? = null

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

    companion object {

        const val TOKEN = "RXN0YWJsaXNoRGF0YVN0cmluZw=="

    }

}