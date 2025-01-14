package net.trustly.android.sdk.interfaces

import android.webkit.JavascriptInterface
import net.trustly.android.sdk.views.TrustlyView

open class TrustlyJsInterface(private val trustlyView: TrustlyView) {

    private val PAYWITHMYBANK_EVENT: String = "PayWithMyBank.event"
    private val EVENT: String = "event"
    private val NULL_VALUE: String = "null"
    private val PARAMS_DIVIDER: String = "\\|"

    @JavascriptInterface
    fun postMessage(message: String?) {
        if (message == null || message.trim().isEmpty()) return

        val params = message.split(PARAMS_DIVIDER.toRegex()).toTypedArray()
        val command = params[0]
        if (command.trim().isEmpty()) return

        if (command.equals(PAYWITHMYBANK_EVENT, ignoreCase = true)) {
            val eventDetails = HashMap<String, String>()
            for ((key, value) in getEventNames()) {
                addToListenerDetails(params, key, value, eventDetails)
            }
            trustlyView.notifyListener(EVENT, eventDetails)
        }
    }

    @JavascriptInterface
    fun resize(width: Float, height: Float) {
        trustlyView.resize(width, height)
    }

    fun addToListenerDetails(
        params: Array<String>?,
        index: Int,
        eventName: String?,
        eventDetails: HashMap<String, String>?
    ) {
        if (eventDetails == null || eventName == null || params == null || index >= params.size) return
        val value = params[index]
        if (value.trim().isEmpty() || value.trim().equals(NULL_VALUE, ignoreCase = true)) return
        eventDetails[eventName] = params[index]
    }

    private fun getEventNames(): Map<Int, String?> {
        return mapOf(
            2 to "page",
            3 to "transactionId",
            4 to "merchantReference",
            5 to "type",
            6 to "data",
            7 to "transfer",
        )
    }

}