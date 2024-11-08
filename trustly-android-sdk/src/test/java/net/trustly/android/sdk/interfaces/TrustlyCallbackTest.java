package net.trustly.android.sdk.interfaces;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

public class TrustlyCallbackTest {

    @Mock
    public TrustlyCallback<Trustly, HashMap<String, String>> mockTrustlyCallback;

    @Mock
    public Trustly mockTrustly;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() {
        clearInvocations(mockTrustlyCallback, mockTrustly);
    }

    @Test
    public void shouldValidateTrustlyCallbackWhenEventAndParametersArePassed() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("param1", "param1");
        parameters.put("param2", "param2");
        new TrustlyCallbackImpl(mockTrustlyCallback).handleParameters(mockTrustly, parameters);
        verify(mockTrustlyCallback).handle(mockTrustly, parameters);
    }

    @Test
    public void shouldValidateTrustlyCallbackWhenEventAndNullParametersArePassed() {
        new TrustlyCallbackImpl(mockTrustlyCallback).handleParameters(mockTrustly, null);
        verify(mockTrustlyCallback).handle(mockTrustly, null);
    }

    private static class TrustlyCallbackImpl {

        private final TrustlyCallback<Trustly, HashMap<String, String>> trustlyCallback;

        TrustlyCallbackImpl(TrustlyCallback<Trustly, HashMap<String, String>> trustlyCallback) {
            this.trustlyCallback = trustlyCallback;
        }

        void handleParameters(Trustly trustly, HashMap<String, String> parameters) {
            trustlyCallback.handle(trustly, parameters);
        }

    }

}