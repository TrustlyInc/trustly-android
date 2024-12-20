package net.trustly.android.sdk.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

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
import net.trustly.android.sdk.util.CustomTabsManager;
import net.trustly.android.sdk.util.UrlUtils;
import net.trustly.android.sdk.util.api.APIRequestManager;
import net.trustly.android.sdk.util.cid.CidManager;
import net.trustly.android.sdk.views.oauth.TrustlyOAuthView;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;

/**
 * TrustlyView is a view class that implements the Trustly SDK interface
 */
public class TrustlyView extends LinearLayout implements Trustly {

    static String PROTOCOL = "https://";
    static String DOMAIN = "paywithmybank.com";
    static String version = BuildConfig.SDK_VERSION;

    private static final String DYNAMIC = "dynamic";
    private static final String INDEX = "index";
    private static final String LOCAL = "local";
    private static final String MOBILE = "mobile";
    private static final String PAYMENT_PROVIDER_ID = "paymentProviderId";
    private static final String PAYMENT_TYPE = "paymentType";

    private static boolean isLocalEnvironment = false;

    enum Status {
        START,
        WIDGET_LOADING,
        WIDGET_LOADED,
        PANEL_LOADING,
        PANEL_LOADED
    }

    private Status status = Status.START;

    private final WebView webView;

