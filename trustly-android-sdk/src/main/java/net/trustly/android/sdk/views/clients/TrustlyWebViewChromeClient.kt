package net.trustly.android.sdk.views.clients

import android.os.Message
import android.webkit.WebChromeClient
import android.webkit.WebView
import net.trustly.android.sdk.views.TrustlyView

/**
 * Chrome client for Trustly View
 */
class TrustlyWebViewChromeClient(private val trustlyView: TrustlyView) : WebChromeClient() {

    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ) = trustlyView.handleWebChromeClientOnCreateWindow(view, resultMsg);

}