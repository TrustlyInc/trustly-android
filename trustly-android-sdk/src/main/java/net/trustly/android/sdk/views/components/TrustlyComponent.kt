package net.trustly.android.sdk.views.components

import net.trustly.android.sdk.data.Settings
import net.trustly.android.sdk.data.TrustlyService
import net.trustly.android.sdk.data.TrustlyUrlFetcher

abstract class TrustlyComponent {

    abstract fun updateEstablishData(establishData: Map<String, String>, grp: Int)

    fun getSettingsData(
        trustlyUrlFetcher: TrustlyUrlFetcher,
        baseUrl: String,
        token: String,
        settingsCallback: (Settings) -> Unit
    ) {
        TrustlyService(trustlyUrlFetcher) { settingsCallback.invoke(it) }.getSettingsData(
            baseUrl,
            token
        )
    }

}