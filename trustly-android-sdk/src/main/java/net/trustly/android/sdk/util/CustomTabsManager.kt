package net.trustly.android.sdk.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent

object CustomTabsManager {

    fun openCustomTabsIntent(context: Context?, url: String?) {
        try {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.apply {
                intent.setPackage("com.android.chrome")
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }.run {
                launchUrl(context!!, Uri.parse(url))
            }
        } catch (e: Exception) {
            Log.e("CustomTabsManager", e.toString())
            showDisabledBrowserMessage(context)
        }
    }

    private fun showDisabledBrowserMessage(context: Context?) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Please note: This app requires Google Chrome to function properly. If Google Chrome is not installed on your device, please install it from the Google Play Store or enable it in your system settings to ensure full compatibility and functionality.")
            .setTitle("Google Chrome browser required")
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

}