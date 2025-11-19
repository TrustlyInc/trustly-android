package net.trustly.android.sdk.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import java.io.Serializable

class TrustlyRedirectActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.data != null && intent.data!!.getQueryParameter(STATUS_PARAM) != null) {
            val transactionDetail = getTransactionDetailFromUri(intent.data!!)
            val intent = Intent(this, TrustlyCustomTabsManagerActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(
                    TrustlyCustomTabsManagerActivity.ESTABLISH_DATA,
                    transactionDetail as Serializable
                )
            startActivity(intent)
        } else {
            val intent = Intent(this, TrustlyCustomTabsManagerActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
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