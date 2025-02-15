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
    private TrustlyListener mockTrustlyListener;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() {
        clearInvocations(mockTrustlyListener);
    }

    @Test
    public void shouldValidateTrustlyListenerWhenEventAndDetailsArePassed() {
        HashMap<String, String> eventNames = new HashMap<>();
        eventNames.put("key1", "value1");
        eventNames.put("key2", "value2");
        new TrustlyListenerImpl(mockTrustlyListener).sendEventChange("eventWithDetails", eventNames);
        verify(mockTrustlyListener).onChange("eventWithDetails", eventNames);
    }

    @Test
    public void shouldValidateTrustlyListenerWhenEventAndNullDetailsArePassed() {
        new TrustlyListenerImpl(mockTrustlyListener).sendEventChange("eventWithoutDetails", null);
        verify(mockTrustlyListener).onChange("eventWithoutDetails", null);
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