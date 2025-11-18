package net.trustly.android.sdk.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import net.trustly.android.sdk.interfaces.TrustlyEvents
import net.trustly.android.sdk.views.events.TrustlyEventsImpl

class TrustlyCustomTabsManagerActivity : Activity() {

    private lateinit var customTabsIntent: CustomTabsIntent

    private var trustlyEvents: TrustlyEvents? = null
    private var trustlyView: TrustlyView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("CTM", "onCreate")

        val url = intent.getStringExtra(URL)
        Log.d("CTM", url.toString())
        val useWebView = intent.getBooleanExtra(USE_WEBVIEW, false)
        if (url != null) {
            openCustomTabsIntent(this, url, useWebView)
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d("CTM", "onResume")
        Log.d("CTM", intent.data.toString())

        if (intent.getSerializableExtra(ESTABLISH_DATA) != null) {
            val transactionDetails = intent.getSerializableExtra(ESTABLISH_DATA) as Map<String, String>
            Log.d("CTM", transactionDetails.toString())

            if (this.trustlyEvents == null)
                this.trustlyEvents = TrustlyEventsImpl

            if (transactionDetails[STATUS_PARAM] == SUCCESS_STATUS_PARAM) {
                this.trustlyEvents!!.handleOnReturn(null, transactionDetails)
            } else {
                this.trustlyEvents!!.handleOnCancel(null, transactionDetails)
            }
            finish()
        } else {
            Log.d("CTM", "else")
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("CTM", "onDestroy")
    }

    private fun openCustomTabsIntent(context: Context, url: String, useWebView: Boolean) {
        try {
            val builder = CustomTabsIntent.Builder()
            customTabsIntent = builder.build()
            customTabsIntent.intent.setPackage("com.android.chrome")
            if (useWebView) {
                customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            } else {
                customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
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

    fun startIntent(
        context: Context,
        url: String,
        useWebView: Boolean = true
    ) {
        val intent = Intent(context, TrustlyCustomTabsManagerActivity::class.java)
            .putExtra(URL, url)
            .putExtra(USE_WEBVIEW, useWebView)
        context.startActivity(intent)
    }

    fun setEventsCallback(trustlyView: TrustlyView?, trustlyEvents: TrustlyEvents) {
        this.trustlyView = trustlyView
        this.trustlyEvents = trustlyEvents
    }

    companion object {

        const val ESTABLISH_DATA = "establishData"
        private const val URL = "URL"
        private const val USE_WEBVIEW = "USE_WEBVIEW"
        private const val STATUS_PARAM = "status"
        private const val SUCCESS_STATUS_PARAM = "2"

    }

}