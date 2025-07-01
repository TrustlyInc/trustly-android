package net.trustly.android.sdk.data

import android.util.Log
import okhttp3.RequestBody
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

    fun postLightboxUrl(userAgent: String, body: RequestBody, lightboxUrl: (String) -> Unit, error: (String) -> Unit) {
        apiInterface.postLightboxUrl(userAgent, body).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.body() != null) {
                    Log.d(TAG, response.body().toString())
                    lightboxUrl.invoke(response.body() as String)
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e(TAG, t.message.toString())
                error.invoke(t.message.toString())
            }
        })
    }

}