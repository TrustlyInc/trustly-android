/*  ___________________________________________________________________________________________________________
 *
 *    TRUSTLY CONFIDENTIAL AND PROPRIETARY INFORMATION
 *  ___________________________________________________________________________________________________________
 *
 *      Copyright (c) 2012 - 2020 Trustly
 *      All Rights Reserved.
 *
 *   NOTICE:  All information contained herein is, and remains, the confidential and proprietary property of
 *   Trustly and its suppliers, if any. The intellectual and technical concepts contained herein are the
 *   confidential and proprietary property of Trustly and its suppliers and  may be covered by U.S. and
 *   Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of
 *   this information or reproduction of this material is strictly forbidden unless prior written permission is
 *   obtained from Trustly.
 *   ___________________________________________________________________________________________________________
 */
package net.trustly.android.sdk.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.browser.customtabs.CustomTabsIntent;

import net.trustly.android.sdk.TrustlyJsInterface;
import net.trustly.android.sdk.interfaces.Trustly;
import net.trustly.android.sdk.interfaces.TrustlyCallback;
import net.trustly.android.sdk.interfaces.TrustlyListener;
import net.trustly.android.sdk.util.UrlUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TrustlyView is a view class that implements the Trustly SDK interface
 */
public class TrustlyView extends FrameLayout implements Trustly {

