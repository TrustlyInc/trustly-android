package net.trustly.android.sdk.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CidManager {

    private static final int EXPIRATION_TIME_LIMIT = 1;

    public static final String CID_PARAM = "CID";
    public static final String SESSION_CID_PARAM = "SESSION_CID";

    private CidManager() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static void generateCid(Context context) {
        CidStorage.saveData(context, CidStorage.CID, generateNewSession(context));
    }

    public static Map<String, String> getOrCreateSessionCid(Context context) {
        String cid = CidStorage.readDataFrom(context, CidStorage.CID);
        String sessionCid = CidStorage.readDataFrom(context, CidStorage.SESSION_CID);
        if (sessionCid == null) {
            sessionCid = cid;
        } else if (!isValid(sessionCid.split("-")[2])) {
            sessionCid = generateNewSession(context);
        }

        CidStorage.saveData(context, CidStorage.SESSION_CID, sessionCid);

        Map<String, String> values = new HashMap<>();
        values.put(CID_PARAM, cid);
        values.put(SESSION_CID_PARAM, sessionCid);
        return values;
    }

    private static String generateNewSession(Context context) {
        return getFingerPrint(context) + "-" + getRandomKey() + "-" + getTimestampBase36();
    }

    private static UUID getUUID() {
        return UUID.randomUUID();
    }

    @SuppressLint("HardwareIds")
    private static String getFingerPrint(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                                  Settings.Secure.ANDROID_ID).substring(0, 4).toUpperCase();
    }

    private static String getRandomKey() {
        return getUUID().toString()
                .split("-")[2].toUpperCase();
    }

    private static String getTimestampBase36() {
        return Long.toString(Calendar.getInstance()
                                     .getTimeInMillis(), 36)
                .toUpperCase();
    }

    private static boolean isValid(String timestamp) {
        Calendar lastTime = Calendar.getInstance();
        lastTime.setTimeInMillis(Long.parseLong(timestamp, 36));
        return hoursAgo(lastTime) < EXPIRATION_TIME_LIMIT;
    }

    public static int hoursAgo(Calendar datetime) {
        Calendar now = Calendar.getInstance();
        long differenceInMillis = now.getTimeInMillis() - datetime.getTimeInMillis();
        long differenceInHours = (differenceInMillis) / (1000L * 60L * 60L);
        return (int) differenceInHours;
    }

}