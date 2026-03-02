package net.trustly.android.sdk.util.last_used

import android.content.Context

object LastUsedBankManager {

    private const val LAST_USED_BANK_ID = "last_used_bank_id"

    fun saveLastUsedBank(context: Context, lastUsedBank: String) {
        LastUsedBankStorage.saveData(context, LAST_USED_BANK_ID, lastUsedBank)
    }

    fun getLastUsedBankBase64(context: Context) =
        LastUsedBankStorage.readStringDataFrom(context, LAST_USED_BANK_ID)

}