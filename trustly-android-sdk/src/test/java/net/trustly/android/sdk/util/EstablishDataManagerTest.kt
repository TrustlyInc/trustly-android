package net.trustly.android.sdk.util

import android.content.Context
import net.trustly.android.sdk.util.TrustlyConstants.PAYMENT_PROVIDER_ID
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.mock

class EstablishDataManagerTest {

    @Mock
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        EstablishDataManager.getEstablishData().clear()

        mockContext = mock(Context::class.java)
    }

    @After
    fun tearDown() {
        clearInvocations(mockContext)
    }

    @Test
    fun shouldValidateEstablishDataManagerUpdateEstablishData() {
        val establishData = mapOf("key1" to "value1", "key2" to "value2")
        EstablishDataManager.updateEstablishData(establishData)
        val result = EstablishDataManager.getEstablishData()
        assertEquals(establishData, result)
    }

    @Test
    fun shouldValidateEstablishDataManagerUpdatePaymentProviderId() {
        val paymentProviderId = "provider123"
        EstablishDataManager.updatePaymentProviderId(paymentProviderId)
        val result = EstablishDataManager.getEstablishData()
        assertEquals(paymentProviderId, result[PAYMENT_PROVIDER_ID])
    }

    @Test
    fun shouldValidateEstablishDataManagerSaveLastUsedBankByTrustlyContext() {
        val establishData = mapOf("trustlyContext" to "context123", "key" to "value", "key2" to "value2")
        val result = EstablishDataManager.saveLastUsedBankByTrustlyContext(mockContext, establishData)
        assertEquals("value", result["key"])
        assertEquals("value2", result["key2"])
        assertEquals(null, result["trustlyContext"])
    }

    @Test
    fun shouldValidateEstablishDataManagerSaveLastUsedBankWithoutTrustlyContext() {
        val establishData = mapOf("key" to "value", "key2" to "value2")
        val result = EstablishDataManager.saveLastUsedBankByTrustlyContext(mockContext, establishData)
        assertEquals("value", result["key"])
        assertEquals("value2", result["key2"])
        assertEquals(null, result["trustlyContext"])
    }

}