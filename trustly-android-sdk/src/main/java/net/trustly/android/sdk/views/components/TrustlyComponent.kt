package net.trustly.android.sdk.views.components

import android.util.Log

abstract class TrustlyComponent {

    abstract fun updateEstablishData(establishData: Map<String, String>, grp: Int)

    protected fun showErrorMessage(tag: String, e: Exception) {
        Log.e(tag, e.message, e)
    }

}