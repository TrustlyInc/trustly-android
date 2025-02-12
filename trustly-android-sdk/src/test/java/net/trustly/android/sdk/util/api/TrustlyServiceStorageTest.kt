package net.trustly.android.sdk.util.api

import android.content.Context
import android.content.SharedPreferences
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TrustlyServiceStorageTest {

    @Mock
    lateinit var mockEdit: SharedPreferences.Editor

    @Mock
    lateinit var mockedPrefs: SharedPreferences

    @Mock
    lateinit var mockedContext: Context

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun tearDown() {
        clearInvocations(mockEdit, mockedContext, mockedPrefs)
    }

    @Test
    fun testSavingDataAPIRequest() {
        `when`(mockEdit.putString(anyString(), anyString())).thenReturn(mockEdit)
        `when`(mockedPrefs.edit()).thenReturn(mockEdit)
        `when`(mockedContext.getSharedPreferences("API_STORAGE", Context.MODE_PRIVATE)).thenReturn(mockedPrefs)

        APIRequestStorage.saveData(mockedContext, "API_REQUEST", "1724258668")

        verify(mockEdit, times(1)).putString("API_REQUEST", "1724258668")
        verify(mockEdit, times(1)).apply()
    }

    @Test
    fun testReadingDataNullAPIRequest() {
        `when`(mockedPrefs.getString(anyString(), anyString())).thenReturn("1724258668")
        `when`(mockedContext.getSharedPreferences("API_STORAGE", Context.MODE_PRIVATE)).thenReturn(mockedPrefs)

        val apiRequest = APIRequestStorage.readDataFrom(mockedContext, "API_REQUEST")

        verify(mockedPrefs, times(1)).getString("API_REQUEST", null)
        assertEquals(null, apiRequest)
    }

}