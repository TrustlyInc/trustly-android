package net.trustly.android.sdk.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;

public class CustomTabsManager {

    protected CustomTabsManager() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static void openCustomTabsIntent(Context context, String url) {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } catch (Exception e) {
            Log.e("CustomTabsManager", e.toString());
            showDisabledBrowserMessage(context);
        }
    }

    private static void showDisabledBrowserMessage(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please note: This app requires Google Chrome to function properly. If Google Chrome is not installed on your device, please install it from the Google Play Store or enable it in your system settings to ensure full compatibility and functionality.")
                .setTitle("Google Chrome browser required")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
