package net.trustly.android.sdk.util.last_used

import android.content.Context
import androidx.core.content.edit

object LastUsedBankStorage {

    private const val LAST_USED_BANK_PREFERENCES_NAME = "LAST_USED_BANK"

    fun saveData(context: Context, preferenceId: String, preferenceValue: Int) {
        getSharedPreferences(context).edit { putInt(preferenceId, preferenceValue) }
    }

    fun saveData(context: Context, preferenceId: String, preferenceValue: String?) {
        getSharedPreferences(context).edit { putString(preferenceId, preferenceValue) }
    }

    fun readIntDataFrom(context: Context, preferenceId: String) =
        getSharedPreferences(context).getInt(preferenceId, -1)

    fun readStringDataFrom(context: Context, preferenceId: String) =
        getSharedPreferences(context).getString(preferenceId, null)

    private fun getSharedPreferences(context: Context) =
        context.getSharedPreferences(LAST_USED_BANK_PREFERENCES_NAME, Context.MODE_PRIVATE)

}