    private static int grp = -1;

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
    @SuppressLint("SetJavaScriptEnabled")
    public TrustlyView(Context context, AttributeSet attrs, int defStyleAttr, String env) {
        super(context, attrs, defStyleAttr);
        if (env != null) env = env.toLowerCase();

        this.env = env;

        try {
            if (grp < 0) {
                SharedPreferences pref = context.getSharedPreferences("PayWithMyBank", 0);
                if (pref != null) {
                    grp = pref.getInt("grp", -1);
                    if (grp < 0) {
                        grp = new SecureRandom().nextInt(100);
                        pref.edit().putInt("grp", grp).commit();
                    }
                }
            }
        } catch (Exception e) {
            grp = 1;
        }

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

        final TrustlyView self = this;

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView.HitTestResult result = view.getHitTestResult();
                String url = result.getExtra();

                if (result.getType() == 0) {
                    //window.open
                    final TrustlyOAuthView trustlyOAuthView = new TrustlyOAuthView(view.getContext());
                    self.addView(trustlyOAuthView);
                    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                    transport.setWebView(trustlyOAuthView.getWebView());
                    resultMsg.sendToTarget();
                    return true;
                } else {
                    if (self.onExternalUrl != null) {
                        Map<String, String> params = new HashMap<>();
                        params.put("url", url);
                        self.onExternalUrl.handle(self, params);
                    } else {
                        CustomTabsManager.openCustomTabsIntent(view.getContext(), url);
                    }
                    return false;
                }

            }

        });

        webView.setWebViewClient(new WebViewClient() {

            @Deprecated
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                boolean isNotAssetFile = true;
                try {
                    isNotAssetFile = !failingUrl.matches(".*\\.svg\\.png\\.jpg\\.jpeg\\.css\\.gif\\.webp");
                } catch (Exception e) {
                    onCancel.handle(self, new HashMap<>());
                }

                if (!isLocalEnvironment() && onCancel != null && isNotAssetFile) {
                    onCancel.handle(self, new HashMap<>());
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                String url = request.getUrl().toString();
                this.onReceivedError(view, 0, "", url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:TrustlyNativeSDK.resize(document.body.scrollWidth, document.body.scrollHeight)");

                switch (status) {
                    case PANEL_LOADING:
                        status = Status.PANEL_LOADED;
                        break;
                    case WIDGET_LOADING:
                        status = Status.WIDGET_LOADED;
                        notifyWidgetLoaded();
                        break;
                }

                String title = view.getTitle();

                if (title != null) {
                    Pattern p = Pattern.compile("[0-9]+");
                    Matcher m = p.matcher(title);
                    while (m.find()) {
                        long n = Long.parseLong(m.group()) / 100;
                        if (onCancel != null && (n == 4 || n == 5)) {
                            onCancel.handle(self, new HashMap<>());
                        }
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    if ((url).startsWith(returnURL)) {
                        if (onReturn != null) {
                            onReturn.handle(self, UrlUtils.getQueryParametersFromUrl(url));
                            notifyClose();
                            return true;
                        }
                    } else if ((url).startsWith(cancelURL)) {
                        if (onCancel != null) {
                            onCancel.handle(self, UrlUtils.getQueryParametersFromUrl(url));
                            notifyClose();
                            return true;
                        }
                    } else if ((url).startsWith("msg://")) {
                        if ((url).startsWith("msg://push?")) {
                            String[] params = url.substring(11).split("\\|");
                            switch (params[0]) {
                                case "PayWithMyBank.createTransaction":
                                    if (params.length > 1) {
                                        data.put(PAYMENT_PROVIDER_ID, params[1]);
                                    } else {
                                        data.put(PAYMENT_PROVIDER_ID, "");
                                    }

                                    if (onWidgetBankSelected != null) {
                                        onWidgetBankSelected.handle(self, data);
                                    }
                                    break;
                            }
                        }

                        return true;
                    }
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        addView(webView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Trustly establish(Map<String, String> establishData) {
        try {
            status = Status.PANEL_LOADING;
            CidManager.generateCid(getContext());

            data = new HashMap<>(establishData);
            String url = getEndpointUrl("index", establishData);

            String deviceType = establishData.get("deviceType");

            if (deviceType != null) {
                deviceType = deviceType + ":android:native";
            } else {
                deviceType = "mobile:android:native";
            }

            String lang = establishData.get("metadata.lang");
            if (lang != null) data.put("lang", lang);

            data.put("metadata.sdkAndroidVersion", version);
            data.put("deviceType", deviceType);
            data.put("returnUrl", returnURL);
            data.put("cancelUrl", cancelURL);
            data.put("grp", Integer.toString(grp));

            if (data.containsKey(PAYMENT_PROVIDER_ID)) {
                data.put("widgetLoaded", "true");
            }

            Map<String, String> sessionCidValues = CidManager.getOrCreateSessionCid(getContext());
            data.put("sessionCid", sessionCidValues.get(CidManager.SESSION_CID_PARAM));
            data.put("metadata.cid", sessionCidValues.get(CidManager.CID_PARAM));

            notifyOpen();

            if (LOCAL.equals(data.get("env"))) {
                webView.setWebContentsDebuggingEnabled(true);
                setIsLocalEnvironment(true);
            }

            if (APIRequestManager.INSTANCE.validateAPIRequest(getContext())) {
                Settings settings = APIRequestManager.INSTANCE.getAPIRequestSettings(getContext());
                openWebViewOrCustomTabs(settings, data);
            } else {
                APIMethod apiInterface = RetrofitInstance.INSTANCE.getInstance(getDomain(MOBILE, establishData)).create(APIMethod.class);
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
            Log.e("TrustlyView", e.getMessage());
        }
        return this;
    }

    private void openWebViewOrCustomTabs(Settings settings, Map<String, String> establishData) {
        if (settings.getSettings().getIntegrationStrategy().equals("webview")) {
            data.put("metadata.integrationContext", "InAppBrowser");
            byte[] encodedParameters = UrlUtils.getParameterString(data).getBytes(StandardCharsets.UTF_8);
            webView.postUrl(getEndpointUrl(INDEX, establishData), encodedParameters);
        } else {
            data.put("returnUrl", establishData.get("metadata.urlScheme"));
            data.put("cancelUrl", establishData.get("metadata.urlScheme"));
            CustomTabsManager.openCustomTabsIntent(getContext(), getEndpointUrl(MOBILE, establishData) + "?token=" + getTokenByEncodedParameters(data));
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
            data = new HashMap<>(establishData);
            String deviceType = establishData.get("deviceType");

            if (deviceType != null) {
                deviceType = deviceType + ":android:hybrid";
            } else {
                deviceType = "mobile:android:hybrid";
            }

            String lang = establishData.get("metadata.lang");

            HashMap<String, String> d = new HashMap<>();
            d.put("accessId", establishData.get("accessId"));
            d.put("merchantId", establishData.get("merchantId"));
            d.put("paymentType", establishData.get(PAYMENT_TYPE));
            d.put("deviceType", deviceType);
            if (lang != null) d.put("lang", lang);
            d.put("grp", Integer.toString(grp));
            d.put("dynamicWidget", "true");

            if (establishData.get("customer.address.country") != null) {
                d.put("customer.address.country", establishData.get("customer.address.country"));
            } else {
                d.put("customer.address.country", "US");
            }

            if (establishData.get("customer.address.country") == null || "us".equals(establishData.get("customer.address.country").toLowerCase())) {
                d.put("customer.address.state", establishData.get("customer.address.state"));
            }

            Map<String, String> sessionCidValues = CidManager.getOrCreateSessionCid(getContext());
            d.put("sessionCid", sessionCidValues.get(CidManager.SESSION_CID_PARAM));
            d.put("cid", sessionCidValues.get(CidManager.CID_PARAM));

            Map<String, String> hash = new HashMap<>();

            hash.put("merchantReference", establishData.get("merchantReference"));
            hash.put("customer.externalId", establishData.get("customer.externalId"));

            if (status != Status.WIDGET_LOADED) {
                status = Status.WIDGET_LOADING;
                notifyWidgetLoading();

                String url = getEndpointUrl("widget", establishData) + "&" + UrlUtils.getParameterString(d) + "#" + UrlUtils.getParameterString(hash);
                webView.loadUrl(url);
                webView.setBackgroundColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
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
    public void notifyListener(String eventName, HashMap<String, String> eventDetails) {
        if (this.trustlyListener != null) {
            this.trustlyListener.onChange(eventName, eventDetails);
        }
    }

    private String getDomain(String function, Map<String, String> establishData) {
        String environment = establishData.get("env") != null ? establishData.get("env").toLowerCase() : env;
        String envHost = establishData.get("envHost");

        if (environment == null) {
            return PROTOCOL + DOMAIN;
        }

        switch (environment) {
            case DYNAMIC: {
                String host = envHost != null ? envHost : "";
                return "https://" + host + ".int.trustly.one";
            }
            case LOCAL: {
                String host = (envHost != null && !envHost.equals("localhost")) ? envHost : BuildConfig.LOCAL_IP;
                String port = "";
                String protocol = "http://";

                if (MOBILE.equals(function)) {
                    port = ":10000";
                } else {
                    port = ":8000";
                }

                return protocol + host + port;
            }
            case "prod":
            case "production":
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

        if (MOBILE.equals(function)) {
             return domain + "/frontend/mobile/establish";
        }

        if (INDEX.equals(function) &&
                !"Verification".equals(establishData.get(PAYMENT_TYPE)) &&
                establishData.get(PAYMENT_PROVIDER_ID) != null) {
            function = "selectBank";
        }

        return domain + "/start/selectBank/" + function + "?v=" + version + "-android-sdk";
    }

    private void notifyOpen() {
        notifyListener("open", null);
    }

    private void notifyClose() {
        notifyListener("close", null);
    }

    private void notifyWidgetLoading() {
        HashMap<String, String> eventDetails = new HashMap<>();
        eventDetails.put("page", "widget");
        eventDetails.put("type", "loading");

        notifyListener("event", eventDetails);
    }

    private void notifyWidgetLoaded() {
        HashMap<String, String> eventDetails = new HashMap<>();
        eventDetails.put("page", "widget");
        eventDetails.put("type", "load");

        notifyListener("event", eventDetails);
    }

    public static boolean isLocalEnvironment() {
        return isLocalEnvironment;
    }

    public static void setIsLocalEnvironment(boolean isLocal) {
        isLocalEnvironment = isLocal;
    }

    protected static void resetGrp() {
        grp = -1;
    }
}