package net.trustly.android.sdk.views.components

import android.util.Log
import net.trustly.android.sdk.data.APIMethod
import net.trustly.android.sdk.data.APIRequest
import net.trustly.android.sdk.data.Settings

abstract class TrustlyComponent {

    private val TAG = "TrustlyComponent"

    abstract fun updateEstablishData(establishData: Map<String, String>, grp: Int)

    fun getSettingsData(apiInterface: APIMethod, token: String, settingsCallback: (Settings) -> Unit, errorCallback: (String) -> Unit) {
        APIRequest(apiInterface, {
            settingsCallback.invoke(it)
        }, {
            errorCallback.invoke(it)
            Log.e(TAG, it)
        }).getSettingsData(token)
    }

}