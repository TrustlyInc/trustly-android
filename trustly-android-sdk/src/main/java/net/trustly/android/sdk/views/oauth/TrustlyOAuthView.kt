package net.trustly.android.sdk.views.oauth

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
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

    var webView: WebView = WebView(context)

    init {
        with(webView) {
            settings.apply {
                setSupportMultipleWindows(true)
                javaScriptEnabled = true
                domStorageEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
            }
            layoutParams =
                RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )
            webViewClient = TrustlyOAuthClient()
        }
    }

}