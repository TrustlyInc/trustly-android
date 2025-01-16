package net.trustly.android.sdk.views.clients

import android.content.Context
import android.os.Message
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.interfaces.TrustlyCallback
import net.trustly.android.sdk.views.TrustlyCustomTabsManager
import net.trustly.android.sdk.views.TrustlyView
import net.trustly.android.sdk.views.oauth.TrustlyOAuthView

/**
 * Chrome client for Trustly View
 */
class TrustlyWebViewChromeClient(
    private val context: Context,
    private val trustlyView: TrustlyView,
    private val onExternalUrl: TrustlyCallback<Trustly, Map<String, String>>?
) : WebChromeClient() {

    override fun onCreateWindow(
        view: WebView,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message
    ) = handleWebChromeClientOnCreateWindow(view, resultMsg)

    private fun handleWebChromeClientOnCreateWindow(view: WebView, resultMsg: Message): Boolean {
        val result = view.hitTestResult
        if (result.type == 0) {
            //window.open
            val trustlyOAuthView = TrustlyOAuthView(context)
            trustlyView.addView(trustlyOAuthView)
            val transport = resultMsg.obj as WebViewTransport
            transport.webView = trustlyOAuthView.getWebView()
            resultMsg.sendToTarget()
            return true
        } else {
            val url = result.extra
            onExternalUrl?.let { onExternalUrl ->
                url?.let {
                    val params = mapOf("url" to it)
                    onExternalUrl.handle(trustlyView, params)
                }
            } ?: url?.let {
                TrustlyCustomTabsManager.openCustomTabsIntent(context, it)
            }
            return false
        }
    }

}