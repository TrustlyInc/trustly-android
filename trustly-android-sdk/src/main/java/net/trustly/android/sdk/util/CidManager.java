package net.trustly.android.sdk.util;

import android.content.Context;

import java.util.Calendar;
import java.util.UUID;

public class CidManager {

    private String SESSION_CID_KEY = "PayWithMyBank.sessionCid";
    private static int EXPIRATION_TIME_LIMIT = 1;

    public String getOrCreateSessionCid(Context context) {
        // Verify if theres a valid CID locally, if not, create one
        String sessionCid = CidStorage.readDataFrom(context);
        if (sessionCid != null) {
            // If the CID is expired, create a new one and replace it
            if (!isValid(sessionCid.split("-")[2])) {
                // Get the CID value from the required parameters
                sessionCid = getFingerPrint() + "-" + getRandomKey() + "-" + getTimestampBase36();
                CidStorage.saveData(context, sessionCid);
            }
        }
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
        return Calendar.getInstance().compareTo(lastTime) > 0;
    }

}
