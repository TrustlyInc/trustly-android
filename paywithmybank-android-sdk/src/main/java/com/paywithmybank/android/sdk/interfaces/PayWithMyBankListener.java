package com.paywithmybank.android.sdk.interfaces;

import java.util.HashMap;

public interface PayWithMyBankListener {
    void onChange(String eventName, HashMap<String, String> eventDetails);
}