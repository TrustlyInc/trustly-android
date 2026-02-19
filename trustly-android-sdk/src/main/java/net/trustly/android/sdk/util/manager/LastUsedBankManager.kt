package net.trustly.android.sdk.util.manager

import net.trustly.android.sdk.data.TrustlyStorage

class LastUsedBankManager(private val trustlyStorage: TrustlyStorage) {

    companion object {

        const val LAST_USED_BANK_PREFERENCES_NAME = "last_used_bank"
        private const val LAST_USED_BANK_ID = "last_used_bank_id"

    }

    fun saveLastUsedBank(lastUsedBank: String) {
        trustlyStorage.saveData(LAST_USED_BANK_ID, lastUsedBank)
    }

    fun getLastUsedBank() = trustlyStorage.readStringDataFrom(LAST_USED_BANK_ID)

}