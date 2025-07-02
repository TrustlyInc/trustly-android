package net.trustly.android.sdk.data

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APIRequest(private val apiInterface: APIMethod) {

    companion object {
        const val TAG = "APIRequest"
    }

    fun getSettingsData(token: String, settings: (Settings) -> Unit, error: (String) -> Unit) {
        apiInterface.getSettings(token).enqueue(object : Callback<Settings> {
            override fun onResponse(call: Call<Settings>, response: Response<Settings>) {
                if (response.body() != null) {
                    Log.d(TAG, response.body().toString())
                    settings.invoke(response.body() as Settings)
                }
            }

            override fun onFailure(call: Call<Settings>, t: Throwable) {
                Log.e(TAG, t.message.toString())
                error.invoke(t.message.toString())
            }
        })
    }

}