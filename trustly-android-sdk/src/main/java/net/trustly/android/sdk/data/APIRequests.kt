package net.trustly.android.sdk.data

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Suppress("DEPRECATION")
class APIRequests(private val delegate: APIAsyncResponse) : AsyncTask<String?, String?, String?>() {

    companion object {
        private const val TIMEOUT = 5000
    }

    interface APIAsyncResponse {
        fun processFinish(output: String?)
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String?): String? {
        try {
            val connection = URL(params.first()).openConnection() as HttpURLConnection
            connection.apply {
                requestMethod = "GET"
                connectTimeout = TIMEOUT
                readTimeout = TIMEOUT
            }.connect()
            val rd = BufferedReader(InputStreamReader(connection.inputStream))
            val content = StringBuilder()
            var line: String?
            while ((rd.readLine().also { line = it }) != null) {
                content.append(line).append("\n")
            }
            return content.toString()
        } catch (e: Exception) {
            return e.message
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: String?) {
        delegate.processFinish(result)
    }

}
