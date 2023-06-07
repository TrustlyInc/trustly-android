package net.trustly.android.sdk.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @deprecated Use TrustlyView
 */
@Deprecated
public class TrustlyPanel extends TrustlyView {

    public TrustlyPanel(Context context) {
        super(context, null, 0, null);
    }

    public TrustlyPanel(Context context, String env) {
        super(context, null, 0, null);
    }

    public TrustlyPanel(Context context, AttributeSet attrs) {
        super(context, attrs, 0, null);
    }

    public TrustlyPanel(Context context, AttributeSet attrs, String env) {
        super(context, attrs, 0, env);
    }

    public TrustlyPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, null);
    }

    public TrustlyPanel(Context context, AttributeSet attrs, int defStyleAttr, String env) {
        super(context, attrs, defStyleAttr, env);
    }
}
