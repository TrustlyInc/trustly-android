package net.trustly.android.sdk.views.clients;

import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import net.trustly.android.sdk.views.TrustlyView;

/**
 * Chrome client for Trustly View
 */
public class TrustlyWebViewChromeClient extends WebChromeClient {

    private final TrustlyView trustlyView;

    public TrustlyWebViewChromeClient(TrustlyView trustlyView) {
        this.trustlyView = trustlyView;
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return trustlyView.handleWebChromeClientOnCreateWindow(view, resultMsg);
    }

}