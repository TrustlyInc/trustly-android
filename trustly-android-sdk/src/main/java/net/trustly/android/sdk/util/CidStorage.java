package net.trustly.android.sdk.util;

import android.content.Context;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class CidStorage {

    private static final String CID_STORAGE = "CID_STORAGE";
    private static final String SESSION_CID = "SESSION_CID";

    public static void saveData(Context context, String sessionCid) {
        getSharedPreferences(context).edit().putString(SESSION_CID, sessionCid).apply();
    }

    public static String readDataFrom(Context context) {
        return getSharedPreferences(context).getString(SESSION_CID, null);
    }

    private static EncryptedSharedPreferences getSharedPreferences(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            return (EncryptedSharedPreferences) EncryptedSharedPreferences.create(context, CID_STORAGE, masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
