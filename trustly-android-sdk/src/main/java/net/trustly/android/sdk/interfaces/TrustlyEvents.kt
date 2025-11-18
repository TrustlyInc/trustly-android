package net.trustly.android.sdk.interfaces

interface TrustlyEvents {

    fun setOnExternalUrlCallback(onExternalUrl: TrustlyCallback<Trustly, Map<String, String>>?)
    fun handleOnExternalUrl(trustlyView: Trustly, params: Map<String, String>)
    fun setOnReturnCallback(onReturn: TrustlyCallback<Trustly, Map<String, String>>?)
    fun handleOnReturn(trustlyView: Trustly?, queryParametersFromUrl: Map<String, String>)
    fun setOnCancelCallback(onCancel: TrustlyCallback<Trustly, Map<String, String>>?)
    fun handleOnCancel(trustlyView: Trustly?, queryParametersFromUrl: Map<String, String>)
    fun setOnWidgetBankSelectedCallback(onWidgetBankSelected: TrustlyCallback<Trustly, Map<String, String>>?)
    fun handleOnWidgetBankSelected(trustlyView: Trustly, params: Map<String, String>)
    fun setTrustlyListener(trustlyListener: TrustlyListener?)
    fun notifyListener(eventName: String, eventDetails: HashMap<String, String>)
    fun notifyOpen()
    fun notifyClose()
    fun notifyWidgetLoading()
    fun notifyWidgetLoaded()
    fun handleErrorLog(description: String, failingUrl: String = "")

}