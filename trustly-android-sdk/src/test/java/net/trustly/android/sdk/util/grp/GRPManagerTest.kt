package net.trustly.android.sdk.util.grp

import android.content.Context
import android.content.SharedPreferences
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GRPManagerTest {

    private val GRP_STORAGE: String = "PayWithMyBank"
    private val GRP: String = "grp"

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
        `when`(mockContext.getSharedPreferences(GRP_STORAGE, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        `when`(mockSharedPreferencesEditor.putInt(anyString(), anyInt())).thenReturn(mockSharedPreferencesEditor)
    }

    @After
    fun tearDown() {
        clearInvocations(mockSharedPreferencesEditor, mockSharedPreferences, mockContext)
    }

    @Test
    fun shouldValidateGRPManagerSaveGRPMethod() {
        GRPManager.saveGRP(mockContext, 20)

        verify(mockSharedPreferencesEditor, times(1)).putInt(GRP, 20)
        verify(mockSharedPreferencesEditor, times(1)).apply()
        verify(mockSharedPreferences, times(1)).edit()
    }

    @Test
    fun shouldValidateCidManagerGetOrCreateSessionCidMethod() {
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(23)

        val result = GRPManager.getGRP(mockContext)

        verify(mockSharedPreferences, times(1)).getInt(GRP, -1)
        verify(mockSharedPreferences, times(0)).edit()
        assertEquals(23, result)
    }

}