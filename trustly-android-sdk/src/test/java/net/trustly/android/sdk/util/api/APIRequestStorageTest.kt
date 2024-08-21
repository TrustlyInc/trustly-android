package net.trustly.android.sdk.util.api

import android.content.Context
import android.content.SharedPreferences
import org.junit.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class APIRequestStorageTest {

    @Test
    fun testSavingDataAPIRequest() {
        val mockEdit = mock(SharedPreferences.Editor::class.java)
        `when`(mockEdit.putString(anyString(), anyString())).thenReturn(mockEdit)

        val mockedPrefs = mock(SharedPreferences::class.java)
        `when`(mockedPrefs.edit()).thenReturn(mockEdit)

        val mockedContext = mock(Context::class.java)
        `when`(mockedContext.getSharedPreferences("API_STORAGE", Context.MODE_PRIVATE)).thenReturn(mockedPrefs)

        APIRequestStorage.saveData(mockedContext, "API_REQUEST", "1724258668")

        verify(mockEdit, times(1)).putString("API_REQUEST", "1724258668")
        verify(mockEdit, times(1)).apply()
    }

    @Test
    fun testReadingDataAPIRequest() {
        val mockedPrefs = mock(SharedPreferences::class.java)
        `when`(mockedPrefs.getString(anyString(), anyString())).thenReturn("1724258668")

        val mockedContext = mock(Context::class.java)
        `when`(mockedContext.getSharedPreferences("API_STORAGE", Context.MODE_PRIVATE)).thenReturn(mockedPrefs)

        APIRequestStorage.readDataFrom(mockedContext, "API_REQUEST")

        verify(mockedPrefs, times(1)).getString("API_REQUEST", null)
    }

}