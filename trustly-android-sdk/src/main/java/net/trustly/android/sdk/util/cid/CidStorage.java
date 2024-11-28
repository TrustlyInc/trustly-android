package net.trustly.android.sdk.util.cid;

import android.content.Context;
import android.content.SharedPreferences;

public class CidStorage {

    private static final String CID_STORAGE = "CID_STORAGE";
    public static final String SESSION_CID = "SESSION_CID";
    public static final String CID = "CID";

    public CidStorage() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static void saveData(Context context, String preferenceId, String preferenceValue) {
        getSharedPreferences(context).edit().putString(preferenceId, preferenceValue).apply();
    }

    public static String readDataFrom(Context context, String preferenceId) {
        return getSharedPreferences(context).getString(preferenceId, null);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(CID_STORAGE, Context.MODE_PRIVATE);
    }

}
