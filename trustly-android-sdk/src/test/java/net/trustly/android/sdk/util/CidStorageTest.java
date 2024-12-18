package net.trustly.android.sdk.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import net.trustly.android.sdk.util.cid.CidStorage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CidStorageTest {

    private static final String SESSION_CID = "SESSION_CID";
    private static final String CID_STORAGE = "CID_STORAGE";
    private static final String SESSION_CID_VALUE = "1724258668";

    @Mock
    private SharedPreferences.Editor mockSharedPreferencesEditor;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private Context mockContext;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor);
        when(mockContext.getSharedPreferences(CID_STORAGE, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
    }

    @After
    public void tearDown() {
        clearInvocations(mockSharedPreferencesEditor, mockSharedPreferences, mockContext);
    }

    @Test
    public void shouldValidateCidStorageSaveDataMethod() {
        when(mockSharedPreferencesEditor.putString(anyString(), anyString())).thenReturn(mockSharedPreferencesEditor);

        CidStorage.saveData(mockContext, SESSION_CID, SESSION_CID_VALUE);

        verify(mockSharedPreferencesEditor, times(1)).putString(SESSION_CID, SESSION_CID_VALUE);
        verify(mockSharedPreferencesEditor, times(1)).apply();
        verify(mockSharedPreferences, times(1)).edit();
    }

    @Test
    public void shouldValidateCidStorageReadDataFromMethodWithResult() {
        when(mockSharedPreferences.getString(anyString(), eq(null))).thenReturn(SESSION_CID_VALUE);

        String result = CidStorage.readDataFrom(mockContext, SESSION_CID);

        verify(mockSharedPreferences, times(1)).getString(SESSION_CID, null);
        verify(mockSharedPreferences, times(0)).edit();
        assertEquals(SESSION_CID_VALUE, result);
    }

    @Test
    public void shouldValidateCidStorageReadDataFromMethodWithNullResult() {
        when(mockSharedPreferences.getString(anyString(), eq(null))).thenReturn(null);

        String result = CidStorage.readDataFrom(mockContext, SESSION_CID);

        verify(mockSharedPreferences, times(1)).getString(SESSION_CID, null);
        verify(mockSharedPreferences, times(0)).edit();
        assertNull(result);
    }

    @Test
    public void shouldValidateCidStorageReadDataFromMethodWithNullKey() {
        String result = CidStorage.readDataFrom(mockContext, null);

        verify(mockSharedPreferences, times(1)).getString(null, null);
        verify(mockSharedPreferences, times(0)).edit();
        assertNull(result);
    }

}