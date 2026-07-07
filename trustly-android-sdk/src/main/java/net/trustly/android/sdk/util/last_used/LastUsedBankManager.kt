package net.trustly.android.sdk.util.last_used

import android.content.Context
import com.google.gson.Gson
import net.trustly.android.sdk.data.LastUsedBank
import net.trustly.android.sdk.util.UrlUtils
import net.trustly.android.sdk.util.storage.TrustlyStorageClient

object LastUsedBankManager {

    private const val LAST_USED_BANK_ID = "LAST_USED_BANK_ID"
    private var storageClientFactory: (() -> TrustlyStorageClient)? = null

    fun saveLastUsedBank(context: Context, lastUsedBank: String) {
        val client = storageClientFactory?.invoke() ?: TrustlyStorageClient(context)
        client.setItem(LAST_USED_BANK_ID, lastUsedBank)
        LastUsedBankStorage.saveData(context, LAST_USED_BANK_ID, lastUsedBank)
    }

    fun getLastUsedBankBase64(context: Context): String? {
        val client = storageClientFactory?.invoke() ?: TrustlyStorageClient(context)
        client.getItem(LAST_USED_BANK_ID)?.let {
            return it
        }

        return LastUsedBankStorage.readStringDataFrom(context, LAST_USED_BANK_ID)
    }

    fun getLaseUsedBankByCountryCode(context: Context, countryCode: String): String? {
        val lastUsedBankBase64 = getLastUsedBankBase64(context) ?: return null
        val decodeBase64ToString = UrlUtils.decodeBase64ToString(lastUsedBankBase64)
        val lastUsedBank = Gson().fromJson(decodeBase64ToString, LastUsedBank::class.java)
        lastUsedBank.lastUsed.entries.forEach {
            if (it.key == countryCode) return it.value
        }
        return null
    }

    @Suppress("unused")
    internal fun setStorageClientFactoryForTesting(factory: (() -> TrustlyStorageClient)?) {
        storageClientFactory = factory
    }

}