package net.trustly.android.sdk.views

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

object TrustlyCustomTabsManager {

    fun openCustomTabsIntent(context: Context, url: String) {
        try {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.intent.setPackage("com.android.chrome")
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            customTabsIntent.launchUrl(context, url.toUri())
        } catch (e: Exception) {
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

}