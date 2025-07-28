package net.trustly.android.sdk.util

import net.trustly.android.sdk.util.TrustlyConstants.PAYMENT_PROVIDER_ID

object EstablishDataManager {

    private var establishData: MutableMap<String, String> = mutableMapOf()

    fun updateEstablishData(establishData: Map<String, String>) {
        EstablishDataManager.establishData.putAll(establishData)
    }

    fun getEstablishData() = establishData

    fun updatePaymentProviderId(paymentProviderId: String): MutableMap<String, String> {
        establishData[PAYMENT_PROVIDER_ID] = paymentProviderId
        return getEstablishData()
    }

}