package net.trustly.android.sdk.util

import android.util.Log
import net.trustly.android.sdk.util.TrustlyConstants.ACCESS_ID
import net.trustly.android.sdk.util.TrustlyConstants.CANCEL_URL
import net.trustly.android.sdk.util.TrustlyConstants.MERCHANT_ID
import net.trustly.android.sdk.util.TrustlyConstants.MERCHANT_REFERENCE
import net.trustly.android.sdk.util.TrustlyConstants.REQUEST_SIGNATURE
import net.trustly.android.sdk.util.TrustlyConstants.RETURN_URL

object EstablishDataUtils {

    private val REQUIRED_FIELDS = listOf(
        ACCESS_ID, MERCHANT_ID, MERCHANT_REFERENCE, RETURN_URL, CANCEL_URL, REQUEST_SIGNATURE
    )

    fun validateEstablishDataRequiredFields(establishData: Map<String, String>): String {
        var attributesMissing = ""
        for (field in REQUIRED_FIELDS) {
            if (!establishData.containsKey(field)) {
                attributesMissing += "$field "
            }
        }
        if (attributesMissing.isNotEmpty()) {
            showErrorMessage(attributesMissing)
        }
        return attributesMissing
    }

    private fun showErrorMessage(attributes: String) {
        Log.w("EstablishDataUtils", "Required attributes missing: $attributes\n" +
                "Learn more at Trustly Docs: https://amer.developers.trustly.com/payments/docs/establish-data#base-properties")
    }

}