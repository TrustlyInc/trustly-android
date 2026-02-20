package net.trustly.android.sdk.util.last_used

import android.content.Context
import android.content.SharedPreferences
import net.trustly.android.sdk.data.TrustlyStorage
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LastUsedBankManagerTest {

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockTrustlyStorage: TrustlyStorage

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        `when`(mockContext.getSharedPreferences("last_used_bank", Context.MODE_PRIVATE)).thenReturn(
            mockSharedPreferences
        )
    }

    @After
    fun tearDown() {
        clearInvocations(mockContext, mockTrustlyStorage, mockSharedPreferences)
    }

    @Test
    fun shouldValidateLastUsedBankManagerGetLastUser() {
        `when`(
            mockTrustlyStorage.readStringDataFrom(
                anyString()
            )
        ).thenReturn("lastUser")

        val lastUsedBank = LastUsedBankManager(mockTrustlyStorage).getLastUsedBank()

        verify(mockTrustlyStorage, times(1)).readStringDataFrom("last_used_bank_id")
        assertEquals("lastUser", lastUsedBank)
    }

    @Test
    fun shouldValidateLastUsedBankManagerSaveLastUser() {
        LastUsedBankManager(mockTrustlyStorage).saveLastUsedBank("lastUser")

        verify(mockTrustlyStorage, times(1)).saveData("last_used_bank_id", "lastUser")
    }

}