package net.trustly.android.sdk.views.components

import android.content.Context
import android.graphics.Color
import android.webkit.WebView
import net.trustly.android.sdk.util.TrustlyConstants.ACCESS_ID
import net.trustly.android.sdk.util.TrustlyConstants.CID
import net.trustly.android.sdk.util.TrustlyConstants.CUSTOMER_ADDRESS_COUNTRY
import net.trustly.android.sdk.util.TrustlyConstants.CUSTOMER_ADDRESS_STATE
import net.trustly.android.sdk.util.TrustlyConstants.CUSTOMER_EXTERNAL_ID
import net.trustly.android.sdk.util.TrustlyConstants.DEVICE_TYPE
import net.trustly.android.sdk.util.TrustlyConstants.DYNAMIC_WIDGET
import net.trustly.android.sdk.util.TrustlyConstants.GRP
import net.trustly.android.sdk.util.TrustlyConstants.LANG
import net.trustly.android.sdk.util.TrustlyConstants.MERCHANT_ID
import net.trustly.android.sdk.util.TrustlyConstants.MERCHANT_REFERENCE
import net.trustly.android.sdk.util.TrustlyConstants.METADATA_LANG
import net.trustly.android.sdk.util.TrustlyConstants.PAYMENT_TYPE
import net.trustly.android.sdk.util.TrustlyConstants.SESSION_CID
import net.trustly.android.sdk.util.TrustlyConstants.WIDGET
import net.trustly.android.sdk.util.UrlUtils
import net.trustly.android.sdk.util.cid.CidManager
import net.trustly.android.sdk.views.TrustlyView.Status

class TrustlyWidget(
    private val context: Context,
    private val webView: WebView,
    private var status: Status,
    private val notifyStatusChanged: (Status) -> Unit,
    private val notifyWidgetLoading: () -> Unit
) : TrustlyComponent() {

    private val AMPERSAND_CHAR: String = "&"
    private val HASHTAG_SIGN: String = "#"
    private val CUSTOMER_ADDRESS_COUNTRY_DEFAULT = "US"

    override fun updateEstablishData(establishData: Map<String, String>, grp: Int) {
        val data = HashMap<String?, String?>()
        data[ACCESS_ID] = establishData[ACCESS_ID]
        data[MERCHANT_ID] = establishData[MERCHANT_ID]
        data[PAYMENT_TYPE] = establishData[PAYMENT_TYPE]
        data[DEVICE_TYPE] = "${establishData[DEVICE_TYPE] ?: "mobile"}:android:hybrid"

        val lang = establishData[METADATA_LANG]
        if (lang != null) data[LANG] = lang
        data[GRP] = grp.toString()
        data[DYNAMIC_WIDGET] = "true"

        if (establishData[CUSTOMER_ADDRESS_COUNTRY] != null) {
            data[CUSTOMER_ADDRESS_COUNTRY] = establishData[CUSTOMER_ADDRESS_COUNTRY]
        } else {
            data[CUSTOMER_ADDRESS_COUNTRY] = CUSTOMER_ADDRESS_COUNTRY_DEFAULT
            data[CUSTOMER_ADDRESS_STATE] = establishData[CUSTOMER_ADDRESS_STATE]
        }

        val sessionCidValues = CidManager.getOrCreateSessionCid(context)
        data[SESSION_CID] = sessionCidValues[CidManager.SESSION_CID_PARAM]
        data[CID] = sessionCidValues[CidManager.CID_PARAM]

        val hash: MutableMap<String?, String?> = HashMap()
        hash[MERCHANT_REFERENCE] = establishData[MERCHANT_REFERENCE]
        hash[CUSTOMER_EXTERNAL_ID] = establishData[CUSTOMER_EXTERNAL_ID]

        if (status != Status.WIDGET_LOADED) {
            notifyStatusChanged.invoke(Status.WIDGET_LOADING)
            notifyWidgetLoading.invoke()

            val dataParameters = UrlUtils.getParameterString(data)
            val hashParameters = UrlUtils.getParameterString(hash)
            val url: String = UrlUtils.getEndpointUrl(
                WIDGET,
                establishData
            ) + AMPERSAND_CHAR + dataParameters + HASHTAG_SIGN + hashParameters

            with(webView) {
                loadUrl(url)
                setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}