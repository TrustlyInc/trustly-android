package net.trustly.android.sdk.util.api

import android.content.Context
import android.content.SharedPreferences

object APIRequestStorage {

    private const val API_STORAGE = "API_STORAGE"

    fun saveData(context: Context, preferenceId: String?, preferenceValue: String?) {
        getSharedPreferences(context).edit().putString(preferenceId, preferenceValue).apply()
    }

    fun readDataFrom(context: Context, preferenceId: String?): String? {
        return getSharedPreferences(context).getString(preferenceId, null)
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(API_STORAGE, Context.MODE_PRIVATE)
    }

}