package net.trustly.android.sdk.util.api

import android.content.Context
import android.content.SharedPreferences
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.CALLS_REAL_METHODS
import org.mockito.Mockito.anyString
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


class APIRequestManagerTest {

    @Mock
    lateinit var mockEdit: SharedPreferences.Editor

    @Mock
    lateinit var mockedPrefs: SharedPreferences

    @Mock
    lateinit var mockedContext: Context

    @Before
    fun setUp() {
        mockEdit = mock(SharedPreferences.Editor::class.java)
        mockedPrefs = mock(SharedPreferences::class.java)
        mockedContext = mock(Context::class.java)
    }

    @After
    fun tearDown() {
        clearInvocations(mockEdit, mockedContext, mockedPrefs)
    }

    @Test
    fun testGettingDataAPIRequest() {
        `when`(mockedContext.getSharedPreferences("API_STORAGE", Context.MODE_PRIVATE)).thenReturn(mockedPrefs)
        `when`(mockedPrefs.getString(anyString(), anyString())).thenReturn("1724258668")

        mockStatic(APIRequestStorage::class.java, CALLS_REAL_METHODS).use { static ->
            static.`when`<Any> { APIRequestStorage.readDataFrom(mockedContext, "API_REQUEST") }
                .thenReturn("13896438")
        }

        val result = APIRequestManager.getAPIRequest(mockedContext)
        assertEquals("13896438", result)
    }

    @Test
    fun testSavingDataAPIRequest() {
        `when`(mockEdit.putString(anyString(), anyString())).thenReturn(mockEdit)
        `when`(mockedPrefs.edit()).thenReturn(mockEdit)
        `when`(mockedContext.getSharedPreferences("API_STORAGE", Context.MODE_PRIVATE)).thenReturn(mockedPrefs)

        APIRequestManager.saveAPIRequest(mockedContext)

        verify(mockEdit, times(1)).apply()
    }

}