    static String PROTOCOL = "https://";
    static String DOMAIN = "paywithmybank.com";
    static String version = "2.2.1";

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
        this(context, null, 0, null);
    }

    /**
     * {@inheritDoc}
     *
     * @param context Interface to global information about an application environment.
     * @param env     Set if environment different than production (such as "sandbox")
     */
    public TrustlyView(Context context, String env) {
        this(context, null, 0, null);
    }

    /**
     * {@inheritDoc}
     *
     * @param context Interface to global information about an application environment.
     * @param attrs   A collection of attributes, as found associated with a tag in an XML document.
     */
    public TrustlyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0, null);
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
                        grp = new Random().nextInt(100);
                        pref.edit().putInt("grp", grp).commit();
                    }
                }
            }
        } catch (Exception e) {
            grp = 1;
        }

        RelativeLayout.LayoutParams paramsPanel = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsPanel.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        setLayoutParams(paramsPanel);

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
                    transport.setWebView(trustlyOAuthView.webView);
                    resultMsg.sendToTarget();
                    return true;
                } else {
                    if (self.onExternalUrl != null) {
                        Map<String, String> params = new HashMap<>();
                        params.put("url", url);
                        self.onExternalUrl.handle(self, params);
                    } else {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(view.getContext(), Uri.parse(url));
                    }
                    return false;
                }

            }

        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                String url;
                boolean isNotAssetFile = true;

                try {
                    url = request.getUrl().toString();
                    isNotAssetFile = !url.matches(".*\\.svg\\.png\\.jpg\\.jpeg\\.css\\.gif\\.webp");
                } catch (Exception e) {
                    onCancel.handle(self, new HashMap<>());
                }

                if (onCancel != null && isNotAssetFile) {
                    onCancel.handle(self, new HashMap<>());
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
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
                        int n = Integer.parseInt(m.group()) / 100;
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
                                        data.put("paymentProviderId", params[1]);
                                    } else {
                                        data.put("paymentProviderId", "");
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
    public Trustly establish(Map<String, String> establishData) {
        status = Status.PANEL_LOADING;

        data = new HashMap<>(establishData);
        String url = getEndpointUrl("index", establishData);

        try {
            String deviceType = establishData.get("deviceType");

            if (deviceType != null) {
                deviceType = deviceType + ":android:native";
            } else {
                deviceType = "mobile:android:native";
            }

            String lang = establishData.get("metadata.lang");
            if (lang != null) data.put("lang", lang);

            String integrationContext = establishData.get("metadata.integrationContext");
            if (integrationContext == null || integrationContext.isEmpty()) {
                data.put("metadata.integrationContext", "InAppBrowser");
            }
            data.put("metadata.sdkAndroidVersion", version);
            data.put("deviceType", deviceType);
            data.put("returnUrl", returnURL);
            data.put("cancelUrl", cancelURL);
            data.put("grp", Integer.toString(grp));

            if (data.containsKey("paymentProviderId")) {
                data.put("widgetLoaded", "true");
            }

            notifyOpen();

            if ("local".equals(data.get("env"))) {
                webView.setWebContentsDebuggingEnabled(true);
            }

            webView.postUrl(url, UrlUtils.getParameterString(data).getBytes("UTF-8"));
        } catch (Exception e) {
        }
        return this;
    }

    public String getInAppBrowserLaunchURL(Map<String, String> establishData) {
        Map<String, String> data = new HashMap<>(establishData);
        String deviceType = establishData.get("deviceType");

        if (deviceType != null) {
            deviceType = deviceType + ":android:iab";
        } else {
            deviceType = "mobile:android:iab";
        }

        String lang = establishData.get("metadata.lang");
        if (lang != null) data.put("lang", lang);

        data.put("metadata.sdkAndroidVersion", version);
        data.put("deviceType", deviceType);
        data.put("grp", Integer.toString(grp));

        if (data.containsKey("paymentProviderId")) {
            data.put("widgetLoaded", "true");
        }

        return UrlUtils.getParameterString(data);
    }

    /**
     * {@inheritDoc}
     */
    public Trustly selectBankWidget(Map<String, String> establishData) {
        data = new HashMap<>(establishData);
        try {
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
            d.put("paymentType", establishData.get("paymentType"));
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
            Map<String, String> hash = new HashMap<>();

            hash.put("merchantReference", establishData.get("merchantReference"));
            hash.put("customer.externalId", establishData.get("customer.externalId"));

            if (status == Status.WIDGET_LOADED) {
                return this;
            }
            status = Status.WIDGET_LOADING;

            notifyWidgetLoading();

            String url = getEndpointUrl("widget", establishData) + "&" + UrlUtils.getParameterString(d) + "#" + UrlUtils.getParameterString(hash);
            webView.loadUrl(url);
            webView.setBackgroundColor(Color.TRANSPARENT);
        } catch (Exception e) {
        }
        return this;
    }

    public Trustly hybrid(String url, String returnURL, String cancelURL) {
        this.returnURL = returnURL;
        this.cancelURL = cancelURL;
        webView.loadUrl(url);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Trustly verify(Map<String, String> verifyData) {
        verifyData.put("paymentType", "Verification");
        return establish(verifyData);
    }

    @Override
    public Trustly setListener(TrustlyListener trustlyListener) {
        this.trustlyListener = trustlyListener;

        return this;
    }

    /**
     * {@inheritDoc}
     */
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
    public Trustly onBankSelected(TrustlyCallback<Trustly, Map<String, String>> handler) {
        this.onWidgetBankSelected = handler;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Trustly onCancel(TrustlyCallback<Trustly, Map<String, String>> handler) {
        this.onCancel = handler;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Trustly onExternalUrl(TrustlyCallback<Trustly, Map<String, String>> handler) {
        this.onExternalUrl = handler;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Trustly notifyListener(String eventName, HashMap<String, String> eventDetails) {
        if (this.trustlyListener == null) {
            return this;
        }

        this.trustlyListener.onChange(eventName, eventDetails);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    protected String getEndpointUrl(String function, Map<String, String> establishData) {

        String subDomain = establishData.get("env") != null
                ? establishData.get("env").toLowerCase()
                : env;

        String localUrl = establishData.get("localUrl");

        if (subDomain == null || "prod".equals(subDomain) || "production".equals(subDomain)) {
            subDomain = "";
        } else {
            subDomain = subDomain + ".";
        }

        if ("index".equals(function) &&
                !"Verification".equals(establishData.get("paymentType")) &&
                establishData.get("paymentProviderId") != null) {
            function = "selectBank";

        }

        if (localUrl != null && "local.".equals(subDomain)) {
            return "http://" + localUrl + "/start/selectBank/" + function + "?v=" + version + "-android-sdk";
        }

        return PROTOCOL + subDomain + DOMAIN + "/start/selectBank/" + function + "?v=" + version + "-android-sdk";
    }

    protected void notifyOpen() {
        notifyListener("open", null);
    }

    protected void notifyClose() {
        notifyListener("close", null);
    }

    protected void notifyWidgetLoading() {
        HashMap<String, String> eventDetails = new HashMap<>();
        eventDetails.put("page", "widget");
        eventDetails.put("type", "loading");

        notifyListener("event", eventDetails);
    }

    protected void notifyWidgetLoaded() {
        HashMap<String, String> eventDetails = new HashMap<>();
        eventDetails.put("page", "widget");
        eventDetails.put("type", "load");

        notifyListener("event", eventDetails);
    }
}