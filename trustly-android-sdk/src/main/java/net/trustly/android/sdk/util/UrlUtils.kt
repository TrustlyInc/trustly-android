package net.trustly.android.sdk.util

import android.net.Uri
import android.util.Base64
import com.google.gson.JsonObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Collections

object UrlUtils {

    private const val EMPTY: String = ""
    private const val AMPERSAND_CHAR: String = "&"
    private const val EQUALS_CHAR: String = "="
    private const val URL: String = "url"
    private const val REQUEST_SIGNATURE: String = "requestSignature=.*"
    private const val SEPARATOR: String = "\\."

    fun getQueryParameterNames(uri: Uri): Set<String> {
        val query = uri.encodedQuery ?: return emptySet()
        val names: MutableSet<String> = LinkedHashSet()
        var start = 0
        do {
            val next = query.indexOf(AMPERSAND_CHAR, start)
            val end = if ((next == -1)) query.length else next
            var separator = query.indexOf(EQUALS_CHAR, start)
            if (separator > end || separator == -1) {
                separator = end
            }
            val name = query.substring(start, separator)
            names.add(name)
            // Move start to end of name.
            start = end + 1
        } while (start < query.length)
        return Collections.unmodifiableSet(names)
    }

    fun getQueryParametersFromUrl(url: String): Map<String, String?> {
        val uri = Uri.parse(url)
        val queryParameters: MutableMap<String, String?> = HashMap()
        queryParameters[URL] = url.replace(REQUEST_SIGNATURE.toRegex(), EMPTY)
        val queryParametersKeys = getQueryParameterNames(uri)
        for (queryParameterKey in queryParametersKeys) {
            queryParameters[queryParameterKey] = uri.getQueryParameter(queryParameterKey)
        }
        return queryParameters
    }

    fun getParameterString(parameters: Map<String?, String?>): String {
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
            val keys = key1.split(SEPARATOR.toRegex())
                .toTypedArray()
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

}