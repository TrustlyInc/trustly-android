package net.trustly.android.sdk.util

import android.content.Context
import net.trustly.android.sdk.util.TrustlyConstants.PAYMENT_PROVIDER_ID
import net.trustly.android.sdk.util.last_used.LastUsedBankManager

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

    fun saveLastUsedBankByTrustlyContext(context: Context, transactionDetails: Map<String, String>): Map<String, String> {
        transactionDetails[TrustlyConstants.TRUSTLY_CONTEXT]?.let {
            LastUsedBankManager.saveLastUsedBank(context, it)
        }
        return transactionDetails.toMutableMap().apply {
            remove(TrustlyConstants.TRUSTLY_CONTEXT)
        }
    }

}