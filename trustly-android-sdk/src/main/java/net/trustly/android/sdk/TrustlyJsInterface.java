package net.trustly.android.sdk;

import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;

import net.trustly.android.sdk.views.TrustlyView;

import java.util.HashMap;
import java.util.Map;

public class TrustlyJsInterface {

    private static final String PAYWITHMYBANK_EVENT = "PayWithMyBank.event";
    private static final String EVENT = "event";
    private static final String NULL_VALUE = "null";
    private static final String PARAMS_DIVIDER = "\\|";

    private final TrustlyView trustlyView;

    public TrustlyJsInterface(TrustlyView trustlyView) {
        this.trustlyView = trustlyView;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        if (trustlyView == null || message == null || message.trim().isEmpty()) return;

        String[] params = message.split(PARAMS_DIVIDER);
        if (params.length == 0) return;

        String command = params[0];
        if (command.trim().isEmpty()) return;

        if (command.equalsIgnoreCase(PAYWITHMYBANK_EVENT)) {
            HashMap<String, String> eventDetails = new HashMap<>();
            for (Map.Entry<Integer, String> entry : getEventNames().entrySet()) {
                addToListenerDetails(params, entry.getKey(), entry.getValue(), eventDetails);
            }
            trustlyView.notifyListener(EVENT, eventDetails);
        }
    }

    @JavascriptInterface
    public void resize(final float width, final float height) {
        new Handler(Looper.getMainLooper()).post(() -> {
            DisplayMetrics displayMetrics = trustlyView.getContext().getResources().getDisplayMetrics();
            float widthPixels = applyDimension(width, displayMetrics);
            float heightPixels = 0.0F;
            if (height != heightPixels) {
                heightPixels = applyDimension(height, displayMetrics);
            } else {
                heightPixels = applyDimension(width * 1.75F, displayMetrics);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) widthPixels, (int) heightPixels);
            trustlyView.setLayoutParams(params);
        });
    }

    private float applyDimension(float value, DisplayMetrics displayMetrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics);
    }

    protected void addToListenerDetails(String[] params, int index, String eventName, HashMap<String, String> eventDetails) {
        if (eventDetails == null || eventName == null || params == null || index >= params.length) return;
        String value = params[index];
        if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase(NULL_VALUE)) return;
        eventDetails.put(eventName, params[index]);
    }

    private Map<Integer, String> getEventNames() {
        Map<Integer, String> eventNames = new HashMap<>();
        eventNames.put(2, "page");
        eventNames.put(3, "transactionId");
        eventNames.put(4, "merchantReference");
        eventNames.put(5, "type");
        eventNames.put(6, "data");
        eventNames.put(7, "transfer");
        return eventNames;
    }

}