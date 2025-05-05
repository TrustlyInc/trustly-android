package net.trustly.android.sdk.util.error

import android.os.Build
import android.util.Log
import net.trustly.android.sdk.BuildConfig
import net.trustly.android.sdk.data.TrustlyService
import net.trustly.android.sdk.data.TrustlyUrlFetcher
import net.trustly.android.sdk.data.model.Tracking
import java.io.PrintWriter
import java.io.StringWriter
import java.util.Calendar

class TrustlyExceptionHandler : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        val stackTrace = StringWriter()
        exception.printStackTrace(PrintWriter(stackTrace))
        val tracking = Tracking(
            BuildConfig.SDK_VERSION,
            "Android",
            Build.VERSION.SDK_INT.toString(),
            Build.MODEL,
            Calendar.getInstance().time.toString(),
            "error",
            stackTrace.toString()
        )
        logErrorTracking(tracking)
        sendErrorTrackingData(tracking)
    }

    private fun logErrorTracking(tracking: Tracking) {
        val errorMessage = StringBuilder()
        errorMessage.apply {
            append(NEW_LINE)
            append("Trustly SDK Version: ")
            append(tracking.trustlySdkVersion)
            append(NEW_LINE)
            append("Android SDK Version: ")
            append(tracking.deviceVersion)
            append(NEW_LINE)
            append("Android Version Release: ")
            append(Build.VERSION.RELEASE)
            append(NEW_LINE)
            append("Device Model: ")
            append(tracking.deviceModel)
            append(NEW_LINE)
            append("Date: ")
            append(tracking.createdAt)
            append(NEW_LINE)
            append(tracking.message)
        }

        Log.e(TAG, errorMessage.toString())
    }

    private fun sendErrorTrackingData(tracking: Tracking) {
        TrustlyService(TrustlyUrlFetcher()).postTrackingData("url", tracking) {
            Log.e(TAG, it.toString())
        }
    }

    companion object {

        private const val TAG = "TrustlyExceptionHandler"
        private const val NEW_LINE = "\n"

    }

}