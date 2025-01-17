package net.trustly.android.sdk.data

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APIRequest(private val apiInterface: APIMethod, private val settings: (Settings) -> Unit, private val error: (String) -> Unit) {

    fun getSettingsData(token: String) {
        apiInterface.getSettings(token).enqueue(object : Callback<Settings> {
            override fun onResponse(call: Call<Settings>, response: Response<Settings>) {
                if (response.body() != null) {
                    Log.d(TAG, response.body().toString())
                    settings.invoke(response.body() as Settings)
                } else {
                    onFailure(call, Throwable(response.errorBody().toString()))
                }
            }

            override fun onFailure(call: Call<Settings>, t: Throwable) {
                Log.e(TAG, t.message.toString())
                error.invoke(t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "APIRequest"
    }

}