package net.trustly.android.sdk.views;

import static net.trustly.android.sdk.util.TrustlyConstants.EVENT;
import static net.trustly.android.sdk.util.TrustlyConstants.EVENT_PAGE;
import static net.trustly.android.sdk.util.TrustlyConstants.EVENT_TYPE;
import static net.trustly.android.sdk.util.TrustlyConstants.PAYMENT_PROVIDER_ID;
import static net.trustly.android.sdk.util.TrustlyConstants.WIDGET;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import net.trustly.android.sdk.interfaces.Trustly;
import net.trustly.android.sdk.interfaces.TrustlyCallback;
import net.trustly.android.sdk.interfaces.TrustlyJsInterface;
import net.trustly.android.sdk.interfaces.TrustlyListener;
import net.trustly.android.sdk.util.UrlUtils;
import net.trustly.android.sdk.util.grp.GRPManager;
import net.trustly.android.sdk.views.clients.TrustlyWebViewChromeClient;
import net.trustly.android.sdk.views.clients.TrustlyWebViewClient;
import net.trustly.android.sdk.views.components.TrustlyLightbox;
import net.trustly.android.sdk.views.components.TrustlyWidget;
import net.trustly.android.sdk.views.oauth.TrustlyOAuthView;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;

/**
 * TrustlyView is a view class that implements the Trustly SDK interface
 */
public class TrustlyView extends LinearLayout implements Trustly {

    private static final String TAG = "TrustlyView";

    private static boolean isLocalEnvironment = false;

    public enum Status {
        START,
        WIDGET_LOADING,
        WIDGET_LOADED,
        PANEL_LOADING,
        PANEL_LOADED
    }

    private Status status = Status.START;

    private WebView webView;

    private int grp = -1;

    private Map<String, String> data;
    TrustlyCallback<Trustly, Map<String, String>> onReturn;
    TrustlyCallback<Trustly, Map<String, String>> onCancel;
    TrustlyCallback<Trustly, Map<String, String>> onWidgetBankSelected;
    TrustlyCallback<Trustly, Map<String, String>> onExternalUrl;

    private TrustlyListener trustlyListener = null;

    private String returnURL = "msg://return";
    private String cancelURL = "msg://cancel";

    /**
     * {@inheritDoc}
     *
     * @param context Interface to global information about an application environment.
     */
    public TrustlyView(Context context) {
        this(context, null);
    }

    /**
     * {@inheritDoc}
     *
     * @param context Interface to global information about an application environment.
     * @param attrs   A collection of attributes, as found associated with a tag in an XML document.
     */
    public TrustlyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * {@inheritDoc}
     *
     * @param context      Interface to global information about an application environment.
     * @param attrs        A collection of attributes, as found associated with a tag in an XML document.
     * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that
     *                     supplies defaults values for the TypedArray. Can be 0 to not look for defaults.
     */
    public TrustlyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initGrp(context);
        initWebView(context);

        setWebViewChromeClient();
        setWebViewClient();

