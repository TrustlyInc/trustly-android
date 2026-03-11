package net.trustly.android.sdk.util.last_used

import android.content.Context
import com.google.gson.Gson
import net.trustly.android.sdk.data.LastUsedBank
import net.trustly.android.sdk.util.UrlUtils

object LastUsedBankManager {

    private const val LAST_USED_BANK_ID = "LAST_USED_BANK_ID"

    fun saveLastUsedBank(context: Context, lastUsedBank: String) {
        LastUsedBankStorage.saveData(context, LAST_USED_BANK_ID, lastUsedBank)
    }

    fun getLastUsedBankBase64(context: Context) =
        LastUsedBankStorage.readStringDataFrom(context, LAST_USED_BANK_ID)

    fun getLaseUsedBankByCountryCode(context: Context, countryCode: String): String? {
        val lastUsedBankBase64 = getLastUsedBankBase64(context) ?: return null
        val decodeBase64ToString = UrlUtils.decodeBase64ToString(lastUsedBankBase64)
        val lastUsedBank = Gson().fromJson(decodeBase64ToString, LastUsedBank::class.java)
        lastUsedBank.lastUsed.entries.forEach {
            if (it.key == countryCode) return it.value
        }
        return null
    }

}