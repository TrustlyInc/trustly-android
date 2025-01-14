package net.trustly.android.sdk.views.clients

import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import net.trustly.android.sdk.views.TrustlyView

/**
 * WebView client for Trustly View
 */
class TrustlyWebViewClient(private val trustlyView: TrustlyView) : WebViewClient() {

    /**
     * @param view The WebView that is initiating the callback.
     * @param url The URL to be loaded.
     * @return `true` to cancel the current load, otherwise return `false`.
     */
    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?) =
        trustlyView.handleWebViewClientShouldOverrideUrlLoading(trustlyView, url)

    /**
     * @param view The WebView that is initiating the callback.
     * @param request Object containing the details of the request.
     * @return `true` to cancel the current load, otherwise return `false`.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Suppress("deprecation")
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
        val url = request.url.toString()
        return this.shouldOverrideUrlLoading(view, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        trustlyView.handleWebViewClientOnPageFinished(view, trustlyView)
    }

    @Suppress("DEPRECATION")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest,
        error: WebResourceError?
    ) {
        val url = request.url.toString()
        this.onReceivedError(view, 0, "", url)
    }

    @Deprecated("Deprecated in Java")
    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        trustlyView.handleWebViewClientOnReceivedError(trustlyView, failingUrl)
    }

}