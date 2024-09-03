package net.trustly.android.sdk.util.api

import android.content.Context
import com.google.gson.Gson
import net.trustly.android.sdk.data.Settings
import net.trustly.android.sdk.data.StrategySetting
import java.util.Calendar
import java.util.concurrent.TimeUnit

object APIRequestManager {

    private const val API_REQUEST = "API_REQUEST"
    private const val API_REQUEST_SETTINGS = "API_REQUEST_SETTINGS"
    private const val EXPIRATION_TIME_LIMIT_MINUTES = 15

    fun validateAPIRequest(context: Context): Boolean {
        val apiRequest = getAPIRequest(context)
        if (apiRequest == null || !isValid(apiRequest)) {
            APIRequestStorage.saveData(context, API_REQUEST, getTimestamp())
            return false
        }
        return true
    }

    fun getAPIRequest(context: Context): String? {
        return APIRequestStorage.readDataFrom(context, API_REQUEST)
    }

    fun saveAPIRequestSettings(context: Context, settings: Settings) {
        APIRequestStorage.saveData(context, API_REQUEST_SETTINGS, Gson().toJson(settings))
    }

    fun getAPIRequestSettings(context: Context): Settings {
        val settings = Gson().fromJson(APIRequestStorage.readDataFrom(context, API_REQUEST_SETTINGS), Settings::class.java)
        return settings ?: Settings(StrategySetting("webview"))
    }

    private fun getTimestamp(): String {
        return Calendar.getInstance().timeInMillis.toString()
    }

    private fun isValid(timestamp: String): Boolean {
        val lastTime = Calendar.getInstance()
        lastTime.timeInMillis = timestamp.toLong()
        return minutesAgo(lastTime) < EXPIRATION_TIME_LIMIT_MINUTES
    }

    private fun minutesAgo(datetime: Calendar): Int {
        val now = Calendar.getInstance()
        val differenceInMillis = now.timeInMillis - datetime.timeInMillis
        return TimeUnit.MILLISECONDS.toMinutes(differenceInMillis).toInt()
    }

}