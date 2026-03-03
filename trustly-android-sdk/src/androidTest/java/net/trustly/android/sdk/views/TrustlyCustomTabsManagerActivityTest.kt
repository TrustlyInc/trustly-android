package net.trustly.android.sdk.views

import android.content.Intent
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
import java.io.Serializable

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyCustomTabsManagerActivityTest : TrustlyActivityTest() {

    private lateinit var trustlyEvents: TrustlyEvents

    @Before
    override fun setUp() {
        super.setUp()

        trustlyEvents = TrustlyEventsImpl
    }

    @Test
    fun shouldValidateCustomTabsManagerActivityOpenCustomTabsIntentMethod() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyCustomTabsManagerActivity().apply {
                setEventsCallback(TrustlyView(activity.applicationContext), trustlyEvents)
                startIntent(activity, "http://www.url.com")
            }
            Assert.assertEquals(
                11,
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
                11,
                TrustlyCustomTabsManagerActivity::class.java.declaredMethods.size
            )
        }
        waitToCloseCustomTabs()
    }

    @Test
    fun shouldValidateCustomTabsManagerActivityOpenCustomTabsIntentMethodWithEstablishData() {
        scenario.onActivity { activity: MockActivity ->
            val establishData = HashMap<String, String>()
            establishData["accessId"] = "123456"
            establishData["merchantId"] = "654321"

            val intent = Intent(activity, TrustlyCustomTabsManagerActivity::class.java)
                .putExtra(
                    TrustlyCustomTabsManagerActivity.ESTABLISH_DATA,
                    establishData as Serializable
                )
            activity.startActivity(intent)
            Assert.assertEquals(
                11,
                TrustlyCustomTabsManagerActivity::class.java.declaredMethods.size
            )
        }
        waitToCloseCustomTabs()
    }

    @Test
    fun shouldValidateCustomTabsManagerActivityOpenCustomTabsIntentMethodWithSuccessStatusEstablishData() {
        scenario.onActivity { activity: MockActivity ->
            val establishData = HashMap<String, String>()
            establishData["accessId"] = "123456"
            establishData["merchantId"] = "654321"
            establishData["status"] = "2"

            val intent = Intent(activity, TrustlyCustomTabsManagerActivity::class.java)
                .putExtra(
                    TrustlyCustomTabsManagerActivity.ESTABLISH_DATA,
                    establishData as Serializable
                )
            activity.startActivity(intent)
            Assert.assertEquals(
                11,
                TrustlyCustomTabsManagerActivity::class.java.declaredMethods.size
            )
        }
        waitToCloseCustomTabs()
    }

    @Test
    fun shouldValidateCustomTabsManagerActivityOpenCustomTabsIntentMethodWithSuccessStatusEstablishDataAndTrustlyContext() {
        scenario.onActivity { activity: MockActivity ->
            val establishData = HashMap<String, String>()
            establishData["accessId"] = "123456"
            establishData["merchantId"] = "654321"
            establishData["status"] = "2"
            establishData["trustlyContext"] = "MTIzNDU2"

            val intent = Intent(activity, TrustlyCustomTabsManagerActivity::class.java)
                .putExtra(
                    TrustlyCustomTabsManagerActivity.ESTABLISH_DATA,
                    establishData as Serializable
                )
            activity.startActivity(intent)
            Assert.assertEquals(
                11,
                TrustlyCustomTabsManagerActivity::class.java.declaredMethods.size
            )
        }
        waitToCloseCustomTabs()
    }

}