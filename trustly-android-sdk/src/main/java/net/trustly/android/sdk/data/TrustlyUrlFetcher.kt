package net.trustly.android.sdk.data

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class TrustlyUrlFetcher {

    private lateinit var connection: HttpURLConnection

    fun getResponseCode() = connection.responseCode

    fun isUrlAvailable() = getResponseCode() == HttpURLConnection.HTTP_OK

    fun openConnection(url: URL) {
        connection = url.openConnection() as HttpURLConnection
    }

    fun setRequestMethod(method: String) {
        connection.requestMethod = method
    }

    fun setTimeOut(timeout: Int) {
        connection.connectTimeout = timeout
        connection.readTimeout = timeout
    }

    fun getResponse(): String {
        val inputStream = InputStreamReader(connection.inputStream)
        val reader = BufferedReader(inputStream)
        val response = reader.readText()
        reader.close()
        return response
    }

    fun getErrorResponse(): String = connection.responseMessage

    fun disconnect() {
        connection.disconnect()
    }

}