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
package com.paywithmybank.android.sdk.interfaces;

import android.content.Context;

import com.paywithmybank.android.sdk.views.PayWithMyBankView;

import java.util.Map;

/**
 * PayWithMyBank interface defines the PayWithMyBank's SDK functionality
 *
 * @see PayWithMyBankView
 */
public interface PayWithMyBank {

    class instance {
        /**
         * Creates a PayWithMyBankView instance that implements PayWithMyBank interface
         *
         * @param context Interface to global information about an application environment.
         * @return The PayWithMyBankView created instance
         */
        public static PayWithMyBankView create(Context context) {
            return new PayWithMyBankView(context);
        }
    }

    /**
     * Shows the select bank widget on the PayWithMyBankView
     *
     * @param establishData The map containing the establish data. Similar to the establishData object
     *                      on the JavaScript API.
     * @return this
     */
    PayWithMyBank selectBankWidget(Map<String, String> establishData);

    /**
     * Sets the callback handler for when the user selects a bank on the widget.
     *
     * @param handler Called when the user selects a bank on the widget.
     *                The callback returnParameters map is the clone of the establishData with an additional entry
     *                for the key "paymentProviderId" with the id of the selected bank.
     * @return this
     */
    PayWithMyBank onBankSelected(PayWithMyBankCallback<PayWithMyBank, Map<String, String>> handler);

    /**
     * Establishes a new transaction. Shows the PayWithMyBank bank authentication on the PayWithMyBankView
     *
     * @param establishData The map containing the establish data. Similar to the establishData object on the
     *                      JavaScript API.
     * @return this
     */
    PayWithMyBank establish(Map<String, String> establishData);

    /**
     * Opens a hybrid application page
     *
     * @param url
     * @param returnUrl
     * @param cancelUrl
     * @return this
     */
    PayWithMyBank hybrid(String url, String returnUrl, String cancelUrl);

    /**
     * Sets a callback to handle when the user flow finishes
     *
     * @param onReturnHandler Called when the user flow finishes and control is returned to the application.
     *                        The returnParameters has as entries the same parameters of the returnURL on the web
     *                        (check the definition on the Integration Guide).
     * @return this
     */
    PayWithMyBank onReturn(PayWithMyBankCallback<PayWithMyBank, Map<String, String>> onReturnHandler);

    /**
     * Sets a callback to handle user cancellation of the flow.
     *
     * @param handler Called when the user cancels the flow finishes and the control is passed back to the application.
     *                The returnParameters has as entries the same parameters of the returnURL on the web (check the
     *                definition on the Integration Guide).
     * @return this
     */
    PayWithMyBank onCancel(PayWithMyBankCallback<PayWithMyBank, Map<String, String>> handler);

    /**
     * Sets a callback to handle external URLs
     *
     * @param handler Called when the PayWithMyBank panel must open an external URL. If not handled an internal in
     *                app WebView will show the external URL. The external URL is sent on the returnParameters
     *                entry key "url".
     * @return this
     */
    PayWithMyBank onExternalUrl(PayWithMyBankCallback<PayWithMyBank, Map<String, String>> handler);

    /**
     * @param verifyData The map containing the verify data.
     * @return this
     * @deprecated Use establish with paymentType=Verify
     */
    PayWithMyBank verify(Map<String, String> verifyData);

    /**
     * Sets a listener to capture internal payment flow events
     *
     * @param payWithMyBankListener The object that will receive the notifications
     * @return this
     */
    PayWithMyBank setListener(PayWithMyBankListener payWithMyBankListener);

    /**
     * Destroy PayWithMyBank WebView
     *
     * @return this
     */
    PayWithMyBank destroy();

    /**
     * This method call a function in order for the Lightbox to proceed with the transaction
     */
    void proceedToChooseAccount();
}
