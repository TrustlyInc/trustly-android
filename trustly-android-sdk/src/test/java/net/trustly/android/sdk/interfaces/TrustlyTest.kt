package net.trustly.android.sdk.interfaces

import android.content.Context
import org.junit.After
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TrustlyTest {

    private val URL: String = "http://www.url.com"
    private val RETURN_URL: String = "http://www.url.com/return"
    private val CANCEL_URL: String = "http://www.url.com/cancel"

    @Mock
    private lateinit var mockTrustly: Trustly

    @Mock
    private var mockTrustlyCallback: TrustlyCallback<Trustly, Map<String, String>>? = null

    @Mock
    private lateinit var mockTrustlyListener: TrustlyListener

    @Mock
    private lateinit var mockContext: Context

    private lateinit var trustlyImpl: TrustlyImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        trustlyImpl = TrustlyImpl(mockTrustly)
    }

    @After
    fun tearDown() {
        clearInvocations(mockTrustly, mockTrustlyCallback, mockTrustlyListener, mockContext)
    }

    @Test
    fun shouldValidateTrustlySelectBankWidgetWhenIsCalled() {
        `when`(mockTrustly.selectBankWidget(anyMap())).thenReturn(mockTrustly)
        val establishData = getEstablishData()
        val result = trustlyImpl.callSelectBankWidget(establishData)
        verify(mockTrustly, times(1)).selectBankWidget(establishData)
        assertSame(mockTrustly, result)
    }

    @Test
    fun shouldValidateTrustlySelectBankEmptyValuesWidgetWhenIsCalled() {
        val result = trustlyImpl.callSelectBankWidget(emptyMap())
        verify(mockTrustly, times(1)).selectBankWidget(emptyMap())
        assertSame(null, result)
    }

    @Test
    fun shouldValidateTrustlyOnBankSelectedWhenIsCalled() {
        `when`(mockTrustly.onBankSelected(any())).thenReturn(mockTrustly)
        val result = trustlyImpl.callOnBankSelected(mockTrustlyCallback)
        verify(mockTrustly, times(1)).onBankSelected(mockTrustlyCallback)
        assertSame(mockTrustly, result)
    }

    @Test
    fun shouldValidateTrustlyOnBankSelectedNullValuesWidgetWhenIsCalled() {
        val result = trustlyImpl.callOnBankSelected(null)
        verify(mockTrustly, times(1)).onBankSelected(null)
        assertSame(null, result)
    }

    @Test
    fun shouldValidateTrustlyEstablishWhenIsCalled() {
        `when`(mockTrustly.establish(anyMap())).thenReturn(mockTrustly)
        val establishData = getEstablishData()
        val result = trustlyImpl.callEstablish(establishData)
        verify(mockTrustly, times(1)).establish(establishData)
        assertSame(mockTrustly, result)
    }

    @Test
    fun shouldValidateTrustlyEstablishEmptyValuesWidgetWhenIsCalled() {
        val result = trustlyImpl.callEstablish(emptyMap())
        verify(mockTrustly, times(1)).establish(emptyMap())
        assertSame(null, result)
    }

    @Test
    fun shouldValidateTrustlyHybridWhenIsCalled() {
        `when`(mockTrustly.hybrid(anyString(), anyString(), anyString())).thenReturn(mockTrustly)
        val result = trustlyImpl.callHybrid(URL, RETURN_URL, CANCEL_URL)
        verify(mockTrustly, times(1)).hybrid(URL, RETURN_URL, CANCEL_URL)
        assertSame(mockTrustly, result)
    }

    @Test
    fun shouldValidateTrustlyOnReturnWhenIsCalled() {
        `when`(mockTrustly.onReturn(any())).thenReturn(mockTrustly)
        val result = trustlyImpl.callOnReturn(mockTrustlyCallback)
        verify(mockTrustly, times(1)).onReturn(mockTrustlyCallback)
        assertSame(mockTrustly, result)
    }

    @Test
    fun shouldValidateTrustlyOnReturnNullValuesWidgetWhenIsCalled() {
        val result = trustlyImpl.callOnReturn(null)
        verify(mockTrustly, times(1)).onReturn(null)
        assertSame(null, result)
    }

    @Test
    fun shouldValidateTrustlyOnCancelWhenIsCalled() {
        `when`(mockTrustly.onCancel(any())).thenReturn(mockTrustly)
        val result = trustlyImpl.callOnCancel(mockTrustlyCallback)
        verify(mockTrustly, times(1)).onCancel(mockTrustlyCallback)
        assertSame(mockTrustly, result)
    }

    @Test
    fun shouldValidateTrustlyOnCancelNullValuesWidgetWhenIsCalled() {
        val result = trustlyImpl.callOnCancel(null)
        verify(mockTrustly, times(1)).onCancel(null)
        assertSame(null, result)
    }

    @Test
    fun shouldValidateTrustlyOnExternalUrlWhenIsCalled() {
        `when`(mockTrustly.onExternalUrl(any())).thenReturn(mockTrustly)
        val result = trustlyImpl.callOnExternalUrl(mockTrustlyCallback)
        verify(mockTrustly, times(1)).onExternalUrl(mockTrustlyCallback)
        assertSame(mockTrustly, result)
    }

    @Test
    fun shouldValidateTrustlyOnExternalUrlNullValuesWidgetWhenIsCalled() {
        val result = trustlyImpl.callOnExternalUrl(null)
        verify(mockTrustly, times(1)).onExternalUrl(null)
        assertSame(null, result)
    }

    @Test
    fun shouldValidateTrustlySetListenerWhenIsCalled() {
        `when`(mockTrustly.setListener(any())).thenReturn(mockTrustly)
        val result = trustlyImpl.callSetListener(mockTrustlyListener)
        verify(mockTrustly, times(1)).setListener(mockTrustlyListener)
        assertSame(mockTrustly, result)
    }

    @Test
    fun shouldValidateTrustlySetListenerNullValuesWidgetWhenIsCalled() {
        val result = trustlyImpl.callSetListener(null)
        verify(mockTrustly, times(1)).setListener(null)
        assertSame(null, result)
    }

    @Test
    fun shouldValidateTrustlyDestroyWhenIsCalled() {
        `when`(mockTrustly.destroy()).thenReturn(mockTrustly)
        val result = trustlyImpl.callDestroy()
        verify(mockTrustly, times(1)).destroy()
        assertSame(mockTrustly, result)
    }

    @Test
    fun shouldValidateTrustlyDestroyNullValueWhenIsCalled() {
        `when`(mockTrustly.destroy()).thenReturn(null)
        val result = trustlyImpl.callDestroy()
        verify(mockTrustly, times(1)).destroy()
        assertSame(null, result)
    }

    @Test
    fun shouldValidateTrustlyProceedToChooseAccountWhenIsCalled() {
        doNothing().`when`(mockTrustly).proceedToChooseAccount()
        trustlyImpl.callProceedToChooseAccount()
        verify(mockTrustly, times(1)).proceedToChooseAccount()
    }

    private fun getEstablishData() = mapOf(
        "accessId" to "123456",
        "merchantId" to "654321"
    )

    private class TrustlyImpl(private val trustly: Trustly) {

        fun callSelectBankWidget(establishData: Map<String, String>) = trustly.selectBankWidget(establishData)

        fun callOnBankSelected(callback: TrustlyCallback<Trustly, Map<String, String>>?) = trustly.onBankSelected(callback)

        fun callEstablish(establishData: Map<String, String>) = trustly.establish(establishData)

        fun callHybrid(url: String, returnUrl: String, cancelUrl: String) = trustly.hybrid(url, returnUrl, cancelUrl)

        fun callOnReturn(callback: TrustlyCallback<Trustly, Map<String, String>>?) = trustly.onReturn(callback)

        fun callOnCancel(callback: TrustlyCallback<Trustly, Map<String, String>>?) = trustly.onCancel(callback)

        fun callOnExternalUrl(callback: TrustlyCallback<Trustly, Map<String, String>>?) = trustly.onExternalUrl(callback)

        fun callSetListener(listener: TrustlyListener?) = trustly.setListener(listener)

        fun callDestroy() = trustly.destroy()

        fun callProceedToChooseAccount() {
            trustly.proceedToChooseAccount()
        }

    }

}