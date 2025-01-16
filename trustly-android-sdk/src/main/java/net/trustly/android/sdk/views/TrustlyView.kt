package net.trustly.android.sdk.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
import android.widget.LinearLayout
import android.widget.RelativeLayout
import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.interfaces.TrustlyCallback
import net.trustly.android.sdk.interfaces.TrustlyJsInterface
import net.trustly.android.sdk.interfaces.TrustlyListener
import net.trustly.android.sdk.util.TrustlyConstants.EVENT
import net.trustly.android.sdk.util.TrustlyConstants.EVENT_PAGE
import net.trustly.android.sdk.util.TrustlyConstants.EVENT_TYPE
import net.trustly.android.sdk.util.TrustlyConstants.PAYMENT_PROVIDER_ID
import net.trustly.android.sdk.util.TrustlyConstants.WIDGET
import net.trustly.android.sdk.util.UrlUtils.getQueryParametersFromUrl
import net.trustly.android.sdk.util.grp.GRPManager.getGRP
import net.trustly.android.sdk.util.grp.GRPManager.saveGRP
import net.trustly.android.sdk.views.TrustlyCustomTabsManager.openCustomTabsIntent
import net.trustly.android.sdk.views.clients.TrustlyWebViewChromeClient
import net.trustly.android.sdk.views.clients.TrustlyWebViewClient
import net.trustly.android.sdk.views.components.TrustlyLightbox
import net.trustly.android.sdk.views.components.TrustlyWidget
import net.trustly.android.sdk.views.oauth.TrustlyOAuthView
import java.security.SecureRandom
import java.util.Objects
import java.util.regex.Pattern

/**
 * TrustlyView is a view class that implements the Trustly SDK interface
 */
class TrustlyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), Trustly {

    private val TAG: String = "TrustlyView"

    enum class Status {
        START,
        WIDGET_LOADING,
        WIDGET_LOADED,
        PANEL_LOADING,
        PANEL_LOADED
    }

    private var status = Status.START

    private lateinit var webView: WebView

    private var grp = -1

    private lateinit var data: HashMap<String, String>
    private var onReturn: TrustlyCallback<Trustly, Map<String, String>>? = null
    private var onCancel: TrustlyCallback<Trustly, Map<String, String>>? = null
    private var onWidgetBankSelected: TrustlyCallback<Trustly, Map<String, String>>? = null
    private var onExternalUrl: TrustlyCallback<Trustly, Map<String, String>>? = null

    private var trustlyListener: TrustlyListener? = null

    private var returnURL = "msg://return"
    private var cancelURL = "msg://cancel"

    init {
        initGrp(context)
        initWebView(context)

        setWebViewChromeClient()
        setWebViewClient()

        addView(webView)
    }

    private fun initGrp(context: Context) {
        grp = getGRP(context)
        if (grp < 0) {
            grp = SecureRandom().nextInt(100)
            saveGRP(context, grp)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(context: Context) {
        webView = WebView(context)
        with(webView) {
            isScrollContainer = false
            val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            layoutParams = params

            settings.apply {
                setSupportMultipleWindows(true)
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
            }

            addJavascriptInterface(TrustlyJsInterface(this@TrustlyView), "TrustlyNativeSDK")
        }
    }

    private fun setWebViewChromeClient() {
        webView.webChromeClient = TrustlyWebViewChromeClient(this)
    }

    private fun setWebViewClient() {
        webView.webViewClient = TrustlyWebViewClient(this)
    }

    override fun selectBankWidget(establishData: Map<String, String>): Trustly {
        data = HashMap(establishData)
        val trustlyWidget = TrustlyWidget(context, webView, status, { statusChanged: Status ->
            status = statusChanged
        }, {
            notifyWidgetLoading()
        })
        trustlyWidget.updateEstablishData(establishData, grp)
        return this
    }

    override fun onBankSelected(handler: TrustlyCallback<Trustly, Map<String, String>>?): Trustly {
        this.onWidgetBankSelected = handler
        return this
    }

    override fun establish(establishData: Map<String, String>): Trustly {
        data = HashMap(establishData)
        val trustlyLightbox =
            TrustlyLightbox(context, webView, returnURL, cancelURL, { statusChanged: Status ->
                status = statusChanged
            }, {
                notifyOpen()
            })
        trustlyLightbox.updateEstablishData(establishData, grp)
        return this
    }

    override fun hybrid(url: String, returnUrlHybrid: String, cancelUrlHybrid: String): Trustly {
        this.returnURL = returnUrlHybrid
        this.cancelURL = cancelUrlHybrid
        webView.loadUrl(url)
        return this
    }

    override fun onReturn(handler: TrustlyCallback<Trustly, Map<String, String>>?): Trustly {
        this.onReturn = handler
        return this
    }

    override fun onCancel(handler: TrustlyCallback<Trustly, Map<String, String>>?): Trustly {
        this.onCancel = handler
        return this
    }

    override fun onExternalUrl(handler: TrustlyCallback<Trustly, Map<String, String>>?): Trustly {
        this.onExternalUrl = handler
        return this
    }

    override fun setListener(trustlyListener: TrustlyListener?): Trustly {
        this.trustlyListener = trustlyListener
        return this
    }

    override fun destroy(): Trustly {
        webView.destroy()
        return this
    }

    override fun proceedToChooseAccount() {
        webView.loadUrl("javascript:Paywithmybank.proceedToChooseAccount();")
    }

    fun resize(width: Float, height: Float) {
        Handler(Looper.getMainLooper()).post {
            val displayMetrics = context.resources.displayMetrics
            val widthPixels = applyDimension(width, displayMetrics)
            var heightPixels = 0.0f
            heightPixels = if (height != heightPixels) {
                applyDimension(height, displayMetrics)
            } else {
                applyDimension(width * 1.75f, displayMetrics)
            }
            val params = LayoutParams(widthPixels.toInt(), heightPixels.toInt())
            this.layoutParams = params
        }
    }

    private fun applyDimension(value: Float, displayMetrics: DisplayMetrics): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics)
    }

