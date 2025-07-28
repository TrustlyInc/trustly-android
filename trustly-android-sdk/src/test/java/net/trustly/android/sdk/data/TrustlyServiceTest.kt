package net.trustly.android.sdk.data

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection
import java.net.URL

class TrustlyServiceTest {

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
    fun shouldValidateTrustlyServiceGetSettingDataWhenReturnSuccess() {
        val settingsFake = Settings(StrategySetting("in-app-browser"))

        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK)
        `when`(mockTrustlyUrlFetcher.isUrlAvailable()).thenReturn(true)
        `when`(mockTrustlyUrlFetcher.getResponse()).thenReturn("{'settings': {'integrationStrategy': 'in-app-browser'}}")

        TrustlyService(mockTrustlyUrlFetcher).getSettingsData(URL, TOKEN) { assertEquals(settingsFake, it) }
    }

    @Test
    fun shouldValidateTrustlyServiceGetSettingDataWhenReturnSuccessForbidden() {
        val settingsFake = Settings(StrategySetting("webview"))

        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN)
        `when`(mockTrustlyUrlFetcher.isUrlAvailable()).thenReturn(false)
        `when`(mockTrustlyUrlFetcher.getErrorResponse()).thenReturn("Forbidden")

        TrustlyService(mockTrustlyUrlFetcher).getSettingsData(URL, TOKEN) { assertEquals(settingsFake, it) }
    }

    @Test
    fun shouldValidateTrustlyServiceGetSettingDataWhenReturnSuccessWithNullResponse() {
        val settingsFake = Settings(StrategySetting("webview"))

        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK)
        `when`(mockTrustlyUrlFetcher.isUrlAvailable()).thenThrow(NullPointerException("Null pointer exception"))
        `when`(mockTrustlyUrlFetcher.getResponse()).thenReturn(null)

        TrustlyService(mockTrustlyUrlFetcher).getSettingsData(URL, TOKEN) { assertEquals(settingsFake, it) }
    }

    @Test
    fun shouldValidateTrustlyServicePostLightboxUrlWhenReturnSuccess() {
        val lightboxUrlFake = "http://trustly.lightboxurl.com"

        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_MOVED_TEMP)
        `when`(mockTrustlyUrlFetcher.isUrlRedirect()).thenReturn(true)
        `when`(mockTrustlyUrlFetcher.getHeaderField(anyString())).thenReturn("http://trustly.lightboxurl.com")

        TrustlyService(mockTrustlyUrlFetcher).postLightboxUrl(
            URL,
            "user-agent",
            byteArrayOf()
        ) {
            assertEquals(
                lightboxUrlFake,
                it
            )
        }
    }

    @Test
    fun shouldValidateTrustlyServicePostLightboxUrlWhenReturnSuccessForbidden() {
        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN)
        `when`(mockTrustlyUrlFetcher.isUrlRedirect()).thenReturn(false)
        `when`(mockTrustlyUrlFetcher.getErrorResponse()).thenReturn("Forbidden")

        TrustlyService(mockTrustlyUrlFetcher).postLightboxUrl(
            URL,
            "user-agent",
            byteArrayOf()
        ) {
            assertEquals(
                null,
                it
            )
        }
    }

    @Test
    fun shouldValidateTrustlyServicePostLightboxUrlWhenReturnSuccessThrowsException() {
        `when`(mockTrustlyUrlFetcher.getResponseCode()).thenReturn(HttpURLConnection.HTTP_MOVED_TEMP)
        `when`(mockTrustlyUrlFetcher.isUrlRedirect()).thenReturn(true)
        `when`(mockTrustlyUrlFetcher.getHeaderField(anyString())).thenThrow(NullPointerException())

        TrustlyService(mockTrustlyUrlFetcher).postLightboxUrl(
            URL,
            "user-agent",
            byteArrayOf()
        ) {
            assertEquals(
                null,
                it
            )
        }
    }

    companion object {

        const val TOKEN = "RXN0YWJsaXNoRGF0YVN0cmluZw=="
        const val URL = "https://www.trustly.com"

    }

}