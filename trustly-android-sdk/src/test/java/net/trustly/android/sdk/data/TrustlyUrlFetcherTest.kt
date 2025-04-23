package net.trustly.android.sdk.data

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class TrustlyUrlFetcherTest {

    @Mock
    private lateinit var mockURL: URL

    @Mock
    private lateinit var mockConnection: HttpURLConnection

    @Mock
    private lateinit var mockInputStream: InputStream

    private lateinit var trustlyUrlFetcher: TrustlyUrlFetcher

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        `when`(mockURL.openConnection()).thenReturn(mockConnection)

        trustlyUrlFetcher = TrustlyUrlFetcher()
        trustlyUrlFetcher.openConnection(mockURL)
    }

    @After
    fun tearDown() {
        clearInvocations(mockURL, mockConnection, mockInputStream)
    }

    @Test
    fun testTrustlyUrlFetcher() {
        trustlyUrlFetcher.openConnection(mockURL)
        assertNotNull(trustlyUrlFetcher)
    }

    @Test
    fun testTrustlyUrlFetcherResponseCodeForbidden() {
        `when`(mockConnection.responseCode).thenReturn(HttpURLConnection.HTTP_FORBIDDEN)

        trustlyUrlFetcher.openConnection(mockURL)
        assertEquals(403, trustlyUrlFetcher.getResponseCode())
    }

    @Test
    fun testTrustlyUrlFetcherResponseCodeSuccess() {
        `when`(mockConnection.responseCode).thenReturn(HttpURLConnection.HTTP_OK)

        trustlyUrlFetcher.openConnection(mockURL)
        assertEquals(200, trustlyUrlFetcher.getResponseCode())
    }

    @Test
    fun testTrustlyUrlFetcherIsUrlAvailable() {
        `when`(mockConnection.responseCode).thenReturn(HttpURLConnection.HTTP_OK)

        trustlyUrlFetcher.openConnection(mockURL)
        assertTrue(trustlyUrlFetcher.isUrlAvailable())
    }

    @Test
    fun testTrustlyUrlFetcherIsUrlNotAvailable() {
        `when`(mockConnection.responseCode).thenReturn(HttpURLConnection.HTTP_FORBIDDEN)

        trustlyUrlFetcher.openConnection(mockURL)
        assertFalse(trustlyUrlFetcher.isUrlAvailable())
    }

    @Test
    fun testTrustlyUrlFetcherSetRequestMethodGET() {
        `when`(mockConnection.responseCode).thenReturn(HttpURLConnection.HTTP_FORBIDDEN)

        trustlyUrlFetcher.openConnection(mockURL)
        trustlyUrlFetcher.setRequestMethod("GET")
        verify(mockConnection, times(1)).setRequestMethod("GET")
    }

    @Test
    fun testTrustlyUrlFetcherSetRequestMethodPOST() {
        `when`(mockConnection.responseCode).thenReturn(HttpURLConnection.HTTP_FORBIDDEN)

        trustlyUrlFetcher.openConnection(mockURL)
        trustlyUrlFetcher.setRequestMethod("POST")
        verify(mockConnection, times(1)).setRequestMethod("POST")
    }

    @Test
    fun testTrustlyUrlFetcherSetRequestProperty() {
        trustlyUrlFetcher.openConnection(mockURL)
        trustlyUrlFetcher.setRequestProperty("Content-type", "application/json")
        verify(mockConnection, times(1)).setRequestProperty("Content-type", "application/json")
    }

    @Test
    fun testTrustlyUrlFetcherSetTimeout() {
        `when`(mockConnection.responseCode).thenReturn(HttpURLConnection.HTTP_FORBIDDEN)

        trustlyUrlFetcher.openConnection(mockURL)
        trustlyUrlFetcher.setTimeOut(50000)
        verify(mockConnection, times(1)).setConnectTimeout(50000)
        verify(mockConnection, times(1)).setReadTimeout(50000)
    }

    @Test
    fun testTrustlyUrlFetcherGetResponse() {
        `when`(mockConnection.inputStream).thenReturn(ByteArrayInputStream(byteArrayOf("111".toByte())))

        trustlyUrlFetcher.openConnection(mockURL)
        assertEquals("o", trustlyUrlFetcher.getResponse())
    }

    @Test
    fun testTrustlyUrlFetcherGetErrorResponse() {
        `when`(mockConnection.responseMessage).thenReturn("Error response message")

        trustlyUrlFetcher.openConnection(mockURL)
        assertEquals("Error response message", trustlyUrlFetcher.getErrorResponse())
    }

    @Test
    fun testTrustlyUrlFetcherDisconnect() {
        trustlyUrlFetcher.disconnect()
        verify(mockConnection, times(1)).disconnect()
    }

}