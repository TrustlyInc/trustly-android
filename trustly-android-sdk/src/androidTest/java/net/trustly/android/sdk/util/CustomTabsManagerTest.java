package net.trustly.android.sdk.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import net.trustly.android.sdk.TrustlyActivityTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CustomTabsManagerTest extends TrustlyActivityTest {

    private static final String URL = "http://www.trustly.com";

    @Test
    public void shouldThrowExceptionWhenCustomTabsManagerInstanceIsCalled() {
        scenario.onActivity(activity -> {
            Throwable exception = assertThrows(IllegalStateException.class, CustomTabsManager::new);
            assertEquals("Utility class cannot be instantiated", exception.getMessage());
        });
    }

    @Test(expected = NullPointerException.class)
    public void shouldValidateCustomTabsManagerOpenCustomTabsIntentMethodWithNullContext() {
        scenario.onActivity(activity -> CustomTabsManager.openCustomTabsIntent(null, URL));
    }

    @Test
    public void shouldValidateCustomTabsManagerOpenCustomTabsIntentMethod() {
        scenario.onActivity(activity -> {
            CustomTabsManager.openCustomTabsIntent(activity, URL);
            assertEquals(4, CustomTabsManager.class.getDeclaredMethods().length);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        scenario.close();
    }

}
