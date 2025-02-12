package net.trustly.android.sdk.util.cid

import android.content.Context

object CidStorage {

    private const val CID_STORAGE: String = "CID_STORAGE"
    const val SESSION_CID: String = "SESSION_CID"
    const val CID: String = "CID"

    fun saveData(context: Context, preferenceId: String?, preferenceValue: String?) {
        getSharedPreferences(context).edit().putString(preferenceId, preferenceValue).apply()
    }

    fun readDataFrom(context: Context, preferenceId: String?) =
        getSharedPreferences(context).getString(preferenceId, null)

    private fun getSharedPreferences(context: Context) =
        context.getSharedPreferences(CID_STORAGE, Context.MODE_PRIVATE)

}