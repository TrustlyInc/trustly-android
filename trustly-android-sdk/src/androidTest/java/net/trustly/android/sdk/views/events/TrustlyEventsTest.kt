package net.trustly.android.sdk.views.events

import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.interfaces.TrustlyCallback
import net.trustly.android.sdk.interfaces.TrustlyListener
import net.trustly.android.sdk.views.TrustlyView
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class TrustlyEventsTest : TrustlyActivityTest() {

    @Mock
    private lateinit var mockTrustlyCallback: TrustlyCallback<Trustly, Map<String, String>>

    @Mock
    private lateinit var mockTrustlyListener: TrustlyListener

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.openMocks(this)
    }

    @After
    override fun tearDown() {
        super.tearDown()

        clearInvocations(mockTrustlyCallback, mockTrustlyListener)
    }

    @Test
    fun testTrustlyEventsHandleOnExternalUrl() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.setOnExternalUrlCallback(mockTrustlyCallback)
            trustlyEvents.handleOnExternalUrl(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(1)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun testTrustlyEventsHandleOnExternalUrlNull() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.handleOnExternalUrl(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(0)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun testTrustlyEventsHandleOnReturn() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.setOnReturnCallback(mockTrustlyCallback)
            trustlyEvents.handleOnReturn(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(1)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun testTrustlyEventsHandleOnReturnNull() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.handleOnReturn(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(0)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun testTrustlyEventsHandleOnCancel() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.setOnCancelCallback(mockTrustlyCallback)
            trustlyEvents.handleOnCancel(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(1)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun testTrustlyEventsHandleOnCancelNull() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.handleOnCancel(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(0)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun testTrustlyEventsGetOnWidgetBankSelectedCallback() {
        scenario.onActivity {
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.setOnWidgetBankSelectedCallback(mockTrustlyCallback)
            val result = trustlyEvents.getOnWidgetBankSelectedCallback()
            assertEquals(mockTrustlyCallback, result)
        }
    }

    @Test
    fun testTrustlyEventsNotifyListener() {
        scenario.onActivity {
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.setTrustlyListener(mockTrustlyListener)
            trustlyEvents.setOnWidgetBankSelectedCallback(mockTrustlyCallback)
            trustlyEvents.notifyListener("event", hashMapOf())
            verify(mockTrustlyListener, times(1)).onChange("event", hashMapOf())
        }
    }

    @Test
    fun testTrustlyEventsNotifyListenerNull() {
        scenario.onActivity {
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.notifyListener("event", hashMapOf())
            verify(mockTrustlyListener, times(0)).onChange("event", hashMapOf())
        }
    }

    @Test
    fun testTrustlyEventsNotifyOpen() {
        scenario.onActivity {
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.setTrustlyListener(mockTrustlyListener)
            trustlyEvents.notifyOpen()
            verify(mockTrustlyListener, times(1)).onChange("open", hashMapOf())
        }
    }

    @Test
    fun testTrustlyEventsNotifyClose() {
        scenario.onActivity {
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.setTrustlyListener(mockTrustlyListener)
            trustlyEvents.notifyClose()
            verify(mockTrustlyListener, times(1)).onChange("close", hashMapOf())
        }
    }

    @Test
    fun testTrustlyEventsNotifyWidgetLoading() {
        scenario.onActivity {
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.setTrustlyListener(mockTrustlyListener)
            trustlyEvents.notifyWidgetLoading()
            verify(mockTrustlyListener, times(1)).onChange("event", hashMapOf(
                "page" to "widget",
                "type" to "loading"
            ))
        }
    }

    @Test
    fun testTrustlyEventsNotifyWidgetLoaded() {
        scenario.onActivity {
            val trustlyEvents = TrustlyEvents()
            trustlyEvents.setTrustlyListener(mockTrustlyListener)
            trustlyEvents.notifyWidgetLoaded()
            verify(mockTrustlyListener, times(1)).onChange("event", hashMapOf(
                "page" to "widget",
                "type" to "load"
            ))
        }
    }

}