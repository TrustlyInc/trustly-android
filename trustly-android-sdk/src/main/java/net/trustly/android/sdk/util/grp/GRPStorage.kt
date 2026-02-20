package net.trustly.android.sdk.util.grp

import android.content.Context
import net.trustly.android.sdk.data.TrustlyStorage

object GRPStorage {

    private const val GRP_STORAGE: String = "PayWithMyBank"
    private const val GRP: String = "grp"

    fun saveData(context: Context, preferenceValue: Int) {
        getTrustlyStorage(context).saveData(GRP, preferenceValue)
    }

    fun readDataFrom(context: Context) = getTrustlyStorage(context).readIntDataFrom(GRP)

    private fun getTrustlyStorage(context: Context) = TrustlyStorage(context, GRP_STORAGE)

}