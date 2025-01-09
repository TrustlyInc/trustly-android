package net.trustly.android.sdk.util.cid

import android.content.Context
import android.content.SharedPreferences
import net.trustly.android.sdk.util.cid.CidStorage.readDataFrom
import net.trustly.android.sdk.util.cid.CidStorage.saveData
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CidStorageTest {

    private val SESSION_CID: String = "SESSION_CID"
    private val CID_STORAGE: String = "CID_STORAGE"
    private val SESSION_CID_VALUE: String = "1724258668"

    @Mock
    private lateinit var mockSharedPreferencesEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        `when`(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor)
        `when`(mockContext.getSharedPreferences(CID_STORAGE, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
    }

    @After
    fun tearDown() {
        clearInvocations(mockSharedPreferencesEditor, mockSharedPreferences, mockContext)
    }

    @Test
    fun shouldValidateCidStorageSaveDataMethod() {
        `when`(mockSharedPreferencesEditor.putString(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(mockSharedPreferencesEditor)

        saveData(mockContext, SESSION_CID, SESSION_CID_VALUE)

        verify(mockSharedPreferencesEditor, times(1)).putString(SESSION_CID, SESSION_CID_VALUE)
        verify(mockSharedPreferencesEditor, times(1)).apply()
        verify(mockSharedPreferences, times(1)).edit()
    }

    @Test
    fun shouldValidateCidStorageReadDataFromMethodWithResult() {
        `when`<String>(mockSharedPreferences.getString(ArgumentMatchers.anyString(), ArgumentMatchers.eq<String>(null))).thenReturn(SESSION_CID_VALUE)

        val result = readDataFrom(mockContext, SESSION_CID)

        verify(mockSharedPreferences, times(1)).getString(SESSION_CID, null)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(SESSION_CID_VALUE, result)
    }

    @Test
    fun shouldValidateCidStorageReadDataFromMethodWithNullResult() {
        `when`(mockSharedPreferences.getString(ArgumentMatchers.anyString(), ArgumentMatchers.eq<String>(null))).thenReturn(null)

        val result = readDataFrom(mockContext, SESSION_CID)

        verify(mockSharedPreferences, times(1)).getString(SESSION_CID, null)
        verify(mockSharedPreferences, times(0)).edit()
        assertNull(result)
    }

    @Test
    fun shouldValidateCidStorageReadDataFromMethodWithNullKey() {
        val result = readDataFrom(mockContext, null)

        verify(mockSharedPreferences, times(1)).getString(null, null)
        verify(mockSharedPreferences, times(0)).edit()
        assertNull(result)
    }

}