        addView(webView);
    }

    private void initGrp(Context context) {
        grp = GRPManager.INSTANCE.getGRP(context);
        if (grp < 0) {
            grp = new SecureRandom().nextInt(100);
            GRPManager.INSTANCE.saveGRP(context, grp);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(Context context) {
        webView = new WebView(context);

        webView.setScrollContainer(false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        webView.setLayoutParams(params);

        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.addJavascriptInterface(new TrustlyJsInterface(this), "TrustlyNativeSDK");
    }

    public void resize(float width, float height) {
        new Handler(Looper.getMainLooper()).post(() -> {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            float widthPixels = applyDimension(width, displayMetrics);
            float heightPixels = 0.0F;
            if (height != heightPixels) {
                heightPixels = applyDimension(height, displayMetrics);
            } else {
                heightPixels = applyDimension(width * 1.75F, displayMetrics);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) widthPixels, (int) heightPixels);
            this.setLayoutParams(params);
        });
    }

    private float applyDimension(float value, DisplayMetrics displayMetrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics);
    }

    private void setWebViewChromeClient() {
        webView.setWebChromeClient(new TrustlyWebViewChromeClient(this));
    }

    private void setWebViewClient() {
        webView.setWebViewClient(new TrustlyWebViewClient(this));
    }

    public boolean handleWebChromeClientOnCreateWindow(WebView view, Message resultMsg) {
        WebView.HitTestResult result = view.getHitTestResult();
        if (result.getType() == 0) {
            //window.open
            final TrustlyOAuthView trustlyOAuthView = new TrustlyOAuthView(view.getContext());
            this.addView(trustlyOAuthView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(trustlyOAuthView.getWebView());
            resultMsg.sendToTarget();
            return true;
        } else {
            String url = result.getExtra();
            if (onExternalUrl != null) {
                Map<String, String> params = new HashMap<>();
                params.put("url", url);
                onExternalUrl.handle(this, params);
            } else {
                TrustlyCustomTabsManager.INSTANCE.openCustomTabsIntent(view.getContext(), url);
            }
            return false;
        }
    }

    public void handleWebViewClientOnReceivedError(TrustlyView trustlyView, String failingUrl) {
        try {
            boolean isAssetFile = failingUrl.matches("([^\\\\s]+(\\\\.(?i)(jpg|jpeg|svg|png|css|gif|webp))$)");
            if (!isLocalEnvironment() && onCancel != null && !isAssetFile) {
                onCancel.handle(trustlyView, new HashMap<>());
            }
        } catch (Exception e) {
            onCancel.handle(trustlyView, new HashMap<>());
            showErrorMessage(e);
        }
    }

    public void handleWebViewClientOnPageFinished(WebView view, TrustlyView trustlyView) {
        webView.loadUrl("javascript:TrustlyNativeSDK.resize(document.body.scrollWidth, document.body.scrollHeight)");

        if (status.equals(Status.PANEL_LOADING)) {
            status = Status.PANEL_LOADED;
        } else if (status.equals(Status.WIDGET_LOADING)) {
            status = Status.WIDGET_LOADED;
            notifyWidgetLoaded();
        }

        String title = view.getTitle();
        if (title != null) {
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(title);
            while (m.find()) {
                long n = Long.parseLong(m.group()) / 100;
                if (onCancel != null && (n == 4 || n == 5)) {
                    onCancel.handle(trustlyView, new HashMap<>());
                }
            }
        }
    }

    public boolean handleWebViewClientShouldOverrideUrlLoading(TrustlyView trustlyView, String url) {
        if (url.startsWith(returnURL) || url.startsWith(cancelURL)) {
            if (url.startsWith(returnURL) && onReturn != null) {
                onReturn.handle(trustlyView, UrlUtils.INSTANCE.getQueryParametersFromUrl(url));
            } else if (onCancel != null) {
                onCancel.handle(trustlyView, UrlUtils.INSTANCE.getQueryParametersFromUrl(url));
            }
            notifyClose();
            return true;
        } else if (url.startsWith("msg://push?")) {
            String[] params = url.split("\\|");
            if (params[0].contains("PayWithMyBank.createTransaction")) {
                data.put(PAYMENT_PROVIDER_ID, params.length > 1 ? params[1] : "");
                if (onWidgetBankSelected != null) {
                    onWidgetBankSelected.handle(trustlyView, data);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Trustly establish(Map<String, String> establishData) {
        data = establishData;
        TrustlyLightbox trustlyLightbox = new TrustlyLightbox(getContext(), webView, returnURL, cancelURL, statusChanged -> {
            status = statusChanged;
            return Unit.INSTANCE;
        }, () -> {
            notifyOpen();
            return Unit.INSTANCE;
        });
        trustlyLightbox.updateEstablishData(establishData, grp);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Trustly selectBankWidget(Map<String, String> establishData) {
        data = establishData;
        TrustlyWidget trustlyWidget = new TrustlyWidget(getContext(), webView, status, statusChanged -> {
            status = statusChanged;
            return Unit.INSTANCE;
        }, () -> {
            notifyWidgetLoading();
            return Unit.INSTANCE;
        });
        trustlyWidget.updateEstablishData(establishData, grp);
        return this;
    }

    @NonNull
    @Override
    public Trustly hybrid(String url, String returnURL, String cancelURL) {
        this.returnURL = returnURL;
        this.cancelURL = cancelURL;
        webView.loadUrl(url);
        return this;
    }

    @NonNull
    @Override
    public Trustly setListener(TrustlyListener trustlyListener) {
        this.trustlyListener = trustlyListener;

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Trustly onReturn(TrustlyCallback<Trustly, Map<String, String>> handler) {
        this.onReturn = handler;
        return this;
    }

    @NonNull
    @Override
    public Trustly destroy() {
        this.webView.destroy();
        return this;
    }

    @Override
    public void proceedToChooseAccount() {
        this.webView.loadUrl("javascript:Paywithmybank.proceedToChooseAccount();");
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Trustly onBankSelected(TrustlyCallback<Trustly, Map<String, String>> handler) {
        this.onWidgetBankSelected = handler;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Trustly onCancel(TrustlyCallback<Trustly, Map<String, String>> handler) {
        this.onCancel = handler;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Trustly onExternalUrl(TrustlyCallback<Trustly, Map<String, String>> handler) {
        this.onExternalUrl = handler;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void notifyListener(String eventName, Map<String, String> eventDetails) {
        if (this.trustlyListener != null) {
            this.trustlyListener.onChange(eventName, new HashMap<>(eventDetails));
        }
    }

    private void notifyOpen() {
        notifyListener("open", new HashMap<>());
    }

    private void notifyClose() {
        notifyListener("close", new HashMap<>());
    }

    private void notifyWidgetLoading() {
        HashMap<String, String> eventDetails = new HashMap<>();
        eventDetails.put(EVENT_PAGE, WIDGET);
        eventDetails.put(EVENT_TYPE, "loading");
        notifyListener(EVENT, eventDetails);
    }

    private void notifyWidgetLoaded() {
        HashMap<String, String> eventDetails = new HashMap<>();
        eventDetails.put(EVENT_PAGE, WIDGET);
        eventDetails.put(EVENT_TYPE, "load");
        notifyListener(EVENT, eventDetails);
    }

    private void showErrorMessage(Exception e) {
        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
    }

    public static boolean isLocalEnvironment() {
        return isLocalEnvironment;
    }

    public static void setIsLocalEnvironment(boolean isLocal) {
        isLocalEnvironment = isLocal;
    }

}