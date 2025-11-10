package net.trustly.android.sdk.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import net.trustly.android.sdk.interfaces.TrustlyEvents
import net.trustly.android.sdk.views.events.TrustlyEventsImpl

class TrustlyCustomTabsManagerActivity : Activity() {

    private lateinit var trustlyEvents: TrustlyEvents
    private lateinit var customTabsIntent: CustomTabsIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        trustlyEvents = TrustlyEventsImpl()

        val url = intent.getStringExtra(URL)
        val useWebView = intent.getBooleanExtra(USE_WEBVIEW, false)
        if (url != null) openCustomTabsIntent(this, url, useWebView)
    }

    override fun onResume() {
        super.onResume()

        if (intent.hasExtra(ESTABLISH_DATA)) {
            val transactionDetails = intent.getSerializableExtra(ESTABLISH_DATA) as Map<String, String>
            if (transactionDetails[STATUS_PARAM] == SUCCESS_STATUS_PARAM) {
                this.trustlyEvents.handleOnReturn(null, transactionDetails)
            } else {
                this.trustlyEvents.handleOnCancel(null, transactionDetails)
            }
        }
        finish()
    }

    private fun openCustomTabsIntent(context: Context, url: String, useWebView: Boolean) {
        try {
            val builder = CustomTabsIntent.Builder()
            customTabsIntent = builder.build()
            customTabsIntent.intent.setPackage("com.android.chrome")
            if (useWebView) {
                customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            }
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

    companion object {

        const val ESTABLISH_DATA = "establishData"
        private const val URL = "URL"
        private const val USE_WEBVIEW = "USE_WEBVIEW"
        private const val STATUS_PARAM = "status"
        private const val SUCCESS_STATUS_PARAM = "2"

        fun startIntent(context: Context, url: String, useWebView: Boolean = true) {
            val intent = Intent(context, TrustlyCustomTabsManagerActivity::class.java)
                .putExtra(URL, url)
                .putExtra(USE_WEBVIEW, useWebView)
            context.startActivity(intent)
        }

    }

}