package net.trustly.android.sdk.interfaces;

import java.util.HashMap;

public interface TrustlyListener {
    void onChange(String eventName, HashMap<String, String> eventDetails);
}