package net.trustly.android.sdk.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.interfaces.TrustlyCallback
import net.trustly.android.sdk.interfaces.TrustlyListener
import net.trustly.android.sdk.util.error.TrustlyExceptionHandler
import net.trustly.android.sdk.util.grp.GRPManager
import net.trustly.android.sdk.views.clients.TrustlyWebViewChromeClient
import net.trustly.android.sdk.views.clients.TrustlyWebViewClient
import net.trustly.android.sdk.views.components.TrustlyLightbox
import net.trustly.android.sdk.views.components.TrustlyWidget
import net.trustly.android.sdk.views.events.TrustlyEvents
import java.security.SecureRandom

/**
 * TrustlyView is a view class that implements the Trustly SDK interface
 */
class TrustlyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), Trustly {

    private var grp = -1
    private var returnURL = "msg://return"
    private var cancelURL = "msg://cancel"

    private lateinit var trustlyEvents: TrustlyEvents
    private lateinit var webView: WebView

    init {
        Thread.setDefaultUncaughtExceptionHandler(TrustlyExceptionHandler())

        this.initEvents()
        this.initGrp(context)
        this.initWebView(context)
        this.setWebViewChromeClient()
        this.setWebViewClient()

        addView(webView)
    }

    private fun initGrp(context: Context) {
        grp = GRPManager.getGRP(context)
        if (grp < 0) {
            grp = SecureRandom().nextInt(100)
            GRPManager.saveGRP(context, grp)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(context: Context) {
        webView = WebView(context)
        with(webView) {
            isScrollContainer = false
            val params = RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
            )
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            layoutParams = params

            settings.apply {
                setSupportMultipleWindows(true)
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
            }
        }
    }

    private fun initEvents() {
        trustlyEvents = TrustlyEvents()
    }

    private fun setWebViewChromeClient() {
        webView.webChromeClient = TrustlyWebViewChromeClient(context, this, trustlyEvents)
    }

    private fun setWebViewClient() {
        webView.webViewClient = TrustlyWebViewClient(this, returnURL, cancelURL, trustlyEvents)
    }

    override fun selectBankWidget(establishData: Map<String, String>): Trustly {
        val trustlyWidget = TrustlyWidget(this, context, webView, trustlyEvents)
        trustlyWidget.updateEstablishData(establishData, grp)
        return this
    }

    override fun onBankSelected(handler: TrustlyCallback<Trustly, Map<String, String>>?): Trustly {
        this.trustlyEvents.setOnWidgetBankSelectedCallback(handler)
        return this
    }

    override fun establish(establishData: Map<String, String>): Trustly {
        val trustlyLightbox = TrustlyLightbox(this, context, webView, returnURL, cancelURL, trustlyEvents)
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
        this.trustlyEvents.setOnReturnCallback(handler)
        return this
    }

    override fun onCancel(handler: TrustlyCallback<Trustly, Map<String, String>>?): Trustly {
        this.trustlyEvents.setOnCancelCallback(handler)
        return this
    }

    override fun onExternalUrl(handler: TrustlyCallback<Trustly, Map<String, String>>?): Trustly {
        this.trustlyEvents.setOnExternalUrlCallback(handler)
        return this
    }

    override fun setListener(trustlyListener: TrustlyListener?): Trustly {
        this.trustlyEvents.setTrustlyListener(trustlyListener)
        return this
    }

    override fun destroy(): Trustly {
        webView.destroy()
        return this
    }

    override fun proceedToChooseAccount() {
        webView.loadUrl("javascript:Trustly.proceedToChooseAccount();")
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

    private fun applyDimension(value: Float, displayMetrics: DisplayMetrics) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics)

    companion object {

        private var isLocalEnvironment: Boolean = false

        fun isLocalEnvironment() = isLocalEnvironment

        fun setIsLocalEnvironment(isLocal: Boolean) {
            isLocalEnvironment = isLocal
        }

    }

}