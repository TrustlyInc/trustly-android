package net.trustly.android.sdk.views

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.interfaces.TrustlyCallback
import net.trustly.android.sdk.interfaces.TrustlyEvents
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.views.events.TrustlyEventsImpl
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.io.Serializable

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyCustomTabsManagerActivityTest : TrustlyActivityTest() {

    @Mock
    private lateinit var mockTrustlyCallback: TrustlyCallback<Trustly, Map<String, String>>

    private lateinit var trustlyEvents: TrustlyEvents

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.openMocks(this)

        trustlyEvents = TrustlyEventsImpl
    }

    @After
    override fun tearDown() {
        super.tearDown()

        trustlyEvents.setOnCancelCallback(null)
        clearInvocations(mockTrustlyCallback)
    }

    @Test
    fun shouldValidateCustomTabsManagerActivityOpenCustomTabsIntentMethod() {
        scenario.onActivity { activity: MockActivity ->
            TrustlyCustomTabsManagerActivity().apply {
                setEventsCallback(TrustlyView(activity.applicationContext), trustlyEvents)
                startIntent(activity, "http://www.url.com")
            }
            Assert.assertEquals(
                13,
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
                13,
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
                13,
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
                13,
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
            establishData["trustlyContext"] = "eyJsYXN0VXNlZCI6eyJVUyI6IjEyMzQ1Njc4OSJ9fQ=="

            val intent = Intent(activity, TrustlyCustomTabsManagerActivity::class.java)
                .putExtra(
                    TrustlyCustomTabsManagerActivity.ESTABLISH_DATA,
                    establishData as Serializable
                )
            activity.startActivity(intent)
            Assert.assertEquals(
                13,
                TrustlyCustomTabsManagerActivity::class.java.declaredMethods.size
            )
        }
        waitToCloseCustomTabs()
    }

    @Test
    fun shouldValidateCustomTabsManagerActivityFiresOnCancelWhenDismissedWithoutRedirect() {
        trustlyEvents.setOnCancelCallback(mockTrustlyCallback)

        // ActivityScenario.launch()/moveToState() must run on the instrumentation thread,
        // not inside scenario.onActivity() (which runs on the activity's main thread).
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            TrustlyCustomTabsManagerActivity::class.java
        )
        ActivityScenario.launch<TrustlyCustomTabsManagerActivity>(intent).use { customTabsScenario ->
            customTabsScenario.onActivity { it.customTabsLaunched = true }

            customTabsScenario.moveToState(Lifecycle.State.CREATED)
            try {
                customTabsScenario.moveToState(Lifecycle.State.RESUMED)
            } catch (_: AssertionError) {
                // onRestart() calls finish(), so the framework aborts the resume and
                // destroys the activity instead of settling back at RESUMED.
            }
        }

        verify(mockTrustlyCallback, times(1)).handle(null, mapOf())
    }

    @Test
    fun shouldValidateCustomTabsManagerActivityFiresOnCancelExactlyOnceForEstablishData() {
        trustlyEvents.setOnCancelCallback(mockTrustlyCallback)

        val establishData = HashMap<String, String>()
        establishData["accessId"] = "123456"
        establishData["merchantId"] = "654321"
        establishData["status"] = "1"

        scenario.onActivity { activity: MockActivity ->
            val intent = Intent(activity, TrustlyCustomTabsManagerActivity::class.java)
                .putExtra(TrustlyCustomTabsManagerActivity.ESTABLISH_DATA, establishData as Serializable)
            activity.startActivity(intent)
        }
        waitToCloseCustomTabs()

        verify(mockTrustlyCallback, times(1)).handle(null, establishData)
    }

}