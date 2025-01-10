package net.trustly.android.sdk.interfaces

/**
 * Trustly interface defines the Trustly's SDK functionality
 *
 * @see TrustlyView
 */
interface Trustly {

    /**
     * Shows the select bank widget on the TrustlyView
     *
     * @param establishData The map containing the establish data. Similar to the establishData object
     * on the JavaScript API.
     * @return this
     */
    fun selectBankWidget(establishData: Map<String, String>?): Trustly

    /**
     * Sets the callback handler for when the user selects a bank on the widget.
     *
     * @param handler Called when the user selects a bank on the widget.
     * The callback returnParameters map is the clone of the establishData with an additional entry
     * for the key "paymentProviderId" with the id of the selected bank.
     * @return this
     */
    fun onBankSelected(handler: TrustlyCallback<Trustly, Map<String, String>>?): Trustly

    /**
     * Establishes a new transaction. Shows the Trustly bank authentication on the TrustlyView
     *
     * @param establishData The map containing the establish data. Similar to the establishData object on the
     * JavaScript API.
     * @return this
     */
    fun establish(establishData: Map<String, String>?): Trustly

    /**
     * Opens a hybrid application page
     *
     * @param url Url that open a hybrid application page
     * @param returnUrl The gateway redirects the customer browser to this URL if the customer authorizes payment
     * (must be a valid URL or function).
     * @param cancelUrl The gateway redirects the customer browser to this URL if the customer cancels payment
     * (must be a valid URL or function).
     * @return this
     */
    fun hybrid(url: String?, returnUrl: String?, cancelUrl: String?): Trustly

    /**
     * Sets a callback to handle when the user flow finishes
     *
     * @param onReturnHandler Called when the user flow finishes and control is returned to the application.
     * The returnParameters has as entries the same parameters of the returnURL on the web
     * (check the definition on the Integration Guide).
     * @return this
     */
    fun onReturn(onReturnHandler: TrustlyCallback<Trustly, Map<String, String>>?): Trustly

    /**
     * Sets a callback to handle user cancellation of the flow.
     *
     * @param handler Called when the user cancels the flow finishes and the control is passed back to the application.
     * The returnParameters has as entries the same parameters of the returnURL on the web (check the
     * definition on the Integration Guide).
     * @return this
     */
    fun onCancel(handler: TrustlyCallback<Trustly, Map<String, String>>?): Trustly

    /**
     * Sets a callback to handle external URLs
     *
     * @param handler Called when the Trustly panel must open an external URL. If not handled an internal in
     * app WebView will show the external URL. The external URL is sent on the returnParameters
     * entry key "url".
     * @return this
     */
    fun onExternalUrl(handler: TrustlyCallback<Trustly, Map<String, String>>?): Trustly

    /**
     * Sets a listener to capture internal payment flow events
     *
     * @param trustlyListener The object that will receive the notifications
     * @return this
     */
    fun setListener(trustlyListener: TrustlyListener?): Trustly

    /**
     * Destroy Trustly WebView
     *
     * @return this
     */
    fun destroy(): Trustly

    /**
     * This method call a function in order for the Lightbox to proceed with the transaction
     */
    fun proceedToChooseAccount()

}