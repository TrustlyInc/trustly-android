package net.trustly.android.sdk.views.components

import android.content.Context
import android.webkit.WebView
import net.trustly.android.sdk.BuildConfig
import net.trustly.android.sdk.data.model.Settings
import net.trustly.android.sdk.data.TrustlyUrlFetcher
import net.trustly.android.sdk.util.EstablishDataManager
import net.trustly.android.sdk.util.TrustlyConstants.CANCEL_URL
import net.trustly.android.sdk.util.TrustlyConstants.DEVICE_TYPE
import net.trustly.android.sdk.util.TrustlyConstants.ENV
import net.trustly.android.sdk.util.TrustlyConstants.ENV_LOCAL
import net.trustly.android.sdk.util.TrustlyConstants.FUNCTION_INDEX
import net.trustly.android.sdk.util.TrustlyConstants.FUNCTION_MOBILE
import net.trustly.android.sdk.util.TrustlyConstants.GRP
import net.trustly.android.sdk.util.TrustlyConstants.INTEGRATION_STRATEGY_DEFAULT
import net.trustly.android.sdk.util.TrustlyConstants.LANG
import net.trustly.android.sdk.util.TrustlyConstants.METADATA_CID
import net.trustly.android.sdk.util.TrustlyConstants.METADATA_INTEGRATION_CONTEXT
import net.trustly.android.sdk.util.TrustlyConstants.METADATA_LANG
import net.trustly.android.sdk.util.TrustlyConstants.METADATA_SDK_ANDROID_VERSION
import net.trustly.android.sdk.util.TrustlyConstants.METADATA_URL_SCHEME
import net.trustly.android.sdk.util.TrustlyConstants.PAYMENT_PROVIDER_ID
import net.trustly.android.sdk.util.TrustlyConstants.RETURN_URL
import net.trustly.android.sdk.util.TrustlyConstants.SESSION_CID
import net.trustly.android.sdk.util.TrustlyConstants.WIDGET_LOADED
import net.trustly.android.sdk.util.UrlUtils
import net.trustly.android.sdk.util.api.APIRequestManager
import net.trustly.android.sdk.util.cid.CidManager
import net.trustly.android.sdk.views.TrustlyCustomTabsManager
import net.trustly.android.sdk.views.TrustlyView
import net.trustly.android.sdk.views.events.TrustlyEvents
import java.nio.charset.StandardCharsets

class TrustlyLightbox(
    private val context: Context,
    private val webView: WebView,
    private val returnURL: String,
    private val cancelURL: String,
    private val trustlyEvents: TrustlyEvents,
) : TrustlyComponent() {

    private val SDK_VERSION: String = BuildConfig.SDK_VERSION
    
    override fun updateEstablishData(establishData: Map<String, String>, grp: Int) {
        trustlyEvents.notifyOpen()
        CidManager.generateCid(context)

        EstablishDataManager.updateEstablishData(establishData)
        val data = HashMap<String, String>(establishData)

        val lang = establishData[METADATA_LANG]
        if (lang != null) data[LANG] = lang

        data[METADATA_SDK_ANDROID_VERSION] = SDK_VERSION
        data[DEVICE_TYPE] = "${establishData[DEVICE_TYPE] ?: "mobile"}:android:native"
        data[RETURN_URL] = returnURL
        data[CANCEL_URL] = cancelURL
        data[GRP] = grp.toString()

        if (data.containsKey(PAYMENT_PROVIDER_ID)) {
            data[WIDGET_LOADED] = "true"
        }

        val sessionCidValues = CidManager.getOrCreateSessionCid(context)
        data[SESSION_CID] = sessionCidValues[CidManager.SESSION_CID_PARAM]!!
        data[METADATA_CID] = sessionCidValues[CidManager.CID_PARAM]!!

        if (ENV_LOCAL == data[ENV]) {
            TrustlyView.setIsLocalEnvironment(true)
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        }

        if (APIRequestManager.validateAPIRequest(context)) {
            val settings = APIRequestManager.getAPIRequestSettings(context)
            openWebViewOrCustomTabs(settings, data)
        } else {
            super.getSettingsData(TrustlyUrlFetcher(), UrlUtils.getDomain(FUNCTION_MOBILE, establishData), getTokenByEncodedParameters(data)) {
                APIRequestManager.saveAPIRequestSettings(context, it)
                openWebViewOrCustomTabs(it, data)
            }
        }
    }

    private fun openWebViewOrCustomTabs(settings: Settings, establishData: HashMap<String, String>) {
        if (settings.settings.integrationStrategy == INTEGRATION_STRATEGY_DEFAULT) {
            establishData[METADATA_INTEGRATION_CONTEXT] = "InAppBrowser"
            val encodedParameters = UrlUtils.getParameterString(establishData.toMap()).toByteArray(StandardCharsets.UTF_8)
            webView.post { webView.postUrl(UrlUtils.getEndpointUrl(FUNCTION_INDEX, establishData), encodedParameters) }
        } else {
            establishData[METADATA_URL_SCHEME]?.let {
                establishData[RETURN_URL] = it
                establishData[CANCEL_URL] = it
            }
            TrustlyCustomTabsManager.openCustomTabsIntent(
                context,
                UrlUtils.getEndpointUrl(
                    FUNCTION_MOBILE,
                    establishData
                ) + "?token=" + getTokenByEncodedParameters(establishData)
            )
        }
        trustlyEvents.notifyClose()
    }

    private fun getTokenByEncodedParameters(data: Map<String, String>): String {
        val jsonFromParameters = UrlUtils.getJsonFromParameters(data)
        return UrlUtils.encodeStringToBase64(jsonFromParameters).replace("\n", "")
    }
    
}