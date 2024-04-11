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

    private final TrustlyView trustlyView;

    public TrustlyJsInterface(TrustlyView trustlyView) {
        this.trustlyView = trustlyView;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        if (trustlyView == null) return;

        if (message == null || message.trim().isEmpty()) return;

        String[] params = message.split("\\|");
        if (params.length == 0) return;

        String command = params[0];
        if (command == null || command.trim().isEmpty()) return;

        if (command.equalsIgnoreCase("PayWithMyBank.event")) {
            HashMap<String, String> eventDetails = new HashMap<>();

            for (Map.Entry<Integer, String> entry : getEventNames().entrySet()) {
                addToListenerDetails(params, entry.getKey(), entry.getValue(), eventDetails);
            }

            String eventName = "event";
            trustlyView.notifyListener(eventName, eventDetails);
        }
    }

    @JavascriptInterface
    public void resize(final float width, final float height) {
        new Handler(Looper.getMainLooper()).post(() -> {
            DisplayMetrics displayMetrics = trustlyView.getContext().getResources().getDisplayMetrics();
            float widthPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, displayMetrics);
            float heightPixels = 0.0F;
            if (height != heightPixels) {
                heightPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, displayMetrics);
            } else {
                heightPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width * 1.75F, displayMetrics);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) widthPixels, (int) heightPixels);
            trustlyView.setLayoutParams(params);
        });
    }

    protected void addToListenerDetails(String[] params, int index, String eventName, HashMap<String, String> eventDetails) {
        if (eventDetails == null || eventName == null || params == null || index >= params.length) {
            return;
        }

        String value = params[index];
        if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("null")) {
            return;
        }

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