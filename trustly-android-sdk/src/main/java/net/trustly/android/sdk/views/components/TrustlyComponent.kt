package net.trustly.android.sdk.views.components

import android.webkit.WebView
import net.trustly.android.sdk.data.Settings
import net.trustly.android.sdk.data.TrustlyService
import net.trustly.android.sdk.data.TrustlyUrlFetcher
import net.trustly.android.sdk.interfaces.TrustlyJsInterface

abstract class TrustlyComponent {

    abstract fun updateEstablishData(establishData: Map<String, String>, grp: Int)

    fun startTrustlyComponent(webView: WebView, trustlyJsInterface: TrustlyJsInterface) {
        with(webView) {
            addJavascriptInterface(trustlyJsInterface, "TrustlyNativeSDK")
        }
    }

    fun getSettingsData(
        trustlyUrlFetcher: TrustlyUrlFetcher,
        baseUrl: String,
        token: String,
        settingsCallback: (Settings) -> Unit
    ) {
        TrustlyService(trustlyUrlFetcher).getSettingsData(
            baseUrl,
            token
        ) { settingsCallback.invoke(it) }
    }

    fun postLightboxUrl(
        trustlyUrlFetcher: TrustlyUrlFetcher,
        baseUrl: String,
        userAgentString: String,
        encodedParameters: ByteArray,
        lightboxUrlCallback: (String?) -> Unit
    ) {
        TrustlyService(trustlyUrlFetcher).postLightboxUrl(
            baseUrl,
            userAgentString,
            encodedParameters
        ) { lightboxUrlCallback.invoke(it) }
    }

    enum class Type {
        WIDGET, LIGHTBOX
    }

}