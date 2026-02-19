package net.trustly.android.sdk.util

import android.content.Context
import androidx.core.content.edit

class TrustlyStorage(val context: Context, val preferencesName: String) {

    fun saveData(preferenceId: String, preferenceValue: Int) {
        getSharedPreferences().edit { putInt(preferenceId, preferenceValue) }
    }

    fun saveData(preferenceId: String, preferenceValue: String) {
        getSharedPreferences().edit { putString(preferenceId, preferenceValue) }
    }

    fun readIntDataFrom(preferenceId: String) =
        getSharedPreferences().getInt(preferenceId, -1)

    fun readStringDataFrom(preferenceId: String) =
        getSharedPreferences().getString(preferenceId, null)

    private fun getSharedPreferences() =
        context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

}