package net.trustly.android.sdk.util.grp

import android.content.Context
import android.content.SharedPreferences
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class GRPStorageTest {

    @Mock
    private lateinit var mockSharedPreferencesEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockSharedPreferencesEditor = mock(SharedPreferences.Editor::class.java)
        mockSharedPreferences = mock(SharedPreferences::class.java)
        mockContext = mock(Context::class.java)
    }

    @After
    fun tearDown() {
        clearInvocations(mockSharedPreferencesEditor, mockSharedPreferences, mockContext)
    }

    @Test
    fun testSavingDataGRP() {
        `when`(mockSharedPreferencesEditor.putInt(anyString(), anyInt())).thenReturn(mockSharedPreferencesEditor)
        `when`(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor)
        `when`(mockContext.getSharedPreferences("PayWithMyBank", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)

        GRPStorage.saveData(mockContext, 32)

        verify(mockSharedPreferencesEditor, times(1)).putInt("grp", 32)
        verify(mockSharedPreferencesEditor, times(1)).apply()
    }

    @Test
    fun testReadingDataGRP() {
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(54)
        `when`(mockContext.getSharedPreferences("PayWithMyBank", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)

        val apiRequest = GRPStorage.readDataFrom(mockContext)

        verify(mockSharedPreferences, times(1)).getInt("grp", -1)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(54, apiRequest)
    }

    @Test
    fun testReadingDataMinusOneGRP() {
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(-1)
        `when`(mockContext.getSharedPreferences("PayWithMyBank", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)

        val apiRequest = GRPStorage.readDataFrom(mockContext)

        verify(mockSharedPreferences, times(1)).getInt("grp", -1)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(-1, apiRequest)
    }

}