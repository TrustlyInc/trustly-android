package net.trustly.android.sdk.util.cid

import android.content.Context
import net.trustly.android.sdk.data.TrustlyStorage

object CidStorage {

    private const val CID_STORAGE: String = "CID_STORAGE"
    const val SESSION_CID: String = "SESSION_CID"
    const val CID: String = "CID"

    fun saveData(context: Context, preferenceId: String, preferenceValue: String?) {
        getTrustlyStorage(context).saveData(preferenceId, preferenceValue)
    }

    fun readDataFrom(context: Context, preferenceId: String) =
        getTrustlyStorage(context).readStringDataFrom(preferenceId)

    private fun getTrustlyStorage(context: Context) = TrustlyStorage(context, CID_STORAGE)

}