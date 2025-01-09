package net.trustly.android.sdk.util.cid

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import net.trustly.android.sdk.util.cid.CidStorage.readDataFrom
import net.trustly.android.sdk.util.cid.CidStorage.saveData
import java.util.Calendar
import java.util.Locale
import java.util.UUID

object CidManager {

    private const val EXPIRATION_TIME_LIMIT: Int = 1

    const val CID_PARAM: String = "CID"
    const val SESSION_CID_PARAM: String = "SESSION_CID"
    private const val DIVIDER: String = "-"

    fun generateCid(context: Context) {
        saveData(context, CidStorage.CID, generateNewSession(context))
    }

    fun getOrCreateSessionCid(context: Context): Map<String, String?> {
        val cid = readDataFrom(context, CidStorage.CID)
        var sessionCid = readDataFrom(context, CidStorage.SESSION_CID)
        if (sessionCid == null) sessionCid = cid
        else {
            val split = sessionCid.split(DIVIDER)
            if (split.size > 2 && !isValid(split[2]))
                sessionCid = generateNewSession(context)
        }

        saveData(context, CidStorage.SESSION_CID, sessionCid)

        return hashMapOf(CID_PARAM to cid, SESSION_CID_PARAM to sessionCid)
    }

    private fun generateNewSession(context: Context) =
        getFingerPrint(context) + DIVIDER + getRandomKey() + DIVIDER + getTimestampBase36()

    private fun getUUID() = UUID.randomUUID()

    @SuppressLint("HardwareIds")
    private fun getFingerPrint(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ).substring(0, 4).uppercase(Locale.getDefault())
    }

    private fun getRandomKey() =
        getUUID().toString().split(DIVIDER.toRegex())[2].uppercase(Locale.getDefault())

    private fun getTimestampBase36() =
        Calendar.getInstance().timeInMillis.toString(36).uppercase(Locale.getDefault())

    private fun isValid(timestamp: String): Boolean {
        val lastTime = Calendar.getInstance()
        lastTime.timeInMillis = timestamp.toLong(36)
        return hoursAgo(lastTime) < EXPIRATION_TIME_LIMIT
    }

    private fun hoursAgo(datetime: Calendar): Int {
        val now = Calendar.getInstance()
        val differenceInMillis = now.timeInMillis - datetime.timeInMillis
        val differenceInHours = (differenceInMillis) / (1000L * 60L * 60L)
        return differenceInHours.toInt()
    }

}