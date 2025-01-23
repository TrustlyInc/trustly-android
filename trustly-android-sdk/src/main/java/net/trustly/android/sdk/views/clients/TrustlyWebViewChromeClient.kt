package net.trustly.android.sdk.views.clients

import android.content.Context
import android.os.Message
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
import net.trustly.android.sdk.views.TrustlyView
import net.trustly.android.sdk.views.events.TrustlyEvents
import net.trustly.android.sdk.views.oauth.TrustlyOAuthView

/**
 * Chrome client for Trustly View
 */
class TrustlyWebViewChromeClient(
    private val context: Context,
    private val trustlyView: TrustlyView,
    private val trustlyEvents: TrustlyEvents,
) : WebChromeClient() {

    override fun onCreateWindow(
        view: WebView,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message
    ) = handleWebChromeClientOnCreateWindow(view, resultMsg)

    private fun handleWebChromeClientOnCreateWindow(view: WebView, resultMsg: Message): Boolean {
        val result = view.hitTestResult
        return if (result.type == 0) {
            //window.open
            val trustlyOAuthView = TrustlyOAuthView(context)
            trustlyView.addView(trustlyOAuthView)
            val transport = resultMsg.obj as WebViewTransport
            transport.webView = trustlyOAuthView.getWebView()
            resultMsg.sendToTarget()
            true
        } else {
            val url = result.extra
            url?.let {
                val params = mapOf("url" to url)
                trustlyEvents.handleOnExternalUrl(trustlyView, params)
            }
            false
        }
    }

}