package net.trustly.android.sdk.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIMethod {

    //TODO Change this one with the correct endpoint
    @GET("api/v2/breeds")
    fun getSettings(
        @Query("merchantId") merchantId: String,
        @Query("grp") grp: String,
        @Query("metadata.flowtype") flowType: String
    ): Call<Settings>

}