    fun handleWebChromeClientOnCreateWindow(view: WebView, resultMsg: Message): Boolean {
        val result = view.hitTestResult
        if (result.type == 0) {
            //window.open
            val trustlyOAuthView = TrustlyOAuthView(view.context)
            this.addView(trustlyOAuthView)
            val transport = resultMsg.obj as WebViewTransport
            transport.webView = trustlyOAuthView.getWebView()
            resultMsg.sendToTarget()
            return true
        } else {
            val url = result.extra
            if (onExternalUrl != null) {
                val params = HashMap<String, String>()
                params["url"] = url!!
                onExternalUrl?.handle(this, params)
            } else {
                openCustomTabsIntent(view.context, url!!)
            }
            return false
        }
    }

    fun handleWebViewClientOnReceivedError(trustlyView: TrustlyView, failingUrl: String?) {
        try {
            val isAssetFile =
                failingUrl!!.matches("([^\\\\s]+(\\\\.(?i)(jpg|jpeg|svg|png|css|gif|webp))$)".toRegex())
            if (!isLocalEnvironment() && onCancel != null && !isAssetFile) {
                onCancel!!.handle(trustlyView, HashMap())
            }
        } catch (e: Exception) {
            onCancel?.handle(trustlyView, HashMap())
            showErrorMessage(e)
        }
    }

    fun handleWebViewClientOnPageFinished(view: WebView, trustlyView: TrustlyView) {
        webView.loadUrl("javascript:TrustlyNativeSDK.resize(document.body.scrollWidth, document.body.scrollHeight)")

        if (status == Status.PANEL_LOADING) {
            status = Status.PANEL_LOADED
        } else if (status == Status.WIDGET_LOADING) {
            status = Status.WIDGET_LOADED
            notifyWidgetLoaded()
        }

        val title = view.title
        if (title != null) {
            val p = Pattern.compile("\\d+")
            val m = p.matcher(title)
            while (m.find()) {
                val n = m.group().toLong() / 100
                if (onCancel != null && (n == 4L || n == 5L)) {
                    onCancel!!.handle(trustlyView, HashMap())
                }
            }
        }
    }

    fun handleWebViewClientShouldOverrideUrlLoading(
        trustlyView: TrustlyView,
        url: String
    ): Boolean {
        if (url.startsWith(returnURL) || url.startsWith(cancelURL)) {
            val queryParametersFromUrl = getQueryParametersFromUrl(url)
            if (url.startsWith(returnURL) && onReturn != null) {
                onReturn?.handle(trustlyView, queryParametersFromUrl)
            } else if (onCancel != null) {
                onCancel?.handle(trustlyView, queryParametersFromUrl)
            }
            notifyClose()
            return true
        } else if (url.startsWith("msg://push?")) {
            val params = url.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (params[0].contains("PayWithMyBank.createTransaction")) {
                data[PAYMENT_PROVIDER_ID] = if (params.size > 1) params[1] else ""
                if (onWidgetBankSelected != null) {
                    onWidgetBankSelected?.handle(trustlyView, data)
                }
            }
            return true
        }
        return false
    }

    fun notifyListener(eventName: String, eventDetails: HashMap<String, String>) {
        if (this.trustlyListener != null) {
            this.trustlyListener?.onChange(
                eventName,
                eventDetails
            )
        }
    }

    private fun notifyOpen() {
        notifyListener("open", HashMap())
    }

    private fun notifyClose() {
        notifyListener("close", HashMap())
    }

    private fun notifyWidgetLoading() {
        val eventDetails = HashMap<String, String>()
        eventDetails[EVENT_PAGE] = WIDGET
        eventDetails[EVENT_TYPE] = "loading"
        notifyListener(EVENT, eventDetails)
    }

    private fun notifyWidgetLoaded() {
        val eventDetails = HashMap<String, String>()
        eventDetails[EVENT_PAGE] = WIDGET
        eventDetails[EVENT_TYPE] = "load"
        notifyListener(EVENT, eventDetails)
    }

    private fun showErrorMessage(e: java.lang.Exception) {
        Log.e(TAG, Objects.requireNonNull<String?>(e.message))
    }

    companion object {

        private var isLocalEnvironment: Boolean = false

        fun isLocalEnvironment(): Boolean {
            return isLocalEnvironment
        }

        fun setIsLocalEnvironment(isLocal: Boolean) {
            isLocalEnvironment = isLocal
        }

    }

}