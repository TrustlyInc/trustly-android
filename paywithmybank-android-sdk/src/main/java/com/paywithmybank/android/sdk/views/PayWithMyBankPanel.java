/*  ___________________________________________________________________________________________________________
 *
 *    TRUSTLY CONFIDENTIAL AND PROPRIETARY INFORMATION
 *  ___________________________________________________________________________________________________________
 *
 *      Copyright (c) 2012 - 2020 Trustly
 *      All Rights Reserved.
 *
 *   NOTICE:  All information contained herein is, and remains, the confidential and proprietary property of
 *   Trustly and its suppliers, if any. The intellectual and technical concepts contained herein are the
 *   confidential and proprietary property of Trustly and its suppliers and  may be covered by U.S. and
 *   Foreign Patents, patents in process, and are protected by trade secret or copyright law. Dissemination of
 *   this information or reproduction of this material is strictly forbidden unless prior written permission is
 *   obtained from Trustly.
 *   ___________________________________________________________________________________________________________
 */
package com.paywithmybank.android.sdk.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @deprecated Use PayWithMyBankView
 */
@Deprecated
public class PayWithMyBankPanel extends PayWithMyBankView {

    public PayWithMyBankPanel(Context context) {
        super(context, null, 0, null);
    }

    public PayWithMyBankPanel(Context context, String env) {
        super(context, null, 0, null);
    }

    public PayWithMyBankPanel(Context context, AttributeSet attrs) {
        super(context, attrs, 0, null);
    }

    public PayWithMyBankPanel(Context context, AttributeSet attrs, String env) {
        super(context, attrs, 0, env);
    }

    public PayWithMyBankPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, null);
    }

    public PayWithMyBankPanel(Context context, AttributeSet attrs, int defStyleAttr, String env) {
        super(context, attrs, defStyleAttr, env);
    }
}
