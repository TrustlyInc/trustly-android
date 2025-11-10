package net.trustly.android.sdk.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.views.TrustlyCustomTabsManagerActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyCustomTabsManagerActivityTest : TrustlyActivityTest() {

    @Test
    fun shouldValidateCustomTabsManagerActivityOpenCustomTabsIntentMethod() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyCustomTabsManagerActivity.startIntent(
                activity,
                "http://www.url.com"
            )
            assertEquals(7, TrustlyCustomTabsManagerActivity::class.java.declaredMethods.size)
        }
        waitToCloseCustomTabs()
    }

}