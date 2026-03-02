package net.trustly.android.sdk.util.last_used

import android.content.Context
import net.trustly.android.sdk.util.UrlUtils

object LastUsedBankManager {

    private const val LAST_USED_BANK_ID = "last_used_bank_id"

    fun saveLastUsedBank(context: Context, lastUsedBank: String) {
        LastUsedBankStorage.saveData(context, LAST_USED_BANK_ID, lastUsedBank)
    }

    fun getLastUsedBank(context: Context, isDecodeFromBase64: Boolean = false): String? {
        val lastUsedBank = LastUsedBankStorage.readStringDataFrom(context, LAST_USED_BANK_ID)
        if (isDecodeFromBase64) {
            return UrlUtils.decodeBase64ToString(lastUsedBank!!)
        }
        return lastUsedBank
    }

}