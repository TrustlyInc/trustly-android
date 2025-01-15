package net.trustly.android.sdk.views

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent

object TrustlyCustomTabsManager {

    private const val TAG = "CustomTabsManager"

    fun openCustomTabsIntent(context: Context, url: String) {
        try {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.intent.setPackage("com.android.chrome")
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            customTabsIntent.launchUrl(context, Uri.parse(url))
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
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