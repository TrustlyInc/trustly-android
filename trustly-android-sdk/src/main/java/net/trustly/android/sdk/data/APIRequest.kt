package net.trustly.android.sdk.data

import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APIRequest(private val settings: (Settings) -> Unit) {

    private val apiInterface = RetrofitInstance.getInstance().create(APIInterface::class.java)

    fun getSettingsData() {
        apiInterface.getSettings().enqueue(object : Callback<Settings> {
            override fun onResponse(call: Call<Settings>, response: Response<Settings>) {
                if (response.isSuccessful && response.body() != null) {
                    //TODO Uncomment this lines
//                    Log.d("APIRequestNew", response.body().toString())
//                    settings.invoke(response.body() as Settings)

                    //TODO Remove this lines used to mock information
                    mockSettingsResult()
                }
            }

            override fun onFailure(call: Call<Settings>, t: Throwable) {
                Log.e("APIRequestNew", t.message.toString())

                //TODO Remove this lines used to mock information
                mockSettingsResult()
            }
        })
    }

    private fun mockSettingsResult() {
        //TODO Remove this lines used to mock information
        val output = "{'settings': {'lightbox': {'context': 'in-app-browser'}}}";
        settings.invoke(Gson().fromJson(output, Settings::class.java))
    }

}