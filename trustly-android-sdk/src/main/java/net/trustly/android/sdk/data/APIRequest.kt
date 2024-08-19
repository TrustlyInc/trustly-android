package net.trustly.android.sdk.data

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APIRequest(private val apiInterface: APIMethod, private val settings: (Settings) -> Unit) {

    fun getSettingsData(merchantId: String, grp: String, flowType: String) {
        apiInterface.getSettings(merchantId, grp, flowType).enqueue(object : Callback<Settings> {
            override fun onResponse(call: Call<Settings>, response: Response<Settings>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("APIRequest", response.body().toString())
                    settings.invoke(response.body() as Settings)
                }
            }

            override fun onFailure(call: Call<Settings>, t: Throwable) {
                Log.e("APIRequest", t.message.toString())
            }
        })
    }

}