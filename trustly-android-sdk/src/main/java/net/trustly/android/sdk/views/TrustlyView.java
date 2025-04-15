package net.trustly.android.sdk.views;

import static net.trustly.android.sdk.views.TrustlyConstants.CANCEL_URL;
import static net.trustly.android.sdk.views.TrustlyConstants.CID;
import static net.trustly.android.sdk.views.TrustlyConstants.CUSTOMER_ADDRESS_COUNTRY;
import static net.trustly.android.sdk.views.TrustlyConstants.CUSTOMER_ADDRESS_STATE;
import static net.trustly.android.sdk.views.TrustlyConstants.DEVICE_TYPE;
import static net.trustly.android.sdk.views.TrustlyConstants.ENV;
import static net.trustly.android.sdk.views.TrustlyConstants.ENV_DYNAMIC;
import static net.trustly.android.sdk.views.TrustlyConstants.ENV_LOCAL;
import static net.trustly.android.sdk.views.TrustlyConstants.ENV_PROD;
import static net.trustly.android.sdk.views.TrustlyConstants.ENV_PRODUCTION;
import static net.trustly.android.sdk.views.TrustlyConstants.EVENT;
import static net.trustly.android.sdk.views.TrustlyConstants.EVENT_PAGE;
import static net.trustly.android.sdk.views.TrustlyConstants.EVENT_TYPE;
import static net.trustly.android.sdk.views.TrustlyConstants.FUNCTION_INDEX;
import static net.trustly.android.sdk.views.TrustlyConstants.FUNCTION_MOBILE;
import static net.trustly.android.sdk.views.TrustlyConstants.METADATA_CID;
import static net.trustly.android.sdk.views.TrustlyConstants.PAYMENT_PROVIDER_ID;
import static net.trustly.android.sdk.views.TrustlyConstants.PAYMENT_TYPE;
import static net.trustly.android.sdk.views.TrustlyConstants.RETURN_URL;
import static net.trustly.android.sdk.views.TrustlyConstants.SESSION_CID;
import static net.trustly.android.sdk.views.TrustlyConstants.WIDGET;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import net.trustly.android.sdk.BuildConfig;
import net.trustly.android.sdk.TrustlyJsInterface;
import net.trustly.android.sdk.data.APIMethod;
import net.trustly.android.sdk.data.APIRequest;
import net.trustly.android.sdk.data.RetrofitInstance;
import net.trustly.android.sdk.data.Settings;
import net.trustly.android.sdk.data.StrategySetting;
import net.trustly.android.sdk.interfaces.Trustly;
import net.trustly.android.sdk.interfaces.TrustlyCallback;
import net.trustly.android.sdk.interfaces.TrustlyListener;
import net.trustly.android.sdk.util.EstablishDataUtils;
import net.trustly.android.sdk.util.CustomTabsManager;
import net.trustly.android.sdk.util.UrlUtils;
import net.trustly.android.sdk.util.api.APIRequestManager;
import net.trustly.android.sdk.util.cid.CidManager;
import net.trustly.android.sdk.views.clients.TrustlyWebViewChromeClient;
import net.trustly.android.sdk.views.clients.TrustlyWebViewClient;
import net.trustly.android.sdk.views.oauth.TrustlyOAuthView;

