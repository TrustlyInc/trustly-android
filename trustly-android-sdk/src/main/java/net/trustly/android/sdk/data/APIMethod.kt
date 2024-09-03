package net.trustly.android.sdk.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIMethod {

    @GET("frontend/mobile/setup")
    fun getSettings(
        @Query("token") token: String
    ): Call<Settings>

}