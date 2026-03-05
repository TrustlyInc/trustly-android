package net.trustly.android.sdk.data

import android.content.Context
import android.content.SharedPreferences
import net.trustly.android.sdk.util.last_used.LastUsedBankStorage
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LastUsedBankStorageTest {

    companion object {
        private const val PREFERENCES_NAME = "LAST_USED_BANK"
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

        `when`(
            mockSharedPreferencesEditor.putInt(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(mockSharedPreferencesEditor)
        `when`(
            mockSharedPreferencesEditor.putString(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
            )
        ).thenReturn(mockSharedPreferencesEditor)
        `when`(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor)
        `when`(mockContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
    }

    @After
    fun tearDown() {
        clearInvocations(mockSharedPreferencesEditor, mockSharedPreferences, mockContext)
    }

    @Test
    fun shouldValidateTrustlyStorageSavingIntData() {
        LastUsedBankStorage.saveData(mockContext, "preferenceId", 32)

        verify(mockSharedPreferencesEditor, times(1)).putInt("preferenceId", 32)
        verify(mockSharedPreferencesEditor, times(1)).apply()
    }

    @Test
    fun shouldValidateTrustlyStorageSavingStringData() {
        LastUsedBankStorage.saveData(mockContext, "preferenceId", "preferenceValue")

        verify(mockSharedPreferencesEditor, times(1)).putString("preferenceId", "preferenceValue")
        verify(mockSharedPreferencesEditor, times(1)).apply()
    }

    @Test
    fun shouldValidateTrustlyStorageReadingIntData() {
        `when`(
            mockSharedPreferences.getInt(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(54)

        val apiRequest = LastUsedBankStorage.readIntDataFrom(mockContext, "preferenceId")

        verify(mockSharedPreferences, times(1)).getInt("preferenceId", -1)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(54, apiRequest)
    }

    @Test
    fun shouldValidateTrustlyStorageReadingIntDataMinusOne() {
        `when`(
            mockSharedPreferences.getInt(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(-1)

        val apiRequest = LastUsedBankStorage.readIntDataFrom(mockContext, "preferenceId")

        verify(mockSharedPreferences, times(1)).getInt("preferenceId", -1)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(-1, apiRequest)
    }

    @Test
    fun shouldValidateTrustlyStorageReadingStringData() {
        `when`(
            mockSharedPreferences.getString(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn("preferenceValue")

        val apiRequest = LastUsedBankStorage.readStringDataFrom(mockContext, "preferenceId")

        verify(mockSharedPreferences, times(1)).getString("preferenceId", null)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals("preferenceValue", apiRequest)
    }

    @Test
    fun shouldValidateTrustlyStorageReadingStringDataNull() {
        `when`(
            mockSharedPreferences.getString(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn(null)

        val apiRequest = LastUsedBankStorage.readStringDataFrom(mockContext, "preferenceId")

        verify(mockSharedPreferences, times(1)).getString("preferenceId", null)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(null, apiRequest)
    }

}