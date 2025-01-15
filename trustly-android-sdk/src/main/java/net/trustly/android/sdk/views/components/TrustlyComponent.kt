package net.trustly.android.sdk.views.components

import android.util.Log
import net.trustly.android.sdk.BuildConfig
import net.trustly.android.sdk.util.TrustlyConstants.ENV
import net.trustly.android.sdk.util.TrustlyConstants.ENV_DYNAMIC
import net.trustly.android.sdk.util.TrustlyConstants.ENV_HOST
import net.trustly.android.sdk.util.TrustlyConstants.ENV_LOCAL
import net.trustly.android.sdk.util.TrustlyConstants.ENV_LOCALHOST
import net.trustly.android.sdk.util.TrustlyConstants.ENV_PROD
import net.trustly.android.sdk.util.TrustlyConstants.ENV_PRODUCTION
import net.trustly.android.sdk.util.TrustlyConstants.FUNCTION_INDEX
import net.trustly.android.sdk.util.TrustlyConstants.FUNCTION_MOBILE
import net.trustly.android.sdk.util.TrustlyConstants.METADATA_SDK_ANDROID_VERSION
import net.trustly.android.sdk.util.TrustlyConstants.PAYMENT_PROVIDER_ID
import net.trustly.android.sdk.util.TrustlyConstants.PAYMENT_TYPE
import java.util.Locale
import java.util.Objects

abstract class TrustlyComponent {

    private val PROTOCOL: String = "https://"
    private val LOCAL_PROTOCOL: String = "http://"
    private val DOMAIN: String = "paywithmybank.com"

    abstract fun updateEstablishData(establishData: Map<String, String>, grp: Int)

    /**
     * {@inheritDoc}
     */
    protected fun getEndpointUrl(function: String, establishData: Map<String, String>): String {
        var endPoint = function
        val domain = getDomain(function, establishData)
        if (FUNCTION_MOBILE == function) {
            return "$domain/frontend/mobile/establish"
        }
        if (FUNCTION_INDEX == function && "Verification" != establishData[PAYMENT_TYPE] && establishData[PAYMENT_PROVIDER_ID] != null) {
            endPoint = "selectBank"
        }
        return "$domain/start/selectBank/$endPoint?v=${establishData[METADATA_SDK_ANDROID_VERSION]}android-sdk"
    }

    protected fun getDomain(function: String, establishData: Map<String, String>): String {
        val envHost = establishData[ENV_HOST]
        var environment = if (establishData[ENV] != null) Objects.requireNonNull<String?>(
            establishData[ENV]
        ).lowercase(Locale.getDefault()) else null
        if (environment == null) {
            return PROTOCOL + DOMAIN
        }
        environment = when (environment) {
            ENV_DYNAMIC -> {
                val host = envHost ?: ""
                return "$PROTOCOL$host.int.trustly.one"
            }

            ENV_LOCAL -> {
                val host = if ((envHost != null && envHost != ENV_LOCALHOST)) envHost else BuildConfig.LOCAL_IP
                val port = if (FUNCTION_MOBILE == function) ":10000" else ":8000"
                return LOCAL_PROTOCOL + host + port
            }

            ENV_PROD, ENV_PRODUCTION -> ""
            else -> "$environment."
        }
        return PROTOCOL + environment + DOMAIN
    }

    protected fun showErrorMessage(tag: String, e: Exception) {
        Log.e(tag, e.message, e)
    }

}