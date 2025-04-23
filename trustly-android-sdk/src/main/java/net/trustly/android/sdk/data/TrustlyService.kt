package net.trustly.android.sdk.data

import com.google.gson.Gson
import net.trustly.android.sdk.data.model.Settings
import net.trustly.android.sdk.data.model.StrategySetting
import net.trustly.android.sdk.util.TrustlyConstants.INTEGRATION_STRATEGY_DEFAULT
import java.net.URL

class TrustlyService(private val urlFetcher: TrustlyUrlFetcher) {

    fun getSettingsData(baseUrl: String, token: String, settings: (Settings) -> Unit) {
        val defaultResponse = Settings(StrategySetting(INTEGRATION_STRATEGY_DEFAULT))
        Thread {
            urlFetcher.let {
                val url = URL("$baseUrl/frontend/mobile/setup?token=$token")
                it.openConnection(url)
                it.setRequestMethod("GET")
                it.setTimeOut(10000)
                try {
                    if (it.isUrlAvailable()) {
                        val apiResponse = Gson().fromJson(it.getResponse(), Settings::class.java)
                        settings.invoke(apiResponse)
                    } else {
                        settings.invoke(defaultResponse)
                    }
                } catch (_: Exception) {
                    settings.invoke(defaultResponse)
                } finally {
                    it.disconnect()
                }
            }
        }.start()
    }

}