package net.trustly.android.sdk.views.events

import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.interfaces.TrustlyCallback
import net.trustly.android.sdk.interfaces.TrustlyEvents
import net.trustly.android.sdk.interfaces.TrustlyListener
import net.trustly.android.sdk.util.TrustlyConstants.EVENT
import net.trustly.android.sdk.util.TrustlyConstants.EVENT_PAGE
import net.trustly.android.sdk.util.TrustlyConstants.EVENT_TYPE
import net.trustly.android.sdk.util.TrustlyConstants.WIDGET
import net.trustly.android.sdk.util.error.TrustlyExceptionHandler

class TrustlyEventsImpl : TrustlyEvents {

    private var onReturn: TrustlyCallback<Trustly, Map<String, String>>? = null
    private var onCancel: TrustlyCallback<Trustly, Map<String, String>>? = null
    private var onWidgetBankSelected: TrustlyCallback<Trustly, Map<String, String>>? = null
    private var trustlyListener: TrustlyListener? = null
    private var onExternalUrl: TrustlyCallback<Trustly, Map<String, String>>? = null

    override fun setOnExternalUrlCallback(onExternalUrl: TrustlyCallback<Trustly, Map<String, String>>?) {
        this.onExternalUrl = onExternalUrl
    }

    override fun handleOnExternalUrl(trustlyView: Trustly, params: Map<String, String>) {
        this.onExternalUrl?.handle(trustlyView, params)
    }

    override fun setOnReturnCallback(onReturn: TrustlyCallback<Trustly, Map<String, String>>?) {
        this.onReturn = onReturn
    }

    override fun handleOnReturn(trustlyView: Trustly?, queryParametersFromUrl: Map<String, String>) {
        this.onReturn?.handle(trustlyView, queryParametersFromUrl)
    }

    override fun setOnCancelCallback(onCancel: TrustlyCallback<Trustly, Map<String, String>>?) {
        this.onCancel = onCancel
    }

    override fun handleOnCancel(trustlyView: Trustly?, queryParametersFromUrl: Map<String, String>) {
        this.onCancel?.handle(trustlyView, queryParametersFromUrl)
    }

    override fun setOnWidgetBankSelectedCallback(onWidgetBankSelected: TrustlyCallback<Trustly, Map<String, String>>?) {
        this.onWidgetBankSelected = onWidgetBankSelected
    }

    override fun handleOnWidgetBankSelected(trustlyView: Trustly, params: Map<String, String>) {
        this.onWidgetBankSelected?.handle(trustlyView, params)
    }

    override fun setTrustlyListener(trustlyListener: TrustlyListener?) {
        this.trustlyListener = trustlyListener
    }

    override fun notifyListener(eventName: String, eventDetails: HashMap<String, String>) {
        this.trustlyListener?.onChange(eventName, eventDetails)
    }

    override fun notifyOpen() {
        this.notifyListener("open", HashMap())
    }

    override fun notifyClose() {
        this.notifyListener("close", HashMap())
    }

    override fun notifyWidgetLoading() {
        this.notifyListener(
            EVENT, hashMapOf(
                EVENT_PAGE to WIDGET,
                EVENT_TYPE to "loading"
            )
        )
    }

    override fun notifyWidgetLoaded() {
        this.notifyListener(
            EVENT, hashMapOf(
                EVENT_PAGE to WIDGET,
                EVENT_TYPE to "load"
            )
        )
    }

    override fun handleErrorLog(description: String, failingUrl: String) {
        TrustlyExceptionHandler().uncaughtException(
            Thread.currentThread(),
            Exception(failingUrl, Throwable(description))
        )
    }

}