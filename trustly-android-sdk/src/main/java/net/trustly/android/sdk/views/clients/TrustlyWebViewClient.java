package net.trustly.android.sdk.views.clients;

import android.os.Build;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import net.trustly.android.sdk.views.TrustlyView;

public class TrustlyWebViewClient extends WebViewClient {

    private final TrustlyView trustlyView;

    public TrustlyWebViewClient(TrustlyView trustlyView) {
        this.trustlyView = trustlyView;
    }

    /**
     * @param view The WebView that is initiating the callback.
     * @param url The URL to be loaded.
     * @return {@code true} to cancel the current load, otherwise return {@code false}.
     * @deprecated
     */
    @Deprecated(since = "Build.VERSION_CODES.N")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return trustlyView.handleWebViewClientShouldOverrideUrlLoading(trustlyView, url);
    }

    /**
     * @param view The WebView that is initiating the callback.
     * @param request Object containing the details of the request.
     * @return {@code true} to cancel the current load, otherwise return {@code false}.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        return this.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        trustlyView.handleWebViewClientOnPageFinished(view, trustlyView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        String url = request.getUrl().toString();
        this.onReceivedError(view, 0, "", url);
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        trustlyView.handleWebViewClientOnReceivedError(trustlyView, failingUrl);
    }

}