package net.trustly.android.sdk;

import android.app.Application;

import net.trustly.android.sdk.util.CidManager;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new CidManager().generateCid(getApplicationContext());
    }

}