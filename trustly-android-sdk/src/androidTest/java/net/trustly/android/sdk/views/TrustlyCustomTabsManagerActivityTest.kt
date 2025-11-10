package net.trustly.android.sdk.views

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.interfaces.TrustlyEvents
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.views.events.TrustlyEventsImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyCustomTabsManagerActivityTest : TrustlyActivityTest() {

    private lateinit var trustlyEvents: TrustlyEvents

    @Before
    override fun setUp() {
        super.setUp()

        trustlyEvents = TrustlyEventsImpl()
    }

    @Test
    fun shouldValidateCustomTabsManagerActivityOpenCustomTabsIntentMethod() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyCustomTabsManagerActivity().apply {
                setEventsCallback(TrustlyView(activity.applicationContext), trustlyEvents)
                startIntent(activity, "http://www.url.com")
            }
            Assert.assertEquals(
                10,
                TrustlyCustomTabsManagerActivity::class.java.declaredMethods.size
            )
        }
        waitToCloseCustomTabs()
    }

    @Test
    fun shouldValidateCustomTabsManagerActivityOpenCustomTabsIntentMethodWithNoUseWebView() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyCustomTabsManagerActivity().apply {
                setEventsCallback(TrustlyView(activity.applicationContext), trustlyEvents)
                startIntent(activity, "http://www.url.com", false)
            }
            Assert.assertEquals(
                10,
                TrustlyCustomTabsManagerActivity::class.java.declaredMethods.size
            )
        }
        waitToCloseCustomTabs()
    }

}