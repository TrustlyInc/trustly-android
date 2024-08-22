package net.trustly.android.sdk.util.api

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import net.trustly.android.sdk.data.Settings
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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
import java.util.Calendar


class APIRequestManagerTest {

    @Mock
    lateinit var mockedEdit: SharedPreferences.Editor

    @Mock
    lateinit var mockedPrefs: SharedPreferences

    @Mock
    lateinit var mockedContext: Context

    @Mock
    lateinit var mockedCalendar: Calendar

    @Before
    fun setUp() {
        mockedEdit = mock(SharedPreferences.Editor::class.java)
        mockedPrefs = mock(SharedPreferences::class.java)
        mockedContext = mock(Context::class.java)
        mockedCalendar = mock(Calendar::class.java)
    }

    @After
    fun tearDown() {
        clearInvocations(mockedEdit, mockedContext, mockedPrefs)
    }

    @Test
    fun testValidateAPIRequestReturnTrue() {
        `when`(mockedEdit.putString(anyString(), anyString())).thenReturn(mockedEdit)
        `when`(mockedPrefs.edit()).thenReturn(mockedEdit)
        `when`(mockedContext.getSharedPreferences("API_STORAGE", Context.MODE_PRIVATE)).thenReturn(mockedPrefs)

        mockStatic(Calendar::class.java, CALLS_REAL_METHODS).use { static ->
            static.`when`<Any> { Calendar.getInstance() }
                .thenReturn(mockedCalendar)
        }

        mockStatic(APIRequestStorage::class.java, CALLS_REAL_METHODS).use { static ->
            static.`when`<Any> { APIRequestStorage.readDataFrom(mockedContext, "API_REQUEST") }
                .thenReturn("1724337021051")
        }

        val validateAPIRequest = APIRequestManager.validateAPIRequest(mockedContext)

        assertTrue(validateAPIRequest)
    }

    @Test
    fun testValidateAPIRequestReturnFalse() {
        `when`(mockedEdit.putString(anyString(), anyString())).thenReturn(mockedEdit)
        `when`(mockedPrefs.edit()).thenReturn(mockedEdit)
        `when`(mockedContext.getSharedPreferences("API_STORAGE", Context.MODE_PRIVATE)).thenReturn(mockedPrefs)

        mockStatic(Calendar::class.java, CALLS_REAL_METHODS).use { static ->
            static.`when`<Any> { Calendar.getInstance() }
                .thenReturn(mockedCalendar)
        }

        mockStatic(APIRequestStorage::class.java, CALLS_REAL_METHODS).use { static ->
            static.`when`<Any> { APIRequestStorage.readDataFrom(mockedContext, "API_REQUEST") }
                .thenReturn(null)
        }

        val validateAPIRequest = APIRequestManager.validateAPIRequest(mockedContext)

        assertFalse(validateAPIRequest)
    }

    @Test
    fun testGettingDataAPIRequest() {
        `when`(mockedContext.getSharedPreferences("API_STORAGE", Context.MODE_PRIVATE)).thenReturn(mockedPrefs)

        mockStatic(APIRequestStorage::class.java, CALLS_REAL_METHODS).use { static ->
            static.`when`<Any> { APIRequestStorage.readDataFrom(mockedContext, "API_REQUEST") }
                .thenReturn("1724258668")
        }

        val result = APIRequestManager.getAPIRequest(mockedContext)
        assertEquals("1724258668", result)
    }

    @Test
    fun testGettingDataAPIRequestSettings() {
        val output = getSettingsMockData()
        `when`(mockedContext.getSharedPreferences("API_STORAGE", Context.MODE_PRIVATE)).thenReturn(mockedPrefs)

        mockStatic(APIRequestStorage::class.java, CALLS_REAL_METHODS).use { static ->
            static.`when`<Any> { APIRequestStorage.readDataFrom(mockedContext, "API_REQUEST_SETTINGS") }
                .thenReturn(output)
        }

        val settings = APIRequestManager.getAPIRequestSettings(mockedContext)
        assertEquals("in-app-browser", settings.settings.lightbox.context)
    }

    @Test
    fun testSavingDataAPIRequestSettings() {
        `when`(mockedEdit.putString(anyString(), anyString())).thenReturn(mockedEdit)
        `when`(mockedPrefs.edit()).thenReturn(mockedEdit)
        `when`(mockedContext.getSharedPreferences("API_STORAGE", Context.MODE_PRIVATE)).thenReturn(mockedPrefs)

        val settings = Gson().fromJson(getSettingsMockData(), Settings::class.java)
        APIRequestManager.saveAPIRequestSettings(mockedContext, settings)

        verify(mockedEdit, times(1)).putString("API_REQUEST_SETTINGS", "{\"settings\":{\"lightbox\":{\"context\":\"in-app-browser\"}}}")
        verify(mockedEdit, times(1)).apply()
    }

    private fun getSettingsMockData() = "{\"settings\":{\"lightbox\":{\"context\":\"in-app-browser\"}}}"

}