package net.trustly.android.sdk.util;

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
        }
    }

}
