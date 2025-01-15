package net.trustly.android.sdk.views.oauth

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.RelativeLayout

/**
 * View for Trustly OAuth login
 * @param context      Interface to global information about an application environment.
 * @param attrs        A collection of attributes, as found associated with a tag in an XML document.
 *
 */
@SuppressLint("SetJavaScriptEnabled")
class TrustlyOAuthView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private var webView: WebView = WebView(context)

    init {
        webView.settings.apply {
            setSupportMultipleWindows(true)
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }
        webView.layoutParams =
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        webView.webViewClient = TrustlyOAuthClient()
    }

    /**
     * @return The WebView which contains the OAuth login page.
     */
    fun getWebView() = webView

}