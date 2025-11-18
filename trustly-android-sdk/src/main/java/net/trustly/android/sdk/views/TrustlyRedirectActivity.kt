package net.trustly.android.sdk.views

import android.app.Activity
import android.net.Uri
import android.os.Bundle

class TrustlyRedirectActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.data != null && intent.data!!.getQueryParameter(STATUS_PARAM) != null) {
            val transactionDetail = getTransactionDetailFromUri(intent.data!!)
            TrustlyCustomTabsManagerActivity().apply {
                startIntent(this@TrustlyRedirectActivity, transactionDetail)
            }
        }
        finish()
    }

    private fun getTransactionDetailFromUri(appLinkData: Uri): Map<String, String> {
        return mapOf(
            Pair(TRANSACTION_ID_PARAM, appLinkData.getQueryParameter(TRANSACTION_ID_PARAM)!!),
            Pair(TRANSACTION_TYPE_PARAM, appLinkData.getQueryParameter(TRANSACTION_TYPE_PARAM)!!),
            Pair(PANEL_PARAM, appLinkData.getQueryParameter(PANEL_PARAM)!!),
            Pair(PAYMENT_TYPE_PARAM, appLinkData.getQueryParameter(PAYMENT_TYPE_PARAM)!!),
            Pair(STATUS_PARAM, appLinkData.getQueryParameter(STATUS_PARAM)!!)
        )
    }

    companion object {

        private const val TRANSACTION_ID_PARAM = "transactionId"
        private const val TRANSACTION_TYPE_PARAM = "transactionType"
        private const val PANEL_PARAM = "panel"
        private const val PAYMENT_TYPE_PARAM = "payment.paymentType"
        private const val STATUS_PARAM = "status"

    }

}