package net.trustly.android.sdk.interfaces;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import net.trustly.android.sdk.views.TrustlyView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class TrustlyTest {

    @Mock
    private Trustly trustly;

    @Mock
    private TrustlyCallback<Trustly, Map<String, String>> callback;

    @Mock
    private TrustlyListener listener;

    @Mock
    private Context context;

    private TrustlyImpl trustlyImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        trustlyImpl = new TrustlyImpl(trustly);
    }

    @After
    public void tearDown() {
        clearInvocations(trustly, callback, listener, context);
    }

    @Test
    public void shouldValidateTrustlyInstance() {
        Trustly.Instance instance = new Trustly.Instance();
        assertNotNull(instance);
    }

    @Test
    public void shouldValidateTrustlySelectBankWidgetWhenIsCalled() {
        when(trustly.selectBankWidget(anyMap())).thenReturn(trustly);
        HashMap<String, String> establishData = getEstablishData();
        Trustly result = trustlyImpl.callSelectBankWidget(establishData);
        verify(trustly, times(1)).selectBankWidget(establishData);
        assertSame(trustly, result);
    }

    @Test
    public void shouldValidateTrustlySelectBankNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callSelectBankWidget(null);
        verify(trustly, times(1)).selectBankWidget(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyOnBankSelectedWhenIsCalled() {
        when(trustly.onBankSelected(any())).thenReturn(trustly);
        Trustly result = trustlyImpl.callOnBankSelected(callback);
        verify(trustly, times(1)).onBankSelected(callback);
        assertSame(trustly, result);
    }

    @Test
    public void shouldValidateTrustlyOnBankSelectedNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callOnBankSelected(null);
        verify(trustly, times(1)).onBankSelected(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyEstablishWhenIsCalled() {
        when(trustly.establish(anyMap())).thenReturn(trustly);
        HashMap<String, String> establishData = getEstablishData();
        Trustly result = trustlyImpl.callEstablish(establishData);
        verify(trustly, times(1)).establish(establishData);
        assertSame(trustly, result);
    }

    @Test
    public void shouldValidateTrustlyEstablishNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callEstablish(null);
        verify(trustly, times(1)).establish(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyHybridWhenIsCalled() {
        when(trustly.hybrid(any(), any(), any())).thenReturn(trustly);
        Trustly result = trustlyImpl.callHybrid("http://www.url.com", "http://www.url.com/return",
                "http://www.url.com/cancel");
        verify(trustly, times(1)).hybrid("http://www.url.com",
                "http://www.url.com/return", "http://www.url.com/cancel");
        assertSame(trustly, result);
    }

    @Test
    public void shouldValidateTrustlyHybridNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callHybrid(null, null, null);
        verify(trustly, times(1)).hybrid(null, null, null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyOnReturnWhenIsCalled() {
        when(trustly.onReturn(any())).thenReturn(trustly);
        Trustly result = trustlyImpl.callOnReturn(callback);
        verify(trustly, times(1)).onReturn(callback);
        assertSame(trustly, result);
    }

    @Test
    public void shouldValidateTrustlyOnReturnNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callOnReturn(null);
        verify(trustly, times(1)).onReturn(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyOnCancelWhenIsCalled() {
        when(trustly.onCancel(any())).thenReturn(trustly);
        Trustly result = trustlyImpl.callOnCancel(callback);
        verify(trustly, times(1)).onCancel(callback);
        assertSame(trustly, result);
    }

    @Test
    public void shouldValidateTrustlyOnCancelNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callOnCancel(null);
        verify(trustly, times(1)).onCancel(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyOnExternalUrlWhenIsCalled() {
        when(trustly.onExternalUrl(any())).thenReturn(trustly);
        Trustly result = trustlyImpl.callOnExternalUrl(callback);
        verify(trustly, times(1)).onExternalUrl(callback);
        assertSame(trustly, result);
    }

    @Test
    public void shouldValidateTrustlyOnExternalUrlNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callOnExternalUrl(null);
        verify(trustly, times(1)).onExternalUrl(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlySetListenerWhenIsCalled() {
        when(trustly.setListener(any())).thenReturn(trustly);
        Trustly result = trustlyImpl.callSetListener(listener);
        verify(trustly, times(1)).setListener(listener);
        assertSame(trustly, result);
    }

    @Test
    public void shouldValidateTrustlySetListenerNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callSetListener(null);
        verify(trustly, times(1)).setListener(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyDestroyWhenIsCalled() {
        when(trustly.destroy()).thenReturn(trustly);
        Trustly result = trustlyImpl.callDestroy();
        verify(trustly, times(1)).destroy();
        assertSame(trustly, result);
    }

    @Test
    public void shouldValidateTrustlyDestroyNullValueWhenIsCalled() {
        when(trustly.destroy()).thenReturn(null);
        Trustly result = trustlyImpl.callDestroy();
        verify(trustly, times(1)).destroy();
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyProceedToChooseAccountWhenIsCalled() {
        doNothing().when(trustly).proceedToChooseAccount();
        trustlyImpl.callProceedToChooseAccount();
        verify(trustly, times(1)).proceedToChooseAccount();
    }

    private HashMap<String, String> getEstablishData() {
        HashMap<String, String> establishData = new HashMap<>();
        establishData.put("accessId", "123456");
        establishData.put("merchantId", "654321");
        return establishData;
    }

    private static class TrustlyImpl {

        private final Trustly trustly;

        TrustlyImpl(Trustly trustly) {
            this.trustly = trustly;
        }

        public Trustly callSelectBankWidget(Map<String, String> establishData) {
            return trustly.selectBankWidget(establishData);
        }

        public Trustly callOnBankSelected(TrustlyCallback<Trustly, Map<String, String>> callback) {
            return trustly.onBankSelected(callback);
        }

        public Trustly callEstablish(Map<String, String> establishData) {
            return trustly.establish(establishData);
        }

        public Trustly callHybrid(String url, String returnUrl, String cancelUrl) {
            return trustly.hybrid(url, returnUrl, cancelUrl);
        }

        public Trustly callOnReturn(TrustlyCallback<Trustly, Map<String, String>> callback) {
            return trustly.onReturn(callback);
        }

        public Trustly callOnCancel(TrustlyCallback<Trustly, Map<String, String>> callback) {
            return trustly.onCancel(callback);
        }

        public Trustly callOnExternalUrl(TrustlyCallback<Trustly, Map<String, String>> callback) {
            return trustly.onExternalUrl(callback);
        }

        public Trustly callSetListener(TrustlyListener listener) {
            return trustly.setListener(listener);
        }

        public Trustly callDestroy() {
            return trustly.destroy();
        }

        private void callProceedToChooseAccount() {
            trustly.proceedToChooseAccount();
        }

    }

}