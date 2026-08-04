package net.trustly.android.sdk.util.last_used

import android.content.Context
import com.google.gson.Gson
import net.trustly.android.sdk.data.LastUsedBank
import net.trustly.android.sdk.util.UrlUtils

/**
 * Holds the last used bank context for the current session only.
 *
 * Cross-app and cross-session persistence of the last used bank is owned by the
 * web layer (localStorage on trustly.com within the shared Custom Tabs browser).
 * The SDK no longer persists this value to device storage, so no PII is written
 * to disk. This in-memory cache only forwards the context received from the web
 * back to subsequent flows within the same session.
 */
object LastUsedBankManager {

    private var lastUsedBankBase64: String? = null

    fun saveLastUsedBank(context: Context, lastUsedBank: String) {
        lastUsedBankBase64 = lastUsedBank
    }

    fun getLastUsedBankBase64(context: Context): String? = lastUsedBankBase64

    fun getLaseUsedBankByCountryCode(context: Context, countryCode: String): String? {
        val lastUsedBankBase64 = getLastUsedBankBase64(context) ?: return null
        val decodeBase64ToString = UrlUtils.decodeBase64ToString(lastUsedBankBase64)
        val lastUsedBank = Gson().fromJson(decodeBase64ToString, LastUsedBank::class.java)
        lastUsedBank.lastUsed.entries.forEach {
            if (it.key == countryCode) return it.value
        }
        return null
    }

    internal fun clearForTesting() {
        lastUsedBankBase64 = null
    }

}
