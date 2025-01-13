package net.trustly.android.sdk.util.grp

import android.content.Context

object GRPStorage {

    private const val GRP_STORAGE: String = "PayWithMyBank"
    private const val GRP: String = "grp"

    fun saveData(context: Context, preferenceValue: Int) {
        getSharedPreferences(context).edit().putInt(GRP, preferenceValue).apply()
    }

    fun readDataFrom(context: Context) = getSharedPreferences(context).getInt(GRP, -1)

    private fun getSharedPreferences(context: Context) =
        context.getSharedPreferences(GRP_STORAGE, Context.MODE_PRIVATE)

}