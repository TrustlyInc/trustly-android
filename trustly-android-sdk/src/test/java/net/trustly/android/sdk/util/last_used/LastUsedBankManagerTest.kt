package net.trustly.android.sdk.util.last_used

import android.content.Context
import android.content.SharedPreferences
import net.trustly.android.sdk.data.TrustlyStorage
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

class LastUsedBankManagerTest {

    companion object {
        private const val PREFERENCES_NAME = "preferencesName"
    }

    @Mock
    private lateinit var mockSharedPreferencesEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockTrustlyStorage: TrustlyStorage

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
        clearInvocations(mockContext, mockSharedPreferencesEditor, mockSharedPreferences)
    }

    @Test
    fun shouldValidateLastUsedBankManagerGetLastUser() {
        `when`(mockTrustlyStorage.readStringDataFrom(ArgumentMatchers.anyString())).thenReturn("lastUser")
        `when`(
            mockSharedPreferences.getString(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn("lastUser")

        val lastUsedBank = LastUsedBankManager(mockContext).getLastUsedBank()

        verify(mockSharedPreferences, times(1)).getString("preferenceId", null)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals("lastUser", lastUsedBank)
    }

    @Test
    fun shouldValidateLastUsedBankManagerSaveLastUser() {
        LastUsedBankManager(mockContext).saveLastUsedBank("lastUser")

        verify(mockSharedPreferencesEditor, times(1)).putString("last_used_bank_id", "lastUser")
        verify(mockSharedPreferencesEditor, times(1)).apply()
    }

}