package net.trustly.android.sdk.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.clearInvocations;

import android.app.AlertDialog;
import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CustomTabsManagerTest {

    private static final String URL = "http://www.url.com";

    @Mock
    private Context mockContext;

    @Mock
    private AlertDialog.Builder mockAlertDialog;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() {
        clearInvocations(mockContext, mockAlertDialog);
    }

    @Test
    public void shouldThrowExceptionWhenCustomTabsManagerInstanceIsCalled() {
        Throwable exception = assertThrows(IllegalStateException.class, CustomTabsManager::new);
        assertEquals("Utility class cannot be instantiated", exception.getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void shouldValidateCustomTabsManagerOpenCustomTabsIntentMethodWithNullContext() {
        CustomTabsManager.openCustomTabsIntent(null, URL);
    }

    @Test
    public void shouldValidateCustomTabsManagerOpenCustomTabsIntentMethod() {
        CustomTabsManager.openCustomTabsIntent(mockContext, URL);
    }

}