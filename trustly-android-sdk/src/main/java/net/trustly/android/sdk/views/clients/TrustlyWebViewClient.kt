package net.trustly.android.sdk.views.clients

import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.interfaces.TrustlyCallback
import net.trustly.android.sdk.views.TrustlyView
import java.util.regex.Pattern

/**
 * WebView client for Trustly View
 */
class TrustlyWebViewClient(
    private val trustlyView: TrustlyView,
    private val returnURL: String,
    private val cancelURL: String,
    private val onCancel: TrustlyCallback<Trustly, Map<String, String>>?,
    private val notifyStatus: () -> Unit,
    private val urlCallback: (String) -> Unit,
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
        handleWebViewClientOnReceivedError(trustlyView, failingUrl)
    }

    private fun handleWebViewClientShouldOverrideUrlLoading(
        url: String
    ): Boolean {
        if (url.startsWith(returnURL) || url.startsWith(cancelURL) || url.startsWith("msg://push?")) {
            urlCallback.invoke(url)
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
                    onCancel?.handle(trustlyView, HashMap())
                }
            }
        }
        notifyStatus.invoke()
    }

    private fun handleWebViewClientOnReceivedError(trustlyView: TrustlyView, failingUrl: String?) {
        val isAssetFile =
            failingUrl?.matches("([^\\\\s]+(\\\\.(?i)(jpg|jpeg|svg|png|css|gif|webp))$)".toRegex())
        if (!TrustlyView.isLocalEnvironment() && !isAssetFile!!) {
            onCancel?.handle(trustlyView, HashMap())
        }
    }

}