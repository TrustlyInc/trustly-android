package net.trustly.android.sdk.data

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class TrustlyUrlFetcher {

    private lateinit var connection: HttpURLConnection

    fun getResponseCode() = connection.responseCode

    fun isUrlAvailable() = getResponseCode() == HttpURLConnection.HTTP_OK

    fun isUrlRedirect() = getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP

    fun openConnection(url: URL) {
        connection = url.openConnection() as HttpURLConnection
    }

    fun setRequestMethod(method: Method) {
        connection.requestMethod = method.name
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

    fun addHeader(key: String, value: String) {
        connection.setRequestProperty(key, value)
    }

    fun setDoInput(doInput: Boolean) {
        connection.doInput = doInput
    }

    fun setDoOutput(doOutput: Boolean) {
        connection.doOutput = doOutput
    }

    fun addBody(encodedParameters: ByteArray) {
        val outputStream = DataOutputStream(connection.outputStream)
        outputStream.write(encodedParameters)
        outputStream.close()
    }

    fun setInstanceFollowRedirects(followRedirects: Boolean) {
        connection.instanceFollowRedirects = followRedirects
    }

    fun getHeaderField(field: String): String = connection.getHeaderField(field);

    enum class Method {
        GET, POST
    }

}