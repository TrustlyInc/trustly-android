package net.trustly.android.sdk.util.cid

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Secure
import net.trustly.android.sdk.util.cid.CidStorage.readDataFrom
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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
import java.util.Calendar
import java.util.UUID

class CidManagerTest {

    companion object {

        const val CID_PARAM: String = "CID"
        const val CID_STORAGE: String = "CID_STORAGE"
        const val SESSION_CID: String = "SESSION_CID"
        const val ANDROID_ID: String = "android_id"
        const val ANDROID_ID_VALUE: String = "1234-4CD5-0"
        const val CID_VALUE: String = "1234-4A94-0"
        const val SESSION_CID_LESS_VALUE: String = "1234-4A94"
        const val SESSION_CID_VALUE: String = "1234-4A94-0"
        const val SESSION_CID_VALUE_1: String = "1234-4A94-"
        const val SESSION_CID_VALUE_2: String = "1234-4A93-"

    }

    @Mock
    private lateinit var mockSharedPreferencesEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockCalendar: Calendar

    private lateinit var mockSettingsSecure: MockedStatic<Secure>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        `when`(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor)
        `when`(mockContext.getSharedPreferences(CID_STORAGE, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        `when`(mockSharedPreferencesEditor.putString(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(mockSharedPreferencesEditor)

        mockSettingsSecure = mockStatic(Secure::class.java)
    }

    @After
    fun tearDown() {
        mockSettingsSecure.close()
        clearInvocations(mockSharedPreferencesEditor, mockSharedPreferences, mockContext, mockCalendar)
    }

    @Test
    fun shouldValidateCidManagerGenerateCIDMethod() {
        val timeNow = Calendar.getInstance().time
        mockCalendar.time = timeNow
        val mockUUID = UUID.fromString("bbcc4621-d88f-4a94-ae2f-b38072bf5087")

        mockStatic(UUID::class.java, CALLS_REAL_METHODS).`when`<Any> { UUID.randomUUID() }.thenReturn(mockUUID)
        mockStatic(Calendar::class.java, CALLS_REAL_METHODS).`when`<Any> { Calendar.getInstance() }.thenReturn(mockCalendar)
        mockSettingsSecure.`when`<Any> { Secure.getString(mockContext.contentResolver, ANDROID_ID) }.thenReturn(ANDROID_ID_VALUE)

        CidManager.generateCid(mockContext)

        verify(mockSharedPreferencesEditor, times(1)).putString(CID_PARAM, CID_VALUE)
        verify(mockSharedPreferencesEditor, times(1)).apply()
        verify(mockSharedPreferences, times(1)).edit()
    }

    @Test
    fun shouldValidateCidManagerGetOrCreateSessionCidMethod() {
        val timeStampNow = Calendar.getInstance().timeInMillis.toString(36)

        `when`<String>(mockSharedPreferences.getString(ArgumentMatchers.anyString(), ArgumentMatchers.eq<String>(null))).thenReturn(CID_VALUE)
        `when`<String>(readDataFrom(mockContext, SESSION_CID)).thenReturn(SESSION_CID_VALUE_2 + timeStampNow)

        mockSettingsSecure.`when`<Any> { Secure.getString(mockContext.contentResolver, ANDROID_ID) }.thenReturn(ANDROID_ID_VALUE)

        val result = CidManager.getOrCreateSessionCid(mockContext)

        verify(mockSharedPreferencesEditor, times(1)).putString(SESSION_CID, SESSION_CID_VALUE_2 + timeStampNow)
        verify(mockSharedPreferences, times(1)).edit()
        assertNotEquals(getCIDParams(), result)
    }

    @Test
    fun shouldValidateCidManagerGetOrCreateSessionCidMoreThanOneHourMethod() {
        val instance = Calendar.getInstance()
        instance.add(Calendar.DATE, 2)
        mockCalendar.time = instance.time

        val timeStampNow = mockCalendar.timeInMillis.toString(36)

        `when`<String>(mockSharedPreferences.getString(ArgumentMatchers.anyString(), ArgumentMatchers.eq<String>(null))).thenReturn(CID_VALUE)
        `when`<String>(readDataFrom(mockContext, SESSION_CID)).thenReturn(SESSION_CID_VALUE_1 + timeStampNow)

        mockSettingsSecure.`when`<Any> { Secure.getString(mockContext.contentResolver, ANDROID_ID) }.thenReturn(ANDROID_ID_VALUE)

        val result = CidManager.getOrCreateSessionCid(mockContext)

        verify(mockSharedPreferencesEditor, times(1)).putString(SESSION_CID, SESSION_CID_VALUE_1 + timeStampNow)
        verify(mockSharedPreferences, times(1)).edit()
        assertEquals(CID_VALUE, result[SESSION_CID])
    }

    @Test
    fun shouldValidateCidManagerGetOrCreateSessionCidNullValueMethod() {
        val instance = Calendar.getInstance()
        instance.add(Calendar.DATE, 2)
        mockCalendar.time = instance.time

        `when`<String>(mockSharedPreferences.getString(ArgumentMatchers.anyString(), ArgumentMatchers.eq<String>(null))).thenReturn(CID_VALUE)
        `when`<String>(readDataFrom(mockContext, SESSION_CID)).thenReturn(SESSION_CID_VALUE)

        mockSettingsSecure.`when`<Any> { Secure.getString(mockContext.contentResolver, ANDROID_ID) }.thenReturn(ANDROID_ID_VALUE)

        val result = CidManager.getOrCreateSessionCid(mockContext)

        verify(mockSharedPreferencesEditor, times(1)).putString(SESSION_CID, SESSION_CID_VALUE)
        verify(mockSharedPreferences, times(1)).edit()
        assertEquals(SESSION_CID_VALUE, result[SESSION_CID])
    }

    @Test
    fun shouldValidateCidManagerGetOrCreateSessionCidInvalidValueMethod() {
        `when`<String>(mockSharedPreferences.getString(ArgumentMatchers.anyString(), ArgumentMatchers.eq<String>(null))).thenReturn(CID_VALUE)
        `when`<String>(readDataFrom(mockContext, SESSION_CID)).thenReturn(CID_VALUE)

        mockSettingsSecure.`when`<Any> { Secure.getString(mockContext.contentResolver, ANDROID_ID) }.thenReturn(ANDROID_ID_VALUE)

        val result = CidManager.getOrCreateSessionCid(mockContext)

        verify(mockSharedPreferencesEditor, times(0)).putString(SESSION_CID, CID_VALUE)
        verify(mockSharedPreferences, times(1)).edit()
        assertNotEquals(CID_VALUE, result[SESSION_CID])
    }

    @Test
    fun shouldValidateCidManagerGetOrCreateSessionCidReadNullSessionCidMethod() {
        `when`(mockSharedPreferences.getString(ArgumentMatchers.anyString(), ArgumentMatchers.eq<String>(null))).thenReturn(null)
        `when`<String>(readDataFrom(mockContext, CID_PARAM)).thenReturn(CID_VALUE)

        val result = CidManager.getOrCreateSessionCid(mockContext)

        assertEquals(getCIDParams(), result)
    }

    @Test
    fun shouldValidateCidManagerGetOrCreateSessionCidMethodWithSessionCidLessValue() {
        `when`<String>(mockSharedPreferences.getString(ArgumentMatchers.anyString(), ArgumentMatchers.eq<String>(null))).thenReturn(CID_VALUE)
        `when`<String>(readDataFrom(mockContext, SESSION_CID)).thenReturn(SESSION_CID_LESS_VALUE)

        mockSettingsSecure.`when`<Any> { Secure.getString(mockContext.contentResolver, ANDROID_ID) }.thenReturn(ANDROID_ID_VALUE)

        val result = CidManager.getOrCreateSessionCid(mockContext)

        verify(mockSharedPreferencesEditor, times(1)).putString(SESSION_CID, SESSION_CID_LESS_VALUE)
        verify(mockSharedPreferences, times(1)).edit()
        assertNotEquals(getCIDParams(), result)
    }

    private fun getCIDParams() = hashMapOf(
        CID_PARAM to CID_VALUE,
        SESSION_CID to CID_VALUE
    )

}