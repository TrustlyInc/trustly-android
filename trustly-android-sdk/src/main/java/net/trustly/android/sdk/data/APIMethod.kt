package net.trustly.android.sdk.data

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface APIMethod {

    @GET("frontend/mobile/setup")
    fun getSettings(
        @Query("token") token: String
    ): Call<Settings>

    @POST
    @Headers(
        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Content-Type: application/x-www-form-urlencoded"
    )
    fun postLightboxUrl(
        @Header("User-Agent") userAgent: String, @Body body: RequestBody
    ): Call<String>

}