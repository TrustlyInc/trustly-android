package net.trustly.android.sdk.views

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import net.trustly.android.sdk.views.events.TrustlyEvents

class TrustlyRedirectActivity : Activity() {

    private lateinit var trustlyEvents: TrustlyEvents

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trustlyEvents = TrustlyEvents

        Log.d("TrustlyRedirectActivity", "intent $intent")
        Log.d("TrustlyRedirectActivity", "intent data ${intent.data}")

        Log.d("TrustlyRedirectActivity", intent.data!!.toString())
        //TODO Resolver fluxo usando app-to-app com InAppBrowser
        if (intent.data!!.getQueryParameter(STATUS_PARAM) != null) {
            val transactionDetail = getTransactionDetailFromUri(intent.data!!)
            Log.d("TrustlyRedirectActivity", transactionDetail.toString())

            if (transactionDetail[STATUS_PARAM] == "2")
                this.trustlyEvents.handleOnReturn(null, transactionDetail)
            else
                this.trustlyEvents.handleOnCancel(null, transactionDetail)

            finish()
        } else {
            //fluxo webview
            Log.d("TrustlyRedirectActivity", "Has no extras")
            finish()
        }
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

        const val TRANSACTION_ID_PARAM = "transactionId"
        const val TRANSACTION_TYPE_PARAM = "transactionType"
        const val PANEL_PARAM = "panel"
        const val PAYMENT_TYPE_PARAM = "payment.paymentType"
        const val STATUS_PARAM = "status"
        const val URL = "URL"

    }

}