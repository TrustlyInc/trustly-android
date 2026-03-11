package net.trustly.android.sdk.util.last_used

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito.CALLS_REAL_METHODS
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LastUsedBankManagerTest {

    companion object {
        private const val PREFERENCES_NAME = "LAST_USED_BANK"
    }

    @Mock
    private lateinit var mockSharedPreferencesEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockContext: Context

    private lateinit var mockedStaticBase64: MockedStatic<Base64>

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
        mockedStaticBase64 = mockStatic(Base64::class.java, CALLS_REAL_METHODS)
    }

    @After
    fun tearDown() {
        mockedStaticBase64.close()
        clearInvocations(mockContext, mockSharedPreferencesEditor, mockSharedPreferences)
    }

    @Test
    fun shouldValidateLastUsedBankManagerGetLastUsedBase64() {
        `when`(
            mockSharedPreferences.getString(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn("bGFzdFVzZWQ=")

        val lastUsedBank = LastUsedBankManager.getLastUsedBankBase64(mockContext)

        verify(mockSharedPreferences, times(1)).getString("LAST_USED_BANK_ID", null)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals("bGFzdFVzZWQ=", lastUsedBank)
    }

    @Test
    fun shouldValidateLastUsedBankManagerSaveLastUsed() {
        LastUsedBankManager.saveLastUsedBank(mockContext, "lastUsed")

        verify(mockSharedPreferencesEditor, times(1)).putString("LAST_USED_BANK_ID", "lastUsed")
        verify(mockSharedPreferencesEditor, times(1)).apply()
    }

    @Test
    fun shouldValidateLastUsedBankManagerGetLastUsedByCountryCode() {
        mockLastUsedBank("{\"lastUsed\":{\"US\":\"123456789\"}}")

        `when`(
            mockSharedPreferences.getString(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn("eyJsYXN0VXNlZCI6eyJVUyI6IjEyMzQ1Njc4OSJ9fQ==")

        val lastUsedBank = LastUsedBankManager.getLaseUsedBankByCountryCode(mockContext, "US")

        verify(mockSharedPreferences, times(1)).getString("LAST_USED_BANK_ID", null)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals("123456789", lastUsedBank)
    }

    @Test
    fun shouldValidateLastUsedBankManagerGetLastUsedByCountryCodeMultiple() {
        mockLastUsedBank("{\"lastUsed\":{\"US\":\"123456789\",\"CA\":\"24681012\",\"FR\":\"1357911\"}}")

        `when`(
            mockSharedPreferences.getString(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn("eyJsYXN0VXNlZCI6eyJVUyI6IjEyMzQ1Njc4OSJ9fQ==")

        val lastUsedBank = LastUsedBankManager.getLaseUsedBankByCountryCode(mockContext, "CA")

        verify(mockSharedPreferences, times(1)).getString("LAST_USED_BANK_ID", null)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals("24681012", lastUsedBank)
    }

    @Test
    fun shouldValidateLastUsedBankManagerGetLastUsedByCountryCodeNull() {
        `when`(
            mockSharedPreferences.getString(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn(null)

        val lastUsedBank = LastUsedBankManager.getLaseUsedBankByCountryCode(mockContext, "US")

        verify(mockSharedPreferences, times(1)).getString("LAST_USED_BANK_ID", null)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(null, lastUsedBank)
    }

    @Test
    fun shouldValidateLastUsedBankManagerGetLastUsedByCountryCodeDifferent() {
        mockLastUsedBank("{\"lastUsed\":{\"US\":\"123456789\"}}")

        `when`(
            mockSharedPreferences.getString(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn("eyJsYXN0VXNlZCI6eyJVUyI6IjEyMzQ1Njc4OSJ9fQ==")

        val lastUsedBank = LastUsedBankManager.getLaseUsedBankByCountryCode(mockContext, "CA")

        verify(mockSharedPreferences, times(1)).getString("LAST_USED_BANK_ID", null)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(null, lastUsedBank)
    }

    private fun mockLastUsedBank(lastUsedJson: String) {
        mockedStaticBase64.`when`<Any> {
            Base64.decode(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
        }.thenReturn(lastUsedJson.toByteArray())
    }

}