package net.trustly.android.sdk.interfaces;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

public class TrustlyListenerTest {

    @Mock
    private TrustlyListener trustlyListener;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() {
        clearInvocations(trustlyListener);
    }

    @Test
    public void shouldValidateTrustlyListenerWhenEventAndDetailsArePassed() {
        HashMap<String, String> eventNames = new HashMap<>();
        eventNames.put("event1", "event1");
        eventNames.put("event2", "event2");
        new TrustlyListenerImpl(trustlyListener).sendEventChange("eventWithDetails", eventNames);
        verify(trustlyListener).onChange("eventWithDetails", eventNames);
    }

    @Test
    public void shouldValidateTrustlyListenerWhenEventAndNullDetailsArePassed() {
        new TrustlyListenerImpl(trustlyListener).sendEventChange("eventWithoutDetails", null);
        verify(trustlyListener).onChange("eventWithoutDetails", null);
    }

    private static class TrustlyListenerImpl {

        private final TrustlyListener trustlyListener;

        TrustlyListenerImpl(TrustlyListener trustlyListener) {
            this.trustlyListener = trustlyListener;
        }

        private void sendEventChange(String event, HashMap<String, String> eventDetails) {
            trustlyListener.onChange(event, eventDetails);
        }

    }

}