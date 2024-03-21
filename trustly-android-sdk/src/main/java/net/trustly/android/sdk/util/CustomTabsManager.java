package net.trustly.android.sdk.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

public class CustomTabsManager {

    private CustomTabsManager() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static void openCustomTabs(Context context, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage("com.android.chrome");
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

}