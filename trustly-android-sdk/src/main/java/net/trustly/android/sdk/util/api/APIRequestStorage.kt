package net.trustly.android.sdk.util.api

import android.content.Context
import net.trustly.android.sdk.data.TrustlyStorage

object APIRequestStorage {

    private const val API_STORAGE = "API_STORAGE"

    fun saveData(context: Context, preferenceId: String, preferenceValue: String) {
        getTrustlyStorage(context).saveData(preferenceId, preferenceValue)
    }

    fun readDataFrom(context: Context, preferenceId: String) =
        getTrustlyStorage(context).readStringDataFrom(preferenceId)

    private fun getTrustlyStorage(context: Context) = TrustlyStorage(context, API_STORAGE)

}