package net.trustly.android.sdk.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.views.TrustlyCustomTabsManager
import net.trustly.android.sdk.views.TrustlyCustomTabsManager.openCustomTabsIntent
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyCustomTabsManagerTest : TrustlyActivityTest() {

    private val URL: String = "http://www.trustly.com"

    @Test(expected = NullPointerException::class)
    fun shouldValidateCustomTabsManagerOpenCustomTabsIntentMethodWithNullContext() {
        scenario.onActivity { _: MockActivity -> openCustomTabsIntent(null, URL) }
    }

    @Test
    fun shouldValidateCustomTabsManagerOpenCustomTabsIntentMethod() {
        scenario.onActivity { activity: MockActivity ->
            openCustomTabsIntent(activity, URL)
            assertEquals(5, TrustlyCustomTabsManager::class.java.declaredMethods.size)
        }
        TimeUnit.SECONDS.sleep(1L)
        scenario.close()
    }

}