import java.nio.charset.StandardCharsets;
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

    private static final String PROTOCOL = "https://";
    private static final String LOCAL_PROTOCOL = "http://";
    private static final String DOMAIN = "paywithmybank.com";
    private static final String SDK_VERSION = BuildConfig.SDK_VERSION;

    private static boolean isLocalEnvironment = false;

    enum Status {
        START,
        WIDGET_LOADING,
        WIDGET_LOADED,
        PANEL_LOADING,
        PANEL_LOADED
    }

    private Status status = Status.START;

    private WebView webView;

    private int grp = -1;

    private final String env;

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
        this(context, (String) null);
    }

    /**
     * {@inheritDoc}
     *
     * @param context Interface to global information about an application environment.
     * @param env     Set if environment different than production (such as "sandbox")
     */
    public TrustlyView(Context context, String env) {
        this(context, null, 0, env);
    }

    /**
     * {@inheritDoc}
     *
     * @param context Interface to global information about an application environment.
     * @param attrs   A collection of attributes, as found associated with a tag in an XML document.
     */
    public TrustlyView(Context context, AttributeSet attrs) {
        this(context, attrs, null);
    }

    /**
     * {@inheritDoc}
     *
     * @param context Interface to global information about an application environment.
     * @param attrs   A collection of attributes, as found associated with a tag in an XML document.
     * @param env     Set if environment different than production (such as "sandbox")
     */
    public TrustlyView(Context context, AttributeSet attrs, String env) {
        this(context, attrs, 0, env);
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
        this(context, attrs, defStyleAttr, null);
    }

    /**
     * {@inheritDoc}
     *
     * @param context      Interface to global information about an application environment.
     * @param attrs        A collection of attributes, as found associated with a tag in an XML document.
     * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that
     *                     supplies defaults values for the TypedArray. Can be 0 to not look for defaults.
     * @param env          Set if environment different than production (such as "sandbox")
     */
    public TrustlyView(Context context, AttributeSet attrs, int defStyleAttr, String env) {
        super(context, attrs, defStyleAttr);
        if (env != null) env = env.toLowerCase();

        this.env = env;

        initGrp(context);
        initWebView(context);

        setWebViewChromeClient();
        setWebViewClient();

        addView(webView);
    }

    private void initGrp(Context context) {
        try {
            if (grp < 0) {
                SharedPreferences pref = context.getSharedPreferences("PayWithMyBank", 0);
                if (pref != null) {
                    grp = pref.getInt(TrustlyConstants.GRP, -1);
                    if (grp < 0) {
                        grp = new SecureRandom().nextInt(100);
                        pref.edit().putInt(TrustlyConstants.GRP, grp).apply();
                    }
                }
            }
        } catch (Exception e) {
            grp = 1;
            showErrorMessage(e);
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
                CustomTabsManager.openCustomTabsIntent(view.getContext(), url);
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
                onReturn.handle(trustlyView, UrlUtils.getQueryParametersFromUrl(url));
            } else if (onCancel != null) {
                onCancel.handle(trustlyView, UrlUtils.getQueryParametersFromUrl(url));
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
    @Override
    public Trustly establish(Map<String, String> establishData) {
        try {
            EstablishDataUtils.INSTANCE.validateEstablishDataRequiredFields(establishData);

            status = Status.PANEL_LOADING;
            CidManager.generateCid(getContext());

            data = new HashMap<>(establishData);

            String deviceType = establishData.get(DEVICE_TYPE);
            if (deviceType != null) {
                deviceType = deviceType + ":android:native";
            } else {
                deviceType = "mobile:android:native";
            }
            data.put(DEVICE_TYPE, deviceType);

            String lang = establishData.get("metadata.lang");
            if (lang != null) data.put("lang", lang);

            data.put("metadata.sdkAndroidVersion", SDK_VERSION);
            data.put(RETURN_URL, returnURL);
            data.put(CANCEL_URL, cancelURL);
            data.put(TrustlyConstants.GRP, Integer.toString(grp));

            if (data.containsKey(PAYMENT_PROVIDER_ID)) {
                data.put("widgetLoaded", "true");
            }

            Map<String, String> sessionCidValues = CidManager.getOrCreateSessionCid(getContext());
            data.put(SESSION_CID, sessionCidValues.get(CidManager.SESSION_CID_PARAM));
            data.put(METADATA_CID, sessionCidValues.get(CidManager.CID_PARAM));

            notifyOpen();

            if (ENV_LOCAL.equals(data.get(ENV))) {
                WebView.setWebContentsDebuggingEnabled(true);
                setIsLocalEnvironment(true);
            }

            if (APIRequestManager.INSTANCE.validateAPIRequest(getContext())) {
                Settings settings = APIRequestManager.INSTANCE.getAPIRequestSettings(getContext());
                openWebViewOrCustomTabs(settings, data);
            } else {
                APIMethod apiInterface = RetrofitInstance.INSTANCE.getInstance(getDomain(FUNCTION_MOBILE, establishData)).create(APIMethod.class);
                APIRequest apiRequest = new APIRequest(apiInterface, settings -> {
                    APIRequestManager.INSTANCE.saveAPIRequestSettings(getContext(), settings);
                    openWebViewOrCustomTabs(settings, data);
                    return Unit.INSTANCE;
                }, error -> {
                    openWebViewOrCustomTabs(new Settings(new StrategySetting("webview")), data);
                    return Unit.INSTANCE;
                });
                apiRequest.getSettingsData(getTokenByEncodedParameters(data));
            }
        } catch (Exception e) {
            showErrorMessage(e);
        }
        return this;
    }

    private void openWebViewOrCustomTabs(Settings settings, Map<String, String> establishData) {
        if (settings.getSettings().getIntegrationStrategy().equals("webview")) {
            data.put("metadata.integrationContext", "InAppBrowser");
            byte[] encodedParameters = UrlUtils.getParameterString(data).getBytes(StandardCharsets.UTF_8);
            webView.postUrl(getEndpointUrl(FUNCTION_INDEX, establishData), encodedParameters);
        } else {
            data.put(RETURN_URL, establishData.get("metadata.urlScheme"));
            data.put(CANCEL_URL, establishData.get("metadata.urlScheme"));
            CustomTabsManager.openCustomTabsIntent(getContext(), getEndpointUrl(FUNCTION_MOBILE, establishData) + "?token=" + getTokenByEncodedParameters(data));
        }
    }

    private String getTokenByEncodedParameters(Map<String, String> data) {
        String jsonFromParameters = UrlUtils.getJsonFromParameters(data);
        return UrlUtils.encodeStringToBase64(jsonFromParameters).replace("\n", "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Trustly selectBankWidget(Map<String, String> establishData) {
        try {
            EstablishDataUtils.INSTANCE.validateEstablishDataRequiredFields(establishData);

            data = new HashMap<>(establishData);
            String deviceType = establishData.get(DEVICE_TYPE);
            if (deviceType != null) {
                deviceType = deviceType + ":android:hybrid";
            } else {
                deviceType = "mobile:android:hybrid";
            }
            data.put(DEVICE_TYPE, deviceType);

            String lang = establishData.get("metadata.lang");
            if (lang != null) data.put("lang", lang);
            data.put(TrustlyConstants.GRP, Integer.toString(grp));
            data.put("dynamicWidget", "true");

            if (establishData.get(CUSTOMER_ADDRESS_COUNTRY) != null) {
                data.put(CUSTOMER_ADDRESS_COUNTRY, establishData.get(CUSTOMER_ADDRESS_COUNTRY));
            } else {
                data.put(CUSTOMER_ADDRESS_COUNTRY, "US");
            }

            if (establishData.get(CUSTOMER_ADDRESS_COUNTRY) == null || "us".equalsIgnoreCase(establishData.get(CUSTOMER_ADDRESS_COUNTRY))) {
                data.put(CUSTOMER_ADDRESS_STATE, establishData.get(CUSTOMER_ADDRESS_STATE));
            }

            Map<String, String> sessionCidValues = CidManager.getOrCreateSessionCid(getContext());
            data.put(SESSION_CID, sessionCidValues.get(CidManager.SESSION_CID_PARAM));
            data.put(CID, sessionCidValues.get(CidManager.CID_PARAM));

            if (status != Status.WIDGET_LOADED) {
                status = Status.WIDGET_LOADING;
                notifyWidgetLoading();

                String parameterString = UrlUtils.getParameterString(data);
                String url = getEndpointUrl(WIDGET, establishData) + "&" + parameterString + "#" + UrlUtils.encodeStringToBase64(parameterString);
                webView.loadUrl(url);
                webView.setBackgroundColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
            showErrorMessage(e);
        }
        return this;
    }

    @Override
    public Trustly hybrid(String url, String returnURL, String cancelURL) {
        this.returnURL = returnURL;
        this.cancelURL = cancelURL;
        webView.loadUrl(url);
        return this;
    }

    @Override
    public Trustly setListener(TrustlyListener trustlyListener) {
        this.trustlyListener = trustlyListener;

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Trustly onReturn(TrustlyCallback<Trustly, Map<String, String>> handler) {
        this.onReturn = handler;
        return this;
    }

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
    @Override
    public Trustly onBankSelected(TrustlyCallback<Trustly, Map<String, String>> handler) {
        this.onWidgetBankSelected = handler;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Trustly onCancel(TrustlyCallback<Trustly, Map<String, String>> handler) {
        this.onCancel = handler;
        return this;
    }

    /**
     * {@inheritDoc}
     */
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

    private String getDomain(String function, Map<String, String> establishData) {
        String envHost = establishData.get("envHost");
        String environment = establishData.get(ENV) != null ? Objects.requireNonNull(establishData.get(ENV)).toLowerCase() : env;
        if (environment == null) {
            return PROTOCOL + DOMAIN;
        }
        switch (environment) {
            case ENV_DYNAMIC: {
                String host = envHost != null ? envHost : "";
                return PROTOCOL + host + ".int.trustly.one";
            }
            case ENV_LOCAL: {
                String host = (envHost != null && !envHost.equals("localhost")) ? envHost : BuildConfig.LOCAL_IP;
                String port;
                if (FUNCTION_MOBILE.equals(function)) {
                    port = ":10000";
                } else {
                    port = ":8000";
                }
                return LOCAL_PROTOCOL + host + port;
            }
            case ENV_PROD:
            case ENV_PRODUCTION:
                environment = "";
                break;
            default:
                environment = environment + ".";
                break;
        }
        return PROTOCOL + environment + DOMAIN;
    }

    /**
     * {@inheritDoc}
     */
    protected String getEndpointUrl(String function, Map<String, String> establishData) {
        String domain = getDomain(function, establishData);
        if (FUNCTION_MOBILE.equals(function)) {
             return domain + "/frontend/mobile/establish";
        }
        if (FUNCTION_INDEX.equals(function) &&
                !"Verification".equals(establishData.get(PAYMENT_TYPE)) &&
                establishData.get(PAYMENT_PROVIDER_ID) != null) {
            function = "selectBank";
        }
        return domain + "/start/selectBank/" + function + "?v=" + SDK_VERSION + "-android-sdk";
    }

    private void notifyOpen() {
        notifyListener("open", null);
    }

    private void notifyClose() {
        notifyListener("close", null);
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
        Log.e("TrustlyView", Objects.requireNonNull(e.getMessage()));
    }

    public static boolean isLocalEnvironment() {
        return isLocalEnvironment;
    }

    public static void setIsLocalEnvironment(boolean isLocal) {
        isLocalEnvironment = isLocal;
    }

}