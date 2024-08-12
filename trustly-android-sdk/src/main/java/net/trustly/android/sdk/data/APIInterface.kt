package net.trustly.android.sdk.data

import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {

    //TODO Change this one with the correct endpoint
    @GET("api/v2/breeds")
    fun getSettings(): Call<Settings>

}