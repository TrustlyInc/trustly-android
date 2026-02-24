package net.trustly.android.sdk.util.last_used

import net.trustly.android.sdk.data.TrustlyStorage
import net.trustly.android.sdk.util.UrlUtils

class LastUsedBankManager(private val trustlyStorage: TrustlyStorage) {

    companion object {

        const val LAST_USED_BANK_PREFERENCES_NAME = "last_used_bank"
        private const val LAST_USED_BANK_ID = "last_used_bank_id"

    }

    fun saveLastUsedBank(lastUsedBank: String) {
        trustlyStorage.saveData(LAST_USED_BANK_ID, lastUsedBank)
    }

    fun getLastUsedBank(isDecodeFromBase64: Boolean = false): String? {
        val lastUsedBank = trustlyStorage.readStringDataFrom(LAST_USED_BANK_ID)
        if (isDecodeFromBase64) {
            return UrlUtils.decodeBase64ToString(lastUsedBank!!)
        }
        return lastUsedBank
    }

}