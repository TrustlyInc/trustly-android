package net.trustly.android.sdk.interfaces

import net.trustly.android.sdk.views.TrustlyView
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class TrustlyJsInterfaceTest {

    private val EVENT_NAME: String = "event"

    @Mock
    private lateinit var mockTrustlyView: TrustlyView

    private lateinit var trustlyJsInterface: TrustlyJsInterface

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        trustlyJsInterface = TrustlyJsInterface(mockTrustlyView)
    }

    @After
    fun tearDown() {
        clearInvocations(mockTrustlyView)
    }

    @Test
    fun shouldValidateTrustlyJsInterfaceInstanceIsNotNull() {
        assertNotNull(trustlyJsInterface)
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithoutMessage() {
        trustlyJsInterface.postMessage(null)
        verify(mockTrustlyView, times(0)).notifyListener(
            EVENT_NAME, HashMap<String, String>()
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageNullCommand() {
        trustlyJsInterface.postMessage("|event")
        verify(mockTrustlyView, times(0)).notifyListener(
            EVENT_NAME, HashMap<String, String>()
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageNullCommandAndNullEvent() {
        trustlyJsInterface.postMessage("|")
        verify(mockTrustlyView, times(0)).notifyListener(
            EVENT_NAME, HashMap<String, String>()
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithEmptyMessage() {
        trustlyJsInterface.postMessage("")
        verify(mockTrustlyView, times(0)).notifyListener(
            EVENT_NAME, HashMap<String, String>()
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithNoDividerMessage() {
        trustlyJsInterface.postMessage("event")
        verify(mockTrustlyView, times(0)).notifyListener(
            EVENT_NAME, HashMap<String, String>()
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithNoValidEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.eventNotValid|event")
        verify(mockTrustlyView, times(0)).notifyListener(
            EVENT_NAME, HashMap<String, String>()
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithValidEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event")
        verify(mockTrustlyView, times(1)).notifyListener(
            EVENT_NAME, HashMap<String, String>()
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithNullEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|null")
        verify(mockTrustlyView, times(1)).notifyListener(
            EVENT_NAME, HashMap<String, String>()
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithNoPassedEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|")
        verify(mockTrustlyView).notifyListener(EVENT_NAME, HashMap<String, String>())
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithAllValidEvents() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com|123456|47d7-89d3-9628d4cfb65e|bank_selected|100021|123")
        verify(mockTrustlyView, times(1)).notifyListener(
            EVENT_NAME, getAllEventNames()
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithoutPageEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event||123456|47d7-89d3-9628d4cfb65e|bank_selected|100021|123")
        verify(mockTrustlyView, times(1)).notifyListener(
            EVENT_NAME, getAllOtherEventNames("page")
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithoutTransactionIdEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com||47d7-89d3-9628d4cfb65e|bank_selected|100021|123")
        verify(mockTrustlyView, times(1)).notifyListener(
            EVENT_NAME, getAllOtherEventNames("transactionId")
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithoutMerchantReferenceEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com|123456||bank_selected|100021|123")
        verify(mockTrustlyView, times(1)).notifyListener(
            EVENT_NAME, getAllOtherEventNames("merchantReference")
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithoutTypeEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com|123456|47d7-89d3-9628d4cfb65e||100021|123")
        verify(mockTrustlyView, times(1)).notifyListener(
            EVENT_NAME, getAllOtherEventNames("type")
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithoutDataEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com|123456|47d7-89d3-9628d4cfb65e|bank_selected||123")
        verify(mockTrustlyView, times(1)).notifyListener(
            EVENT_NAME, getAllOtherEventNames("data")
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfacePostMessageWithoutTransferEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com|123456|47d7-89d3-9628d4cfb65e|bank_selected|100021|")
        verify(mockTrustlyView, times(1)).notifyListener(
            EVENT_NAME, getAllOtherEventNames("transfer")
        )
    }

    @Test
    fun shouldValidateTrustlyJsInterfaceResize() {
        trustlyJsInterface.resize(100f, 100f)
        verify(mockTrustlyView, times(1)).resize(100f, 100f)
    }

    @Test
    fun shouldValidateTrustlyJsInterfaceAddToListenerDetailsWithAllNullValues() {
        val eventDetails = HashMap<String, String>()
        trustlyJsInterface.addToListenerDetails(null, -1, null, eventDetails)
        assertTrue(eventDetails.isEmpty())
    }

    @Test
    fun shouldValidateTrustlyJsInterfaceAddToListenerDetailsInvalidParamsValue() {
        val eventDetails = HashMap<String, String>()
        trustlyJsInterface.addToListenerDetails(null, 1, EVENT_NAME, eventDetails)
        assertTrue(eventDetails.isEmpty())
    }

    @Test
    fun shouldValidateTrustlyJsInterfaceAddToListenerDetailsInvalidIndexValue() {
        val eventDetails = HashMap<String, String>()
        trustlyJsInterface.addToListenerDetails(arrayOf(), 10, EVENT_NAME, eventDetails)
        assertTrue(eventDetails.isEmpty())
    }

    @Test
    fun shouldValidateTrustlyJsInterfaceAddToListenerDetailsInvalidEventNameValue() {
        val eventDetails = HashMap<String, String>()
        trustlyJsInterface.addToListenerDetails(arrayOf(), 10, null, eventDetails)
        assertTrue(eventDetails.isEmpty())
    }

    @Test
    fun shouldValidateTrustlyJsInterfaceAddToListenerDetailsInvalidEventDetailsValue() {
        val eventDetails: HashMap<String, String>? = null
        trustlyJsInterface.addToListenerDetails(arrayOf(), 10, null, eventDetails)
        assertNull(eventDetails)
    }

    @Test
    fun shouldValidateTrustlyJsInterfaceAddToListenerDetailsInvalidParamsWithNullStringValue() {
        val eventDetails = HashMap<String, String>()
        trustlyJsInterface.addToListenerDetails(arrayOf("null"), 0, EVENT_NAME, eventDetails)
        assertTrue(eventDetails.isEmpty())
    }

    private fun getAllEventNames() = hashMapOf(
        "page" to "http://www.url.com",
        "transactionId" to "123456",
        "merchantReference" to "47d7-89d3-9628d4cfb65e",
        "type" to "bank_selected",
        "data" to "100021",
        "transfer" to "123"
    )

    private fun getAllOtherEventNames(eventName: String): HashMap<String, String> {
        val eventNames = getAllEventNames()
        eventNames.remove(eventName)
        return eventNames
    }

}