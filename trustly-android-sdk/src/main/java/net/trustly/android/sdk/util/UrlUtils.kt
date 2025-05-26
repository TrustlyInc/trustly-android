package net.trustly.android.sdk.util

import android.net.Uri
import android.util.Base64
import com.google.gson.JsonObject
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Locale
import java.util.Objects

object UrlUtils {

    private const val EMPTY: String = ""
    private const val AMPERSAND_CHAR: String = "&"
    private const val EQUALS_CHAR: String = "="
    private const val URL: String = "url"
    private const val REQUEST_SIGNATURE: String = "requestSignature=.*"
    private const val SEPARATOR: String = "\\."
    private const val PROTOCOL: String = "https://"
    private const val LOCAL_PROTOCOL: String = "http://"
    private const val PAYWITHMYBANK: String = "paywithmybank"
    private const val DOMAIN: String = "$PAYWITHMYBANK.com"

    fun getQueryParameterNames(uri: Uri): Map<String, String> {
        val query = uri.encodedQuery ?: return emptyMap()
        val names: HashMap<String, String> = HashMap()
        var start = 0
        do {
            val next = query.indexOf(AMPERSAND_CHAR, start)
            val end = if ((next == -1)) query.length else next
            var separator = query.indexOf(EQUALS_CHAR, start)
            if (separator > end || separator == -1) {
                separator = end
            }
            val name = query.substring(start, separator)
            if (separator + 1 <= end) {
                names[name] = query.substring(separator + 1, end)
            }
            // Move start to end of name.
            start = end + 1
        } while (start < query.length)
        return names
    }

    fun getQueryParametersFromUrl(url: String): Map<String, String> {
        val uri = Uri.parse(url)
        val queryParameters: HashMap<String, String> = HashMap()
        queryParameters[URL] = url.replace(REQUEST_SIGNATURE.toRegex(), EMPTY)
        queryParameters.putAll(getQueryParameterNames(uri))
        return queryParameters
    }

    fun getParameterString(parameters: Map<String, String>): String {
        val sb = StringBuilder()
        for ((key, value) in parameters) {
            if (sb.isNotEmpty()) sb.append(AMPERSAND_CHAR)
            sb.append(urlEncode(key))
            sb.append(EQUALS_CHAR)
            sb.append(urlEncode(value))
        }
        return sb.toString()
    }

    private fun urlEncode(str: String?): String {
        if (str == null) return EMPTY
        return URLEncoder.encode(str, StandardCharsets.UTF_8.name())
    }

    fun getJsonFromParameters(parameters: Map<String, String>) =
        buildJsonObjectSecond(parameters).toString()

    private fun buildJsonObjectSecond(data: Map<String, String>): JsonObject {
        val json = JsonObject()
        for ((key1, value) in data) {
            val keys = key1.split(SEPARATOR.toRegex()).toTypedArray()
            var current = json
            for (i in keys.indices) {
                val key = keys[i]
                if (i == keys.size - 1) {
                    current.addProperty(key, value)
                } else {
                    if (!current.has(key)) {
                        current.add(key, JsonObject())
                    }
                    current = current.getAsJsonObject(key)
                }
            }
        }
        return json
    }

    fun encodeStringToBase64(value: String): String =
        Base64.encodeToString(value.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT)

    fun getEndpointUrl(function: String, establishData: Map<String, String>): String {
        var endPoint = function
        val domain = getDomain(function, establishData)
        if (FUNCTION_MOBILE == function) {
            return "$domain/frontend/mobile/establish"
        }
        if (FUNCTION_INDEX == function && "Verification" != establishData[PAYMENT_TYPE] && establishData[PAYMENT_PROVIDER_ID] != null) {
            endPoint = "selectBank"
        }
        return "$domain/start/selectBank/$endPoint?v=${establishData[METADATA_SDK_ANDROID_VERSION]}-android-sdk"
    }

    fun getDomain(function: String, establishData: Map<String, String>): String {
        var environment = if (establishData[ENV] != null) Objects.requireNonNull<String?>(
            establishData[ENV]
        ).lowercase(Locale.getDefault()) else null
        if (environment == null) {
            return PROTOCOL + DOMAIN
        }
        val envHost = establishData[ENV_HOST]
        environment = when (environment) {
            ENV_DYNAMIC -> {
                val host = envHost ?: PAYWITHMYBANK
                return "$PROTOCOL$host.int.trustly.one"
            }

            ENV_LOCAL -> {
                val host = if (envHost != null && envHost != ENV_LOCALHOST) envHost else BuildConfig.LOCAL_IP
                val port = if (FUNCTION_MOBILE == function) ":10000" else ":8000"
                return LOCAL_PROTOCOL + host + port
            }

            ENV_PROD, ENV_PRODUCTION -> ""
            else -> "$environment."
        }
        return PROTOCOL + environment + DOMAIN
    }

}