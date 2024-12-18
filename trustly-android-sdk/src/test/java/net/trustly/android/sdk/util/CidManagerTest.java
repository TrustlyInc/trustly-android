package net.trustly.android.sdk.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import net.trustly.android.sdk.util.cid.CidManager;
import net.trustly.android.sdk.util.cid.CidStorage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CidManagerTest {

    private static final String CID_PARAM = "CID";
    private static final String CID_STORAGE = "CID_STORAGE";
    private static final String SESSION_CID = "SESSION_CID";
    private static final String ANDROID_ID = "android_id";
    private static final String ANDROID_ID_VALUE = "1234-4CD5-0";
    private static final String CID_VALUE = "1234-4A94-0";
    private static final String SESSION_CID_VALUE_1 = "1234-4A94-";
    private static final String SESSION_CID_VALUE_2 = "1234-4A93-";

    @Mock
    private SharedPreferences.Editor mockSharedPreferencesEditor;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private Context mockContext;

    @Mock
    private Calendar mockCalendar;

    private MockedStatic<Settings.Secure> mockSettingsSecure;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockSharedPreferences.edit()).thenReturn(mockSharedPreferencesEditor);
        when(mockContext.getSharedPreferences(CID_STORAGE, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferencesEditor.putString(anyString(), anyString())).thenReturn(mockSharedPreferencesEditor);

        mockSettingsSecure = mockStatic(Settings.Secure.class);
    }

    @After
    public void tearDown() {
        mockSettingsSecure.close();
        clearInvocations(mockSharedPreferencesEditor, mockSharedPreferences, mockContext, mockCalendar);
    }

    @Test
    public void shouldValidateCidManagerGenerateCIDMethod() {
        Date timeNow = Calendar.getInstance().getTime();
        mockCalendar.setTime(timeNow);
        UUID mockUUID = UUID.fromString("bbcc4621-d88f-4a94-ae2f-b38072bf5087");

        mockStatic(UUID.class, CALLS_REAL_METHODS).when(UUID::randomUUID).thenReturn(mockUUID);
        mockStatic(Calendar.class, CALLS_REAL_METHODS).when(Calendar::getInstance).thenReturn(mockCalendar);
        mockSettingsSecure.when(() -> Settings.Secure.getString(mockContext.getContentResolver(), ANDROID_ID)).thenReturn(ANDROID_ID_VALUE);

        CidManager.generateCid(mockContext);

        verify(mockSharedPreferencesEditor, times(1)).putString("CID", CID_VALUE);
        verify(mockSharedPreferencesEditor, times(1)).apply();
        verify(mockSharedPreferences, times(1)).edit();
    }

    @Test
    public void shouldValidateCidManagerGetOrCreateSessionCidMethod() {
        String timeStampNow = Long.toString(Calendar.getInstance().getTimeInMillis(), 36);

        when(mockSharedPreferences.getString(anyString(), eq(null))).thenReturn(CID_VALUE);
        when(CidStorage.readDataFrom(mockContext, SESSION_CID)).thenReturn(SESSION_CID_VALUE_2 + timeStampNow);

        mockSettingsSecure.when(() -> Settings.Secure.getString(mockContext.getContentResolver(), ANDROID_ID)).thenReturn(ANDROID_ID_VALUE);

        Map<String, String> result = CidManager.getOrCreateSessionCid(mockContext);

        verify(mockSharedPreferencesEditor, times(1)).putString(SESSION_CID, SESSION_CID_VALUE_2 + timeStampNow);
        verify(mockSharedPreferences, times(1)).edit();
        assertNotEquals(getCIDParams(), result);
    }

    @Test
    public void shouldValidateCidManagerGetOrCreateSessionCidMoreThanOneHourMethod() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 2);
        mockCalendar.setTime(instance.getTime());

        String timeStampNow = Long.toString(mockCalendar.getTimeInMillis(), 36);

        when(mockSharedPreferences.getString(anyString(), eq(null))).thenReturn(CID_VALUE);
        when(CidStorage.readDataFrom(mockContext, SESSION_CID)).thenReturn(SESSION_CID_VALUE_1 + timeStampNow);

        mockSettingsSecure.when(() -> Settings.Secure.getString(mockContext.getContentResolver(), ANDROID_ID)).thenReturn(ANDROID_ID_VALUE);

        Map<String, String> result = CidManager.getOrCreateSessionCid(mockContext);

        verify(mockSharedPreferencesEditor, times(1)).putString(SESSION_CID, SESSION_CID_VALUE_1 + timeStampNow);
        verify(mockSharedPreferences, times(1)).edit();
        assertEquals(CID_VALUE, result.get(SESSION_CID));
    }

    @Test
    public void shouldValidateCidManagerGetOrCreateSessionCidNullValueMethod() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 2);
        mockCalendar.setTime(instance.getTime());

        when(mockSharedPreferences.getString(anyString(), eq(null))).thenReturn(CID_VALUE);
        when(CidStorage.readDataFrom(mockContext, SESSION_CID)).thenReturn(SESSION_CID_VALUE_1);

        mockSettingsSecure.when(() -> Settings.Secure.getString(mockContext.getContentResolver(), ANDROID_ID)).thenReturn(ANDROID_ID_VALUE);

        Map<String, String> result = CidManager.getOrCreateSessionCid(mockContext);

        verify(mockSharedPreferencesEditor, times(1)).putString(SESSION_CID, SESSION_CID_VALUE_1);
        verify(mockSharedPreferences, times(1)).edit();
        assertEquals(SESSION_CID_VALUE_1, result.get(SESSION_CID));
    }

    @Test
    public void shouldValidateCidManagerGetOrCreateSessionCidInvalidValueMethod() {
        when(mockSharedPreferences.getString(anyString(), eq(null))).thenReturn(CID_VALUE);
        when(CidStorage.readDataFrom(mockContext, SESSION_CID)).thenReturn(CID_VALUE);

        mockSettingsSecure.when(() -> Settings.Secure.getString(mockContext.getContentResolver(), ANDROID_ID)).thenReturn(ANDROID_ID_VALUE);

        Map<String, String> result = CidManager.getOrCreateSessionCid(mockContext);

        verify(mockSharedPreferencesEditor, times(0)).putString(SESSION_CID, CID_VALUE);
        verify(mockSharedPreferences, times(1)).edit();
        assertNotEquals(CID_VALUE, result.get(SESSION_CID));
    }

    @Test
    public void shouldValidateCidManagerGetOrCreateSessionCidReadNullSessionCidMethod() {
        when(mockSharedPreferences.getString(anyString(), eq(null))).thenReturn(null);
        when(CidStorage.readDataFrom(mockContext, CID_PARAM)).thenReturn(CID_VALUE);

        Map<String, String> result = CidManager.getOrCreateSessionCid(mockContext);

        assertEquals(getCIDParams(), result);
    }

    private Map<String, String> getCIDParams() {
        Map<String, String> cidParams = new HashMap<>();
        cidParams.put("CID", CID_VALUE);
        cidParams.put("SESSION_CID", CID_VALUE);
        return cidParams;
    }

}