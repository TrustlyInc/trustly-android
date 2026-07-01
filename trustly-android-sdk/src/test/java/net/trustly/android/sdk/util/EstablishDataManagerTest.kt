package net.trustly.android.sdk.util

import android.content.Context
import android.content.SharedPreferences
import net.trustly.android.sdk.util.TrustlyConstants.PAYMENT_PROVIDER_ID
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class EstablishDataManagerTest {

    @Mock
    private lateinit var mockSharedPreferencesEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        EstablishDataManager.getEstablishData().clear()

        mockSharedPreferencesEditor = mock(SharedPreferences.Editor::class.java)
        mockSharedPreferences = mock(SharedPreferences::class.java)
        mockContext = mock(Context::class.java)
    }

    @After
    fun tearDown() {
        clearInvocations(mockSharedPreferencesEditor, mockSharedPreferences, mockContext)
    }

    @Test
    fun shouldValidateEstablishDataManagerUpdateEstablishData() {
        val establishData = mapOf("key1" to "value1", "key2" to "value2")
        EstablishDataManager.updateEstablishData(establishData)
        val result = EstablishDataManager.getEstablishData()
        assertEquals(establishData, result)
    }

    @Test
    fun shouldValidateEstablishDataManagerUpdatePaymentProviderId() {
        val paymentProviderId = "provider123"
        EstablishDataManager.updatePaymentProviderId(paymentProviderId)
        val result = EstablishDataManager.getEstablishData()
        assertEquals(paymentProviderId, result[PAYMENT_PROVIDER_ID])
    }

    @Test
    fun shouldValidateEstablishDataManagerSaveLastUsedBankByTrustlyContext() {
        `when`(mockSharedPreferencesEditor.putString(anyString(), anyString())).thenReturn(mockSharedPreferencesEditor)
        `when`(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor)
        `when`(mockContext.getSharedPreferences("LAST_USED_BANK", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)

        val establishData = mapOf("trustlyContext" to "context123", "key" to "value", "key2" to "value2")
        val result = EstablishDataManager.saveLastUsedBankByTrustlyContext(mockContext, establishData)
        assertEquals("value", result["key"])
        assertEquals("value2", result["key2"])
        assertEquals(null, result["trustlyContext"])
    }

    @Test
    fun shouldValidateEstablishDataManagerSaveLastUsedBankWithoutTrustlyContext() {
        val establishData = mapOf("key" to "value", "key2" to "value2")
        val result = EstablishDataManager.saveLastUsedBankByTrustlyContext(mockContext, establishData)
        assertEquals("value", result["key"])
        assertEquals("value2", result["key2"])
        assertEquals(null, result["trustlyContext"])
    }

}