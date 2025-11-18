package net.trustly.android.sdk.views.events

import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.interfaces.TrustlyCallback
import net.trustly.android.sdk.interfaces.TrustlyEvents
import net.trustly.android.sdk.interfaces.TrustlyListener
import net.trustly.android.sdk.views.TrustlyView
import org.junit.After
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

        clearInvocations(mockTrustlyCallback, mockTrustlyListener)
    }

    @Test
    fun shouldValidateTrustlyEventsHandleOnExternalUrl() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            trustlyEvents.setOnExternalUrlCallback(mockTrustlyCallback)
            trustlyEvents.handleOnExternalUrl(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(1)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun shouldValidateTrustlyEventsHandleOnExternalUrlNull() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            trustlyEvents.handleOnExternalUrl(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(0)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun shouldValidateTrustlyEventsHandleOnReturn() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            trustlyEvents.setOnReturnCallback(mockTrustlyCallback)
            trustlyEvents.handleOnReturn(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(1)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun shouldValidateTrustlyEventsHandleOnReturnNull() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            trustlyEvents.handleOnReturn(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(0)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun shouldValidateTrustlyEventsHandleOnCancel() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            trustlyEvents.setOnCancelCallback(mockTrustlyCallback)
            trustlyEvents.handleOnCancel(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(1)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun shouldValidateTrustlyEventsHandleOnCancelNull() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            trustlyEvents.handleOnCancel(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(0)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun shouldValidateTrustlyEventsGetOnWidgetBankSelectedCallback() {
        scenario.onActivity { activity ->
            val trustlyView = TrustlyView(activity)
            trustlyEvents.setOnWidgetBankSelectedCallback(mockTrustlyCallback)
            trustlyEvents.handleOnWidgetBankSelected(trustlyView, mapOf())
            verify(mockTrustlyCallback, times(1)).handle(trustlyView, mapOf())
        }
    }

    @Test
    fun shouldValidateTrustlyEventsNotifyListener() {
        scenario.onActivity {
            trustlyEvents.setTrustlyListener(mockTrustlyListener)
            trustlyEvents.setOnWidgetBankSelectedCallback(mockTrustlyCallback)
            trustlyEvents.notifyListener("event", hashMapOf())
            verify(mockTrustlyListener, times(1)).onChange("event", hashMapOf())
        }
    }

    @Test
    fun shouldValidateTrustlyEventsNotifyListenerNull() {
        scenario.onActivity {
            trustlyEvents.notifyListener("event", hashMapOf())
            verify(mockTrustlyListener, times(0)).onChange("event", hashMapOf())
        }
    }

    @Test
    fun shouldValidateTrustlyEventsNotifyOpen() {
        scenario.onActivity {
            trustlyEvents.setTrustlyListener(mockTrustlyListener)
            trustlyEvents.notifyOpen()
            verify(mockTrustlyListener, times(1)).onChange("open", hashMapOf())
        }
    }

    @Test
    fun shouldValidateTrustlyEventsNotifyClose() {
        scenario.onActivity {
            trustlyEvents.setTrustlyListener(mockTrustlyListener)
            trustlyEvents.notifyClose()
            verify(mockTrustlyListener, times(1)).onChange("close", hashMapOf())
        }
    }

    @Test
    fun shouldValidateTrustlyEventsNotifyWidgetLoading() {
        scenario.onActivity {
            trustlyEvents.setTrustlyListener(mockTrustlyListener)
            trustlyEvents.notifyWidgetLoading()
            verify(mockTrustlyListener, times(1)).onChange("event", hashMapOf(
                "page" to "widget",
                "type" to "loading"
            ))
        }
    }

    @Test
    fun shouldValidateTrustlyEventsNotifyWidgetLoaded() {
        scenario.onActivity {
            trustlyEvents.setTrustlyListener(mockTrustlyListener)
            trustlyEvents.notifyWidgetLoaded()
            verify(mockTrustlyListener, times(1)).onChange("event", hashMapOf(
                "page" to "widget",
                "type" to "load"
            ))
        }
    }

}