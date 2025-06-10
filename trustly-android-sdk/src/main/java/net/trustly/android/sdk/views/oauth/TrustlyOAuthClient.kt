package net.trustly.android.sdk.views.oauth

import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import net.trustly.android.sdk.views.TrustlyCustomTabsManager.openCustomTabsIntent
import net.trustly.android.sdk.views.TrustlyView

/**
 * Client for Trustly OAuth login
 */
class TrustlyOAuthClient : WebViewClient() {

    /**
     * @param view The WebView that is initiating the callback.
     * @param url The URL to be loaded.
     * @return `true` to cancel the current load, otherwise return `false`.
     */
    @Deprecated("")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (TrustlyView.isLocalEnvironment() || (
                    (url.contains("paywithmybank.com") || url.contains("trustly.one"))
                            && url.contains("/oauth/login/"))
        ) {
            openCustomTabsIntent(view.context, url)
        }
        return true
    }

    /**
     * @param view The WebView that is initiating the callback.
     * @param request Object containing the details of the request.
     * @return `true` to cancel the current load, otherwise return `false`.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Suppress("deprecation")
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val url = request.url.toString()
        return this.shouldOverrideUrlLoading(view, url)
    }

}