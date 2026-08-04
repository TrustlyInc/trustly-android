package net.trustly.android.sdk.util.last_used

import android.content.Context
import android.util.Base64
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito.CALLS_REAL_METHODS
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.MockitoAnnotations

class LastUsedBankManagerTest {

    @Mock
    private lateinit var mockContext: Context

    private lateinit var mockedStaticBase64: MockedStatic<Base64>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        LastUsedBankManager.clearForTesting()
        mockedStaticBase64 = mockStatic(Base64::class.java, CALLS_REAL_METHODS)
    }

    @After
    fun tearDown() {
        LastUsedBankManager.clearForTesting()
        mockedStaticBase64.close()
        clearInvocations(mockContext)
    }

    @Test
    fun shouldReturnNullWhenNothingSaved() {
        assertNull(LastUsedBankManager.getLastUsedBankBase64(mockContext))
    }

    @Test
    fun shouldReturnSavedValueFromMemory() {
        LastUsedBankManager.saveLastUsedBank(mockContext, "lastUsed")

        assertEquals("lastUsed", LastUsedBankManager.getLastUsedBankBase64(mockContext))
    }

    @Test
    fun shouldOverwritePreviousValueOnSubsequentSave() {
        LastUsedBankManager.saveLastUsedBank(mockContext, "first")
        LastUsedBankManager.saveLastUsedBank(mockContext, "second")

        assertEquals("second", LastUsedBankManager.getLastUsedBankBase64(mockContext))
    }

    @Test
    fun shouldReturnBankForCountryCode() {
        mockLastUsedBank("{\"lastUsed\":{\"US\":\"123456789\"}}")
        LastUsedBankManager.saveLastUsedBank(mockContext, "eyJsYXN0VXNlZCI6eyJVUyI6IjEyMzQ1Njc4OSJ9fQ==")

        val lastUsedBank = LastUsedBankManager.getLaseUsedBankByCountryCode(mockContext, "US")

        assertEquals("123456789", lastUsedBank)
    }

    @Test
    fun shouldReturnBankForCountryCodeWithMultipleEntries() {
        mockLastUsedBank("{\"lastUsed\":{\"US\":\"123456789\",\"CA\":\"24681012\",\"FR\":\"1357911\"}}")
        LastUsedBankManager.saveLastUsedBank(mockContext, "anyBase64")

        val lastUsedBank = LastUsedBankManager.getLaseUsedBankByCountryCode(mockContext, "CA")

        assertEquals("24681012", lastUsedBank)
    }

    @Test
    fun shouldReturnNullByCountryCodeWhenNothingSaved() {
        assertNull(LastUsedBankManager.getLaseUsedBankByCountryCode(mockContext, "US"))
    }

    @Test
    fun shouldReturnNullByCountryCodeWhenCountryNotPresent() {
        mockLastUsedBank("{\"lastUsed\":{\"US\":\"123456789\"}}")
        LastUsedBankManager.saveLastUsedBank(mockContext, "anyBase64")

        val lastUsedBank = LastUsedBankManager.getLaseUsedBankByCountryCode(mockContext, "CA")

        assertNull(lastUsedBank)
    }

    private fun mockLastUsedBank(lastUsedJson: String) {
        mockedStaticBase64.`when`<Any> {
            Base64.decode(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
        }.thenReturn(lastUsedJson.toByteArray())
    }

}
