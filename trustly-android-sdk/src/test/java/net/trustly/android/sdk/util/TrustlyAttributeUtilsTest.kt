package net.trustly.android.sdk.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TrustlyAttributeUtilsTest {

    @Test
    fun shouldReturnMerchantReferenceForValidAttributes() {
        val validAttributes = mapOf(
            "accessId" to "validAccessId",
            "merchantId" to "validMerchantId",
            "returnUrl" to "validReturnUrl",
            "cancelUrl" to "validCancelUrl",
            "requestSignature" to "validRequestSignature"
        )

        val result = EstablishDataUtils.validateEstablishDataRequiredFields(validAttributes)
        assertTrue(result.contains("merchantReference"))
    }

    @Test
    fun shouldReturnNullForValidAttributes() {
        val validAttributes = mapOf(
            "accessId" to "validAccessId",
            "merchantId" to "validMerchantId",
            "merchantReference" to "validMerchantReference",
            "returnUrl" to "validReturnUrl",
            "cancelUrl" to "validCancelUrl",
            "requestSignature" to "validRequestSignature"
        )

        val result = EstablishDataUtils.validateEstablishDataRequiredFields(validAttributes)
        assertEquals("", result)
    }

}