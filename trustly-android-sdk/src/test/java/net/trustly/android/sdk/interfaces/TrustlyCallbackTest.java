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
    public TrustlyCallback<Trustly, HashMap<String, String>> trustlyCallback;

    @Mock
    public Trustly trustly;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() {
        clearInvocations(trustlyCallback, trustly);
    }

    @Test
    public void shouldValidateTrustlyCallbackWhenEventAndParametersArePassed() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("param1", "param1");
        parameters.put("param2", "param2");
        new TrustlyCallbackImpl(trustlyCallback).handleParameters(trustly, parameters);
        verify(trustlyCallback).handle(trustly, parameters);
    }

    @Test
    public void shouldValidateTrustlyCallbackWhenEventAndNullParametersArePassed() {
        new TrustlyCallbackImpl(trustlyCallback).handleParameters(trustly, null);
        verify(trustlyCallback).handle(trustly, null);
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