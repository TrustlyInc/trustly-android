package net.trustly.android.sdk.views.oauth;

import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import net.trustly.android.sdk.util.CustomTabsManager;
import net.trustly.android.sdk.views.TrustlyView;

public class TrustlyOAuthClient extends WebViewClient {

    @Deprecated
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (TrustlyView.isLocalEnvironment() || ((url.contains("paywithmybank.com") || url.contains("trustly.one")) && url.contains("/oauth/login/"))) {
            CustomTabsManager.openCustomTabsIntent(view.getContext(), url);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        return this.shouldOverrideUrlLoading(view, url);
    }

}
