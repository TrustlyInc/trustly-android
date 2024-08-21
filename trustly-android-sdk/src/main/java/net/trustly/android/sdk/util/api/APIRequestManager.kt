package net.trustly.android.sdk.util.api

import android.content.Context
import java.util.Calendar

object APIRequestManager {

    private const val API_REQUEST = "API_REQUEST"

    fun saveAPIRequest(context: Context) {
        APIRequestStorage.saveData(context, API_REQUEST, getTimestamp())
    }

    fun getAPIRequest(context: Context): String? {
        return APIRequestStorage.readDataFrom(context, API_REQUEST)
    }

    private fun getTimestamp(): String {
        return Calendar.getInstance().timeInMillis.toString()
    }

}