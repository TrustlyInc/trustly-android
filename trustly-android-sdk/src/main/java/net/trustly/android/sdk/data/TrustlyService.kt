package net.trustly.android.sdk.data

import com.google.gson.Gson
import net.trustly.android.sdk.data.model.Settings
import net.trustly.android.sdk.data.model.StrategySetting
import net.trustly.android.sdk.data.model.Tracking
import net.trustly.android.sdk.util.TrustlyConstants.INTEGRATION_STRATEGY_DEFAULT
import java.net.URL

class TrustlyService(private val urlFetcher: TrustlyUrlFetcher) {

    fun getSettingsData(baseUrl: String, token: String, response: (Settings) -> Unit) {
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
                        response.invoke(apiResponse)
                    } else {
                        response.invoke(defaultResponse)
                    }
                } catch (_: Exception) {
                    response.invoke(defaultResponse)
                } finally {
                    it.disconnect()
                }
            }
        }.start()
    }

    fun postTrackingData(baseUrl: String, tracking: Tracking, response: (Tracking) -> Unit) {
        Thread {
            urlFetcher.let {
                val url = URL("$baseUrl/frontend/mobile/tracking/$tracking")
                it.openConnection(url)
                it.setRequestProperty("Content-type", "application/json")
                it.setRequestMethod("POST")
                it.setTimeOut(10000)
                try {
                    if (it.isUrlAvailable()) {
                        val apiResponse = Gson().fromJson(it.getResponse(), Tracking::class.java)
                        response.invoke(apiResponse)
                    } else {
                        response.invoke(tracking)
                    }
                } catch (_: Exception) {
                    response.invoke(tracking)
                } finally {
                    it.disconnect()
                }
            }
        }.start()
    }

}