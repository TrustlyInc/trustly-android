package net.trustly.android.sdk.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import net.trustly.android.sdk.views.events.TrustlyEvents

class TrustlyRedirectActivity : Activity() {

    private var trustlyEvents: TrustlyEvents = TrustlyEvents

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra(URL)
        if (url != null) {
            openCustomTabsIntent(this, url)
        } else {
            Log.d("TrustlyRedirectActivity", intent.data!!.toString())
            if (intent.data!!.getQueryParameter(STATUS_PARAM) != null) {
                val transactionDetail = getTransactionDetailFromUri(intent.data!!)
                Log.d("TrustlyRedirectActivity", transactionDetail.toString())

                if (transactionDetail[STATUS_PARAM] == "2")
                    this.trustlyEvents.handleOnReturn(null, transactionDetail)
                else
                    this.trustlyEvents.handleOnCancel(null, mapOf())
                finish()
            } else {
                Log.d("TrustlyRedirectActivity", "Has no extras")
                finish()
            }
        }
    }

    private fun openCustomTabsIntent(context: Context, url: String) {
        try {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.intent.setPackage("com.android.chrome")
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            customTabsIntent.launchUrl(context, url.toUri())
        } catch (_: Exception) {
            showDisabledBrowserMessage(context)
        }
    }

    private fun showDisabledBrowserMessage(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Please note: This app requires Google Chrome to function properly. If Google Chrome is not installed on your device, please install it from the Google Play Store or enable it in your system settings to ensure full compatibility and functionality.")
            .setTitle("Google Chrome browser required")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
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

        fun startIntent(context: Context, url: String) {
            val intent = Intent(context, TrustlyRedirectActivity::class.java)
                .putExtra(URL, url)
            context.startActivity(intent)
        }

    }

}