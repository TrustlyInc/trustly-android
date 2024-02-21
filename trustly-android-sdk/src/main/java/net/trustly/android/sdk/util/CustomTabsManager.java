package net.trustly.android.sdk.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;

import net.trustly.android.sdk.R;

public class CustomTabsManager {

    private CustomTabsManager() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static void openCustomTabsIntent(Context context, String url) {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage(context.getString(R.string.chrome_package));
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } catch (Exception e) {
            Log.e("CustomTabsManager", e.toString());
            showDisabledBrowserMessage(context);
        }
    }

    private static void showDisabledBrowserMessage(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.chrome_disabled_message))
                .setTitle(context.getString(R.string.chrome_disabled_title))
                .setPositiveButton(context.getString(R.string.chrome_disabled_button),
                        (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
