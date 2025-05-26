package net.trustly.android.sdk.data

import com.google.gson.Gson
import net.trustly.android.sdk.util.TrustlyConstants.INTEGRATION_STRATEGY_DEFAULT
import java.net.URL

class TrustlyService(private val urlFetcher: TrustlyUrlFetcher, private val settings: (Settings) -> Unit) {

    fun getSettingsData(baseUrl: String, token: String) {
        Thread {
            urlFetcher.let {
                val url = URL("$baseUrl/frontend/mobile/setup?token=$token")
                it.openConnection(url)
                it.setRequestMethod("GET")
                it.setTimeOut(10000)
                try {
                    if (it.isUrlAvailable()) {
                        val apiResponse = Gson().fromJson(it.getResponse(), Settings::class.java)
                        handleSuccessResponse(apiResponse)
                    } else {
                        handleErrorResponse()
                    }
                } catch (_: Exception) {
                    handleErrorResponse()
                } finally {
                    it.disconnect()
                }
            }
        }.start()
    }

    private fun handleSuccessResponse(response: Settings) {
        settings.invoke(response)
    }

    private fun handleErrorResponse() {
        settings.invoke(Settings(StrategySetting(INTEGRATION_STRATEGY_DEFAULT)))
    }

}