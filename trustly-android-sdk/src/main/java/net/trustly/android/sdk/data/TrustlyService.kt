package net.trustly.android.sdk.data

import android.util.Log
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
                it.setRequestMethod(TrustlyUrlFetcher.Method.GET)
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

    fun postLightboxUrl(baseUrl: String, userAgentString: String, encodedParameters: ByteArray, lightboxUrl: (String?) -> Unit) {
        Thread {
            urlFetcher.let {
                Log.d("TrustlyService", baseUrl)
                val url = URL(baseUrl)
                it.openConnection(url)
                it.setRequestMethod(TrustlyUrlFetcher.Method.POST)
                it.setTimeOut(10000)
                it.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                it.addHeader("Content-Type", "application/x-www-form-urlencoded")
                it.addHeader("User-Agent", userAgentString)
                it.setDoOutput(true)
                it.addBody(encodedParameters)
                try {
                    Log.d("TrustlyServiceUrlAva", it.isUrlAvailable().toString())
                    if (it.isUrlAvailable()) {
                        Log.d("TrustlyServiceTrue", it.getResponse())
                        val apiResponse = Gson().fromJson(it.getResponse(), String::class.java)
                        lightboxUrl.invoke(apiResponse)
                    } else {
                        Log.d("TrustlyServiceFalse", it.getErrorResponse())
                        lightboxUrl.invoke(null)
                    }
                } catch (e: Exception) {
                    Log.d("TrustlyServiceException", e.message.toString())
                    lightboxUrl.invoke(null)
                } finally {
                    it.disconnect()
                }
            }
        }.start()
    }

}