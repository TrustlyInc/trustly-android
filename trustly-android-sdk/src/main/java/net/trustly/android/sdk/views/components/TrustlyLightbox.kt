package net.trustly.android.sdk.views.components

import android.content.Context
import android.webkit.WebView
import net.trustly.android.sdk.BuildConfig
import net.trustly.android.sdk.data.APIMethod
import net.trustly.android.sdk.data.APIRequest
import net.trustly.android.sdk.data.RetrofitInstance.getInstance
import net.trustly.android.sdk.data.Settings
import net.trustly.android.sdk.data.StrategySetting
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
import net.trustly.android.sdk.views.TrustlyView.Status
import java.nio.charset.StandardCharsets

class TrustlyLightbox(
    val context: Context,
    private val webView: WebView,
    private val returnURL: String,
    private val cancelURL: String,
    private val notifyStatusChanged: (Status) -> Unit,
    private val notifyOpen: () -> Unit
) : TrustlyComponent() {

    private val TAG = "TrustlyLightbox"

    private val SDK_VERSION: String = BuildConfig.SDK_VERSION
    
    override fun updateEstablishData(establishData: Map<String, String>, grp: Int) {
        try {
            notifyStatusChanged.invoke(Status.PANEL_LOADING)
            CidManager.generateCid(context)

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
            sessionCidValues[CidManager.SESSION_CID_PARAM]?.let { data[SESSION_CID] = it }
            sessionCidValues[CidManager.CID_PARAM]?.let { data[METADATA_CID] = it }

            notifyOpen.invoke()

            if (ENV_LOCAL == data[ENV]) {
                WebView.setWebContentsDebuggingEnabled(true)
                TrustlyView.setIsLocalEnvironment(true)
            }

            if (APIRequestManager.validateAPIRequest(context)) {
                val settings = APIRequestManager.getAPIRequestSettings(context)
                openWebViewOrCustomTabs(settings, data)
            } else {
                val apiInterface = getInstance(getDomain(FUNCTION_MOBILE, establishData)).create(APIMethod::class.java)
                val apiRequest = APIRequest(apiInterface, { settings: Settings ->
                    APIRequestManager.saveAPIRequestSettings(context, settings)
                    openWebViewOrCustomTabs(settings, data)
                }, { _: String ->
                    openWebViewOrCustomTabs(Settings(StrategySetting(INTEGRATION_STRATEGY_DEFAULT)), data)
                })
                apiRequest.getSettingsData(getTokenByEncodedParameters(data))
            }
        } catch (e: Exception) {
            showErrorMessage(TAG, e)
        }
    }

    private fun openWebViewOrCustomTabs(settings: Settings, establishData: HashMap<String, String>) {
        if (settings.settings.integrationStrategy == INTEGRATION_STRATEGY_DEFAULT) {
            establishData[METADATA_INTEGRATION_CONTEXT] = "InAppBrowser"
            val encodedParameters = UrlUtils.getParameterString(establishData.toMap()).toByteArray(StandardCharsets.UTF_8)
            webView.postUrl(getEndpointUrl(FUNCTION_INDEX, establishData), encodedParameters)
        } else {
            establishData[RETURN_URL] = establishData[METADATA_URL_SCHEME]!!
            establishData[CANCEL_URL] = establishData[METADATA_URL_SCHEME]!!
            TrustlyCustomTabsManager.openCustomTabsIntent(
                context,
                getEndpointUrl(
                    FUNCTION_MOBILE,
                    establishData
                ) + "?token=" + getTokenByEncodedParameters(establishData)
            )
        }
    }

    private fun getTokenByEncodedParameters(data: Map<String, String>): String {
        val jsonFromParameters = UrlUtils.getJsonFromParameters(data)
        return UrlUtils.encodeStringToBase64(jsonFromParameters).replace("\n", "")
    }
    
}