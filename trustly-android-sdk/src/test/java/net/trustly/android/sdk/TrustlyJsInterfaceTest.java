package net.trustly.android.sdk;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import net.trustly.android.sdk.views.TrustlyView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

public class TrustlyJsInterfaceTest {

    @Mock
    private TrustlyView trustlyView;

    private TrustlyJsInterface trustlyJsInterface;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        trustlyJsInterface = new TrustlyJsInterface(trustlyView);
    }

    @After
    public void tearDown() {
        clearInvocations(trustlyView);
    }

    @Test
    public void shouldValidateTrustlyJsInterfaceInstanceIsNotNull() {
        assertNotNull(trustlyJsInterface);
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithoutTrustlyViewInstance() {
        trustlyJsInterface = new TrustlyJsInterface(null);
        trustlyJsInterface.postMessage("PayWithMyBank.event|event");
        verify(trustlyView, times(0)).notifyListener("event", new HashMap<>());
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithoutMessage() {
        trustlyJsInterface.postMessage(null);
        verify(trustlyView, times(0)).notifyListener("event", new HashMap<>());
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageNullCommand() {
        trustlyJsInterface.postMessage("|event");
        verify(trustlyView, times(0)).notifyListener("event", new HashMap<>());
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageNullCommandAndNullEvent() {
        trustlyJsInterface.postMessage("|");
        verify(trustlyView, times(0)).notifyListener("event", new HashMap<>());
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithEmptyMessage() {
        trustlyJsInterface.postMessage("");
        verify(trustlyView, times(0)).notifyListener("event", new HashMap<>());
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithNoValidEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.eventNotValid|event");
        verify(trustlyView, times(0)).notifyListener("event", new HashMap<>());
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithValidEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event");
        verify(trustlyView, times(1)).notifyListener("event", new HashMap<>());
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithNullEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|null");
        verify(trustlyView, times(1)).notifyListener("event", new HashMap<>());
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithNoPassedEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|");
        verify(trustlyView).notifyListener("event", new HashMap<>());
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithAllValidEvents() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com|123456|47d7-89d3-9628d4cfb65e|bank_selected|100021|123");
        verify(trustlyView, times(1)).notifyListener("event", getAllEventNames());
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithoutPageEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event||123456|47d7-89d3-9628d4cfb65e|bank_selected|100021|123");
        verify(trustlyView, times(1)).notifyListener("event", getAllOtherEventNames("page"));
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithoutTransactionIdEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com||47d7-89d3-9628d4cfb65e|bank_selected|100021|123");
        verify(trustlyView, times(1)).notifyListener("event", getAllOtherEventNames("transactionId"));
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithoutMerchantReferenceEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com|123456||bank_selected|100021|123");
        verify(trustlyView, times(1)).notifyListener("event", getAllOtherEventNames("merchantReference"));
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithoutTypeEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com|123456|47d7-89d3-9628d4cfb65e||100021|123");
        verify(trustlyView, times(1)).notifyListener("event", getAllOtherEventNames("type"));
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithoutDataEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com|123456|47d7-89d3-9628d4cfb65e|bank_selected||123");
        verify(trustlyView, times(1)).notifyListener("event", getAllOtherEventNames("data"));
    }

    @Test
    public void shouldValidateTrustlyJsInterfacePostMessageWithoutTransferEvent() {
        trustlyJsInterface.postMessage("PayWithMyBank.event|event|http://www.url.com|123456|47d7-89d3-9628d4cfb65e|bank_selected|100021|");
        verify(trustlyView, times(1)).notifyListener("event", getAllOtherEventNames("transfer"));
    }

    @Test
    public void shouldValidateTrustlyJsInterfaceAddToListenerDetailsWithAllNullValues() {
        HashMap<String, String> eventDetails = new HashMap<>();
        trustlyJsInterface.addToListenerDetails(null, -1, null, eventDetails);
        assertTrue(eventDetails.isEmpty());
    }

    @Test
    public void shouldValidateTrustlyJsInterfaceAddToListenerDetailsInvalidParamsValue() {
        HashMap<String, String> eventDetails = new HashMap<>();
        trustlyJsInterface.addToListenerDetails(null, 1, "event", eventDetails);
        assertTrue(eventDetails.isEmpty());
    }

    @Test
    public void shouldValidateTrustlyJsInterfaceAddToListenerDetailsInvalidIndexValue() {
        HashMap<String, String> eventDetails = new HashMap<>();
        trustlyJsInterface.addToListenerDetails(new String[] {}, 10, "event", eventDetails);
        assertTrue(eventDetails.isEmpty());
    }

    @Test
    public void shouldValidateTrustlyJsInterfaceAddToListenerDetailsInvalidEventNameValue() {
        HashMap<String, String> eventDetails = new HashMap<>();
        trustlyJsInterface.addToListenerDetails(new String[] {}, 10, null, eventDetails);
        assertTrue(eventDetails.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldValidateTrustlyJsInterfaceAddToListenerDetailsInvalidEventDetailsValue() {
        HashMap<String, String> eventDetails = null;
        trustlyJsInterface.addToListenerDetails(new String[] {}, 10, null, eventDetails);
        assertTrue(eventDetails.isEmpty());
    }

    @Test
    public void shouldValidateTrustlyJsInterfaceAddToListenerDetailsInvalidParamsWithNullValue() {
        HashMap<String, String> eventDetails = new HashMap<>();
        trustlyJsInterface.addToListenerDetails(new String[] { null }, 0, "event", eventDetails);
        assertTrue(eventDetails.isEmpty());
    }

    @Test
    public void shouldValidateTrustlyJsInterfaceAddToListenerDetailsInvalidParamsWithNullStringValue() {
        HashMap<String, String> eventDetails = new HashMap<>();
        trustlyJsInterface.addToListenerDetails(new String[] { "null" }, 0, "event", eventDetails);
        assertTrue(eventDetails.isEmpty());
    }

    private HashMap<String, String> getAllEventNames() {
        HashMap<String, String> eventNames = new HashMap<>();
        eventNames.put("page", "http://www.url.com");
        eventNames.put("transactionId", "123456");
        eventNames.put("merchantReference", "47d7-89d3-9628d4cfb65e");
        eventNames.put("type", "bank_selected");
        eventNames.put("data", "100021");
        eventNames.put("transfer", "123");
        return eventNames;
    }

    private HashMap<String, String> getAllOtherEventNames(String eventName) {
        HashMap<String, String> eventNames = getAllEventNames();
        eventNames.remove(eventName);
        return eventNames;
    }

}