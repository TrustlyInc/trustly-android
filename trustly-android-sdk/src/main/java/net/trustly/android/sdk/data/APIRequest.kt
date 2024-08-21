package net.trustly.android.sdk.data

import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APIRequest(private val apiInterface: APIMethod, private val settings: (Settings) -> Unit) {

    //TODO Remove this method used to mock information
    fun getSettingsMockData() {
        apiInterface.getSettings("", "", "").enqueue(object : Callback<Settings> {
            override fun onResponse(call: Call<Settings>, response: Response<Settings>) {
                if (response.isSuccessful && response.body() != null) {
                    mockSettingsResult()
                }
            }

            override fun onFailure(call: Call<Settings>, t: Throwable) {
                mockSettingsResult()
            }
        })
    }

    fun getSettingsData(merchantId: String, grp: String, flowType: String) {
        apiInterface.getSettings(merchantId, grp, flowType).enqueue(object : Callback<Settings> {
            override fun onResponse(call: Call<Settings>, response: Response<Settings>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("APIRequestNew", response.body().toString())
                    settings.invoke(response.body() as Settings)
                }
            }

            override fun onFailure(call: Call<Settings>, t: Throwable) {
                Log.e("APIRequestNew", t.message.toString())
            }
        })
    }

    //TODO Remove this method used to mock information
    private fun mockSettingsResult() {
        val output = "{'settings': {'lightbox': {'context': 'web-view'}}}";
        settings.invoke(Gson().fromJson(output, Settings::class.java))
    }

}