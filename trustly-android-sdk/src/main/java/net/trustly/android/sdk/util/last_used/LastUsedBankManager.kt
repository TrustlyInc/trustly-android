package net.trustly.android.sdk.util.last_used

import android.content.Context
import net.trustly.android.sdk.data.TrustlyStorage
import net.trustly.android.sdk.util.UrlUtils

class LastUsedBankManager(val context: Context) {

    companion object {

        private const val LAST_USED_BANK_PREFERENCES_NAME = "last_used_bank"
        private const val LAST_USED_BANK_ID = "last_used_bank_id"

    }

    fun saveLastUsedBank(lastUsedBank: String) {
        getTrustlyStorage(context).saveData(LAST_USED_BANK_ID, lastUsedBank)
    }

    fun getLastUsedBank(isDecodeFromBase64: Boolean = false): String? {
        val lastUsedBank = getTrustlyStorage(context).readStringDataFrom(LAST_USED_BANK_ID)
        if (isDecodeFromBase64) {
            return UrlUtils.decodeBase64ToString(lastUsedBank!!)
        }
        return lastUsedBank
    }

    private fun getTrustlyStorage(context: Context) =
        TrustlyStorage(context, LAST_USED_BANK_PREFERENCES_NAME)

}