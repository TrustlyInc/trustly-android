package net.trustly.android.sdk.views.oauth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * View for Trustly OAuth login
 */
public class TrustlyOAuthView extends LinearLayout {

    private final WebView webView;

    /**
     * @param context Interface to global information about an application environment.
     */
    public TrustlyOAuthView(Context context) {
        this(context, null);
    }

    /**
     * @param context Interface to global information about an application environment.
     * @param attrs   A collection of attributes, as found associated with a tag in an XML document.
     */
    public TrustlyOAuthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context      Interface to global information about an application environment.
     * @param attrs        A collection of attributes, as found associated with a tag in an XML document.
     * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that
     *                     supplies defaults values for the TypedArray. Can be 0 to not look for defaults.
     */
    @SuppressLint("SetJavaScriptEnabled")
    public TrustlyOAuthView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        webView = new WebView(context);
        WebSettings settings = webView.getSettings();
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView.setWebViewClient(new TrustlyOAuthClient());
    }

    /**
     * @return The WebView which contains the OAuth login page.
     */
    public WebView getWebView() {
        return webView;
    }

}
