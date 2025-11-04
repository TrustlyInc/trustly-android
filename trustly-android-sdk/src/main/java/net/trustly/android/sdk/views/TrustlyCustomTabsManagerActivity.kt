package net.trustly.android.sdk.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

class TrustlyCustomTabsManagerActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TrustlyCTMActivity", "onCreate")

        val url = intent.getStringExtra(URL)
        Log.d("TrustlyCTMActivity", url.toString())
        if (url != null) {
            openCustomTabsIntent(this, url)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d("TrustlyCTMActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()

        Log.d("TrustlyCTMActivity", "onResume")
    }

    override fun onRestart() {
        super.onRestart()

        Log.d("TrustlyCTMActivity", "onRestart")
    }

    override fun onStop() {
        super.onStop()

        Log.d("TrustlyCTMActivity", "onStop")
    }

    override fun onPause() {
        super.onPause()

        Log.d("TrustlyCTMActivity", "onPause")
    }

    private fun openCustomTabsIntent(context: Context, url: String) {
        try {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.intent.setPackage("com.android.chrome")
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

        const val URL = "URL"

        fun startIntent(context: Context, url: String) {
            val intent = Intent(context, TrustlyCustomTabsManagerActivity::class.java)
                .putExtra(URL, url)
            context.startActivity(intent)
        }

    }

}