package net.trustly.android.sdk.interfaces;

import java.util.HashMap;

/**
 * Trustly listener definition
 */
public interface TrustlyListener {

    /**
     * @param eventName The Trustly event name that triggered the listener
     * @param eventDetails The Trustly event details that triggered the listener
     */
    void onChange(String eventName, HashMap<String, String> eventDetails);
}