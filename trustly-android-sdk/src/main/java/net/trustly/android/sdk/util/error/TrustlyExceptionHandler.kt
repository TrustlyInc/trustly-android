package net.trustly.android.sdk.util.error

import android.os.Build
import android.util.Log
import net.trustly.android.sdk.BuildConfig
import java.io.PrintWriter
import java.io.StringWriter
import java.util.Calendar

class TrustlyExceptionHandler : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        if (exception.stackTraceToString().contains(BuildConfig.LIBRARY_PACKAGE_NAME)) {
            val errorMessage = StringBuilder()
            errorMessage.apply {
                append(NEW_LINE)
                append("Trustly SDK Version: ")
                append(BuildConfig.SDK_VERSION)
                append(NEW_LINE)
                append("Android SDK Version: ")
                append(Build.VERSION.SDK_INT)
                append(NEW_LINE)
                append("Android Version Release: ")
                append(Build.VERSION.RELEASE)
                append(NEW_LINE)
                append("Device Model: ")
                append(Build.MODEL)
                append(NEW_LINE)
                append("Date: ")
                append(Calendar.getInstance().time)
                append(NEW_LINE)
                val stackTrace = StringWriter()
                exception.printStackTrace(PrintWriter(stackTrace))
                append(stackTrace.toString())
            }

            Log.e(TAG, errorMessage.toString())
        }
    }

    companion object {

        private const val TAG = "TrustlyExceptionHandler"
        private const val NEW_LINE = "\n"

    }

}