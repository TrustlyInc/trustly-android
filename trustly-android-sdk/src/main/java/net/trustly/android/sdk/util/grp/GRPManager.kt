package net.trustly.android.sdk.util.grp

import android.content.Context

object GRPManager {

    fun getGRP(context: Context) = GRPStorage.readDataFrom(context)

    fun saveGRP(context: Context, grp: Int) = GRPStorage.saveData(context, grp)

}