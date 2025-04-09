package net.trustly.android.sdk.util

import junit.framework.Assert.assertEquals
import org.junit.Test

class AttributeUtilsTest {

    @Test
    fun shouldReturnMerchantReferenceForValidAttributes() {
        val validAttributes = mapOf(
            "accessId" to "validAccessId",
            "merchantId" to "validMerchantId",
            "returnUrl" to "validReturnUrl",
            "cancelUrl" to "validCancelUrl",
            "requestSignature" to "validRequestSignature"
        )

        val result = AttributeUtils.validateEstablishDataRequiredFields(validAttributes)
        assertEquals("merchantReference", result)
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

        val result = AttributeUtils.validateEstablishDataRequiredFields(validAttributes)
        assertEquals(null, result)
    }

}