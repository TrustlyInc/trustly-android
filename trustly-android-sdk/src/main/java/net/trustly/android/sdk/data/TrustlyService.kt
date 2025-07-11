package net.trustly.android.sdk.data

import com.google.gson.Gson
import net.trustly.android.sdk.util.TrustlyConstants.INTEGRATION_STRATEGY_DEFAULT
import java.net.URL

class TrustlyService(private val urlFetcher: TrustlyUrlFetcher) {

    fun getSettingsData(baseUrl: String, token: String, settings: (Settings) -> Unit) {
        val settingsResponseDefault = Settings(StrategySetting(INTEGRATION_STRATEGY_DEFAULT))
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
                        settings.invoke(settingsResponseDefault)
                    }
                } catch (_: Exception) {
                    settings.invoke(settingsResponseDefault)
                } finally {
                    it.disconnect()
                }
            }
        }.start()
    }

}