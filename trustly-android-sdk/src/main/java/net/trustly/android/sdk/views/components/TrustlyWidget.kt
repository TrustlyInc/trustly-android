package net.trustly.android.sdk.views.components

import android.content.Context
import android.graphics.Color
import android.webkit.WebView
import net.trustly.android.sdk.util.EstablishDataManager
import net.trustly.android.sdk.util.TrustlyConstants.CID
import net.trustly.android.sdk.util.TrustlyConstants.CUSTOMER_ADDRESS_COUNTRY
import net.trustly.android.sdk.util.TrustlyConstants.CUSTOMER_ADDRESS_STATE
import net.trustly.android.sdk.util.TrustlyConstants.DEVICE_TYPE
import net.trustly.android.sdk.util.TrustlyConstants.DYNAMIC_WIDGET
import net.trustly.android.sdk.util.TrustlyConstants.GRP
import net.trustly.android.sdk.util.TrustlyConstants.LANG
import net.trustly.android.sdk.util.TrustlyConstants.METADATA_LANG
import net.trustly.android.sdk.util.TrustlyConstants.SESSION_CID
import net.trustly.android.sdk.util.TrustlyConstants.WIDGET
import net.trustly.android.sdk.util.UrlUtils
import net.trustly.android.sdk.util.cid.CidManager
import net.trustly.android.sdk.views.events.TrustlyEvents

class TrustlyWidget(
    private val context: Context,
    private val webView: WebView,
    private val trustlyEvents: TrustlyEvents
) : TrustlyComponent() {

    override fun updateEstablishData(establishData: Map<String, String>, grp: Int) {
        trustlyEvents.notifyWidgetLoading()
        EstablishDataManager.updateEstablishData(establishData)

        val data = HashMap<String, String>(establishData)
        data[DEVICE_TYPE] = "${establishData[DEVICE_TYPE] ?: "mobile"}:android:hybrid"

        val lang = establishData[METADATA_LANG]
        if (lang != null) data[LANG] = lang
        data[GRP] = grp.toString()
        data[DYNAMIC_WIDGET] = "true"

        if (establishData[CUSTOMER_ADDRESS_COUNTRY] != null) {
            establishData[CUSTOMER_ADDRESS_COUNTRY]?.let { data[CUSTOMER_ADDRESS_COUNTRY] = it }
        } else {
            data[CUSTOMER_ADDRESS_COUNTRY] = CUSTOMER_ADDRESS_COUNTRY_DEFAULT
            establishData[CUSTOMER_ADDRESS_STATE]?.let { data[CUSTOMER_ADDRESS_STATE] = it }
        }

        val sessionCidValues = CidManager.getOrCreateSessionCid(context)
        sessionCidValues[CidManager.SESSION_CID_PARAM]?.let { data[SESSION_CID] = it }
        sessionCidValues[CidManager.CID_PARAM]?.let { data[CID] = it }

        val dataParameters = UrlUtils.getParameterString(data)
        val hashParameters = UrlUtils.encodeStringToBase64(dataParameters)
        val url: String = UrlUtils.getEndpointUrl(
            WIDGET,
            establishData
        ) + AMPERSAND_CHAR + dataParameters + HASHTAG_SIGN + hashParameters

        with(webView) {
            loadUrl(url)
            setBackgroundColor(Color.TRANSPARENT)
        }
        trustlyEvents.notifyWidgetLoaded()
    }

    companion object {

        const val AMPERSAND_CHAR: String = "&"
        const val HASHTAG_SIGN: String = "#"
        const val CUSTOMER_ADDRESS_COUNTRY_DEFAULT = "US"

    }

}