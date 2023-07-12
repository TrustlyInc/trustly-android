package net.trustly.android.sdk.util;

import android.content.Context;

import java.util.Calendar;
import java.util.UUID;

public class CidManager {

    private static final int EXPIRATION_TIME_LIMIT = 1;

    public String getOrCreateSessionCid(Context context) {
        String sessionCid = CidStorage.readDataFrom(context);
        if (sessionCid == null || !isValid(sessionCid.split("-")[2])) {
            sessionCid = generateNewSession(context);
        }
        return sessionCid;
    }

    private String generateNewSession(Context context) {
        String sessionCid = getFingerPrint() + "-" + getRandomKey() + "-" + getTimestampBase36();
        CidStorage.saveData(context, sessionCid);
        return sessionCid;
    }

    private UUID getUUID() {
        return UUID.randomUUID();
    }

    private String getFingerPrint() {
        return getUUID().toString().split("-")[1].toUpperCase();
    }

    private String getRandomKey() {
        return getUUID().toString().split("-")[2].toUpperCase();
    }

    private String getTimestampBase36() {
        return Long.toString(Calendar.getInstance().getTimeInMillis(), 36).toUpperCase();
    }

    private boolean isValid(String timestamp) {
        Calendar lastTime = Calendar.getInstance();
        lastTime.setTimeInMillis(Long.parseLong(timestamp, 36));
        return hoursAgo(lastTime) <= EXPIRATION_TIME_LIMIT;
    }

    public static int hoursAgo(Calendar datetime) {
        Calendar now = Calendar.getInstance(); // Get time now
        long differenceInMillis = now.getTimeInMillis() - datetime.getTimeInMillis();
        long differenceInHours = (differenceInMillis) / 1000L / 60L / 60L;
        return (int) differenceInHours;
    }

}