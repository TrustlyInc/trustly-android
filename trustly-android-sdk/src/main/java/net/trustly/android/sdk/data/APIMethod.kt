package net.trustly.android.sdk.data

import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

fun interface APIMethod {

    @GET("frontend/mobile/setup")
    fun getSettings(
        @Query("token") token: String
    ): Call<Settings>

}