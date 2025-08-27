package net.trustly.android.sdk.views.events

import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.interfaces.TrustlyCallback
import net.trustly.android.sdk.interfaces.TrustlyListener
import net.trustly.android.sdk.util.TrustlyConstants.EVENT
import net.trustly.android.sdk.util.TrustlyConstants.EVENT_PAGE
import net.trustly.android.sdk.util.TrustlyConstants.EVENT_TYPE
import net.trustly.android.sdk.util.TrustlyConstants.WIDGET
import net.trustly.android.sdk.util.error.TrustlyExceptionHandler

class TrustlyEvents {

    private var onReturn: TrustlyCallback<Trustly, Map<String, String>>? = null
    private var onCancel: TrustlyCallback<Trustly, Map<String, String>>? = null
    private var onWidgetBankSelected: TrustlyCallback<Trustly, Map<String, String>>? = null
    private var trustlyListener: TrustlyListener? = null
    private var onExternalUrl: TrustlyCallback<Trustly, Map<String, String>>? = null

    fun setOnExternalUrlCallback(onExternalUrl: TrustlyCallback<Trustly, Map<String, String>>?) {
        this.onExternalUrl = onExternalUrl
    }

    fun handleOnExternalUrl(trustlyView: Trustly, params: Map<String, String>) {
        this.onExternalUrl?.handle(trustlyView, params)
    }

    fun setOnReturnCallback(onReturn: TrustlyCallback<Trustly, Map<String, String>>?) {
        this.onReturn = onReturn
    }

    fun handleOnReturn(trustlyView: Trustly, queryParametersFromUrl: Map<String, String>) {
        this.onReturn?.handle(trustlyView, queryParametersFromUrl)
    }

    fun setOnCancelCallback(onCancel: TrustlyCallback<Trustly, Map<String, String>>?) {
        this.onCancel = onCancel
    }

    fun handleOnCancel(trustlyView: Trustly, queryParametersFromUrl: Map<String, String>) {
        this.onCancel?.handle(trustlyView, queryParametersFromUrl)
    }

    fun setOnWidgetBankSelectedCallback(onWidgetBankSelected: TrustlyCallback<Trustly, Map<String, String>>?) {
        this.onWidgetBankSelected = onWidgetBankSelected
    }

    fun handleOnWidgetBankSelected(trustlyView: Trustly, params: Map<String, String>) {
        this.onWidgetBankSelected?.handle(trustlyView, params)
    }

    fun setTrustlyListener(trustlyListener: TrustlyListener?) {
        this.trustlyListener = trustlyListener
    }

    fun notifyListener(eventName: String, eventDetails: HashMap<String, String>) {
        this.trustlyListener?.onChange(eventName, eventDetails)
    }

    fun notifyOpen() {
        this.notifyListener("open", HashMap())
    }

    fun notifyClose() {
        this.notifyListener("close", HashMap())
    }

    fun notifyWidgetLoading() {
        this.notifyListener(
            EVENT, hashMapOf(
                EVENT_PAGE to WIDGET,
                EVENT_TYPE to "loading"
            )
        )
    }

    fun notifyWidgetLoaded() {
        this.notifyListener(
            EVENT, hashMapOf(
                EVENT_PAGE to WIDGET,
                EVENT_TYPE to "load"
            )
        )
    }

    fun handleErrorLog(description: String, failingUrl: String = "") {
        TrustlyExceptionHandler().uncaughtException(
            Thread.currentThread(),
            Exception(failingUrl, Throwable(description))
        )
    }

}