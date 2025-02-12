package net.trustly.android.sdk.util

import net.trustly.android.sdk.util.TrustlyConstants.PAYMENT_PROVIDER_ID
import org.junit.Assert.assertEquals
import org.junit.Test

class EstablishDataManagerTest {

    @Test
    fun testEstablishDataManagerUpdateEstablishData() {
        val establishData = mapOf("key1" to "value1", "key2" to "value2")
        EstablishDataManager.updateEstablishData(establishData)
        val result = EstablishDataManager.getEstablishData()
        assertEquals(establishData, result)
    }

    @Test
    fun testEstablishDataManagerUpdatePaymentProviderId() {
        val paymentProviderId = "provider123"
        EstablishDataManager.updatePaymentProviderId(paymentProviderId)
        val result = EstablishDataManager.getEstablishData()
        assertEquals(paymentProviderId, result[PAYMENT_PROVIDER_ID])
    }

}