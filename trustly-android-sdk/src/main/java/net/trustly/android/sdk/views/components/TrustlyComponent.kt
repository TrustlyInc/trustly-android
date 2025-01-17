package net.trustly.android.sdk.views.components

import android.util.Log
import net.trustly.android.sdk.data.APIMethod
import net.trustly.android.sdk.data.APIRequest
import net.trustly.android.sdk.data.Settings
import net.trustly.android.sdk.data.StrategySetting
import net.trustly.android.sdk.util.TrustlyConstants.INTEGRATION_STRATEGY_DEFAULT

abstract class TrustlyComponent {

    private val TAG = "TrustlyComponent"

    abstract fun updateEstablishData(establishData: Map<String, String>, grp: Int)

    fun getSettingsData(apiInterface: APIMethod, token: String, settingsCallback: (Settings) -> Unit) {
        APIRequest(apiInterface, {
            settingsCallback.invoke(it)
        }, {
            settingsCallback.invoke(Settings(StrategySetting(INTEGRATION_STRATEGY_DEFAULT)))
            Log.e(TAG, it)
        }).getSettingsData(token)
    }

}