package net.trustly.android.sdk.util;

import static org.mockito.Mockito.clearInvocations;

import android.content.SharedPreferences;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CidStorageTest {

    @Mock
    private SharedPreferences.Editor mockSharedPreferencesEditor;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() {
        clearInvocations(mockSharedPreferencesEditor, mockSharedPreferences);
    }

}