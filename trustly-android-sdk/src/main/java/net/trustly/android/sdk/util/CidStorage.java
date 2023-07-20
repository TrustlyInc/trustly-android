package net.trustly.android.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;

class CidStorage {

    private static final String CID_STORAGE = "CID_STORAGE";
    private static final String SESSION_CID = "SESSION_CID";

    private CidStorage() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static void saveData(Context context, String sessionCid) {
        getSharedPreferences(context).edit().putString(SESSION_CID, sessionCid).apply();
    }

    public static String readDataFrom(Context context) {
        return getSharedPreferences(context).getString(SESSION_CID, null);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(CID_STORAGE, Context.MODE_PRIVATE);
    }

}
