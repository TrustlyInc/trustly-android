package net.trustly.android.sdk.interfaces

import java.util.HashMap

/**
 * Trustly listener definition
 */
fun interface TrustlyListener {

    /**
     * @param eventName The Trustly event name that triggered the listener
     * @param eventDetails The Trustly event details that triggered the listener
     */
    fun onChange(eventName: String, eventDetails: HashMap<String, String>?)

}