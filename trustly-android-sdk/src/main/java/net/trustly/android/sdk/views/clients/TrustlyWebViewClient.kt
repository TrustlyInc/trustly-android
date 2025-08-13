package net.trustly.android.sdk.views.clients

import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import net.trustly.android.sdk.util.EstablishDataManager
import net.trustly.android.sdk.util.UrlUtils
import net.trustly.android.sdk.views.TrustlyView
import net.trustly.android.sdk.views.events.TrustlyEvents
import java.util.regex.Pattern

/**
 * WebView client for Trustly View
 */
class TrustlyWebViewClient(
    private val trustlyView: TrustlyView,
    private val returnURL: String,
    private val cancelURL: String,
    private val trustlyEvents: TrustlyEvents
) : WebViewClient() {

    /**
     * @param view The WebView that is initiating the callback.
     * @param url The URL to be loaded.
     * @return `true` to cancel the current load, otherwise return `false`.
     */
    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView, url: String) =
        handleWebViewClientShouldOverrideUrlLoading(url)

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

    override fun onPageFinished(view: WebView, url: String) {
        handleWebViewClientOnPageFinished(view, trustlyView)
    }

    @Suppress("DEPRECATION")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError?
    ) {
        val url = request.url.toString()
        this.onReceivedError(view, 0, error.toString(), url)
    }

    @Deprecated("Deprecated in Java")
    override fun onReceivedError(
        view: WebView,
        errorCode: Int,
        description: String,
        failingUrl: String
    ) {
        trustlyEvents.handleErrorLog(description, failingUrl)
    }

    private fun handleWebViewClientShouldOverrideUrlLoading(url: String): Boolean {
        if (url.startsWith(returnURL) || url.startsWith(cancelURL)) {
            val queryParametersFromUrl = UrlUtils.getQueryParametersFromUrl(url)
            if (url.startsWith(returnURL)) {
                trustlyEvents.handleOnReturn(trustlyView, queryParametersFromUrl)
            } else {
                trustlyEvents.handleOnCancel(trustlyView, queryParametersFromUrl)
            }
            trustlyEvents.notifyClose()
            return true
        } else if (url.startsWith("msg://push?")) {
            val params = url.split("\\|".toRegex()).toTypedArray()
            if (params.first().contains("PayWithMyBank.createTransaction")) {
                val establishData = EstablishDataManager.updatePaymentProviderId(if (params.size > 1) params[1] else "").toMap()
                trustlyEvents.handleOnWidgetBankSelected(trustlyView, establishData)
            }
            return true
        }
        return false
    }

    private fun handleWebViewClientOnPageFinished(view: WebView, trustlyView: TrustlyView) {
        view.loadUrl("javascript:TrustlyNativeSDK.resize(document.body.scrollWidth, document.body.scrollHeight)")

        val title = view.title
        title?.let {
            val p = Pattern.compile("\\d+")
            val m = p.matcher(it)
            while (m.find()) {
                val n = m.group().toLong() / 100
                if (n == 4L || n == 5L) {
                    trustlyEvents.handleOnCancel(trustlyView, HashMap())
                }
            }
        }
        trustlyEvents.notifyWidgetLoaded()
    }

}