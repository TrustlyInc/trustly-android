package net.trustly.android.sdk.util

import android.content.Context
import android.content.SharedPreferences
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TrustlyStorageTest {

    companion object {
        private const val PREFERENCES_NAME = "preferencesName"
        private const val PREFERENCE_ID = "preferenceId"
    }

    @Mock
    private lateinit var mockSharedPreferencesEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        `when`(mockSharedPreferencesEditor.putInt(anyString(), anyInt())).thenReturn(mockSharedPreferencesEditor)
        `when`(mockSharedPreferencesEditor.putString(anyString(), anyString())).thenReturn(mockSharedPreferencesEditor)
        `when`(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor)
        `when`(mockContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
    }

    @After
    fun tearDown() {
        clearInvocations(mockSharedPreferencesEditor, mockSharedPreferences, mockContext)
    }

    @Test
    fun shouldValidateTrustlyStorageSavingIntData() {
        TrustlyStorage(mockContext, PREFERENCES_NAME).saveData(PREFERENCE_ID, 32)

        verify(mockSharedPreferencesEditor, times(1)).putInt("preferenceId", 32)
        verify(mockSharedPreferencesEditor, times(1)).apply()
    }

    @Test
    fun shouldValidateTrustlyStorageSavingStringData() {
        TrustlyStorage(mockContext, PREFERENCES_NAME).saveData(PREFERENCE_ID, "preferenceValue")

        verify(mockSharedPreferencesEditor, times(1)).putString("preferenceId", "preferenceValue")
        verify(mockSharedPreferencesEditor, times(1)).apply()
    }

    @Test
    fun shouldValidateTrustlyStorageReadingIntData() {
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(54)

        val apiRequest = TrustlyStorage(mockContext, PREFERENCES_NAME).readIntDataFrom("preferenceId")

        verify(mockSharedPreferences, times(1)).getInt("preferenceId", -1)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(54, apiRequest)
    }

    @Test
    fun shouldValidateTrustlyStorageReadingIntDataMinusOne() {
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(-1)

        val apiRequest = TrustlyStorage(mockContext, PREFERENCES_NAME).readIntDataFrom("preferenceId")

        verify(mockSharedPreferences, times(1)).getInt("preferenceId", -1)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(-1, apiRequest)
    }

    @Test
    fun shouldValidateTrustlyStorageReadingStringData() {
        `when`(mockSharedPreferences.getString(anyString(), any())).thenReturn("preferenceValue")

        val apiRequest = TrustlyStorage(mockContext, PREFERENCES_NAME).readStringDataFrom("preferenceId")

        verify(mockSharedPreferences, times(1)).getString("preferenceId", null)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals("preferenceValue", apiRequest)
    }

    @Test
    fun shouldValidateTrustlyStorageReadingStringDataNull() {
        `when`(mockSharedPreferences.getString(anyString(), any())).thenReturn(null)

        val apiRequest = TrustlyStorage(mockContext, PREFERENCES_NAME).readStringDataFrom("preferenceId")

        verify(mockSharedPreferences, times(1)).getString("preferenceId", null)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(null, apiRequest)
    }

}