package net.trustly.android.sdk.interfaces;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class TrustlyTest {

    private static final String URL = "http://www.url.com";
    private static final String RETURN_URL = "http://www.url.com/return";
    private static final String CANCEL_URL = "http://www.url.com/cancel";

    @Mock
    private Trustly mockTrustly;

    @Mock
    private TrustlyCallback<Trustly, Map<String, String>> mockTrustlyCallback;

    @Mock
    private TrustlyListener mockTrustlyListener;

    @Mock
    private Context mockContext;

    private TrustlyImpl trustlyImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        trustlyImpl = new TrustlyImpl(mockTrustly);
    }

    @After
    public void tearDown() {
        clearInvocations(mockTrustly, mockTrustlyCallback, mockTrustlyListener, mockContext);
    }

    @Deprecated
    @Test
    public void shouldThrowExceptionWhenTrustlyInstanceIsCalled() {
        assertThrows(NullPointerException.class, () -> Trustly.Instance.create(mockContext));
    }

    @Test
    public void shouldValidateTrustlySelectBankWidgetWhenIsCalled() {
        when(mockTrustly.selectBankWidget(anyMap())).thenReturn(mockTrustly);
        HashMap<String, String> establishData = getEstablishData();
        Trustly result = trustlyImpl.callSelectBankWidget(establishData);
        verify(mockTrustly, times(1)).selectBankWidget(establishData);
        assertSame(mockTrustly, result);
    }

    @Test
    public void shouldValidateTrustlySelectBankNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callSelectBankWidget(null);
        verify(mockTrustly, times(1)).selectBankWidget(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyOnBankSelectedWhenIsCalled() {
        when(mockTrustly.onBankSelected(any())).thenReturn(mockTrustly);
        Trustly result = trustlyImpl.callOnBankSelected(mockTrustlyCallback);
        verify(mockTrustly, times(1)).onBankSelected(mockTrustlyCallback);
        assertSame(mockTrustly, result);
    }

    @Test
    public void shouldValidateTrustlyOnBankSelectedNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callOnBankSelected(null);
        verify(mockTrustly, times(1)).onBankSelected(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyEstablishWhenIsCalled() {
        when(mockTrustly.establish(anyMap())).thenReturn(mockTrustly);
        HashMap<String, String> establishData = getEstablishData();
        Trustly result = trustlyImpl.callEstablish(establishData);
        verify(mockTrustly, times(1)).establish(establishData);
        assertSame(mockTrustly, result);
    }

    @Test
    public void shouldValidateTrustlyEstablishNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callEstablish(null);
        verify(mockTrustly, times(1)).establish(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyHybridWhenIsCalled() {
        when(mockTrustly.hybrid(any(), any(), any())).thenReturn(mockTrustly);
        Trustly result = trustlyImpl.callHybrid(URL, RETURN_URL,
                CANCEL_URL);
        verify(mockTrustly, times(1)).hybrid(URL, RETURN_URL, CANCEL_URL);
        assertSame(mockTrustly, result);
    }

    @Test
    public void shouldValidateTrustlyHybridWithoutReturnUrlWhenIsCalled() {
        when(mockTrustly.hybrid(any(), any(), any())).thenReturn(mockTrustly);
        Trustly result = trustlyImpl.callHybrid(URL, null,
                CANCEL_URL);
        verify(mockTrustly, times(1)).hybrid(URL, null, CANCEL_URL);
        assertSame(mockTrustly, result);
    }

    @Test
    public void shouldValidateTrustlyHybridWithoutCancelUrlWhenIsCalled() {
        when(mockTrustly.hybrid(any(), any(), any())).thenReturn(mockTrustly);
        Trustly result = trustlyImpl.callHybrid(URL, RETURN_URL,
                null);
        verify(mockTrustly, times(1)).hybrid(URL, RETURN_URL, null);
        assertSame(mockTrustly, result);
    }

    @Test
    public void shouldValidateTrustlyHybridNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callHybrid(null, null, null);
        verify(mockTrustly, times(1)).hybrid(null, null, null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyOnReturnWhenIsCalled() {
        when(mockTrustly.onReturn(any())).thenReturn(mockTrustly);
        Trustly result = trustlyImpl.callOnReturn(mockTrustlyCallback);
        verify(mockTrustly, times(1)).onReturn(mockTrustlyCallback);
        assertSame(mockTrustly, result);
    }

    @Test
    public void shouldValidateTrustlyOnReturnNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callOnReturn(null);
        verify(mockTrustly, times(1)).onReturn(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyOnCancelWhenIsCalled() {
        when(mockTrustly.onCancel(any())).thenReturn(mockTrustly);
        Trustly result = trustlyImpl.callOnCancel(mockTrustlyCallback);
        verify(mockTrustly, times(1)).onCancel(mockTrustlyCallback);
        assertSame(mockTrustly, result);
    }

    @Test
    public void shouldValidateTrustlyOnCancelNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callOnCancel(null);
        verify(mockTrustly, times(1)).onCancel(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyOnExternalUrlWhenIsCalled() {
        when(mockTrustly.onExternalUrl(any())).thenReturn(mockTrustly);
        Trustly result = trustlyImpl.callOnExternalUrl(mockTrustlyCallback);
        verify(mockTrustly, times(1)).onExternalUrl(mockTrustlyCallback);
        assertSame(mockTrustly, result);
    }

    @Test
    public void shouldValidateTrustlyOnExternalUrlNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callOnExternalUrl(null);
        verify(mockTrustly, times(1)).onExternalUrl(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlySetListenerWhenIsCalled() {
        when(mockTrustly.setListener(any())).thenReturn(mockTrustly);
        Trustly result = trustlyImpl.callSetListener(mockTrustlyListener);
        verify(mockTrustly, times(1)).setListener(mockTrustlyListener);
        assertSame(mockTrustly, result);
    }

    @Test
    public void shouldValidateTrustlySetListenerNullValuesWidgetWhenIsCalled() {
        Trustly result = trustlyImpl.callSetListener(null);
        verify(mockTrustly, times(1)).setListener(null);
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyDestroyWhenIsCalled() {
        when(mockTrustly.destroy()).thenReturn(mockTrustly);
        Trustly result = trustlyImpl.callDestroy();
        verify(mockTrustly, times(1)).destroy();
        assertSame(mockTrustly, result);
    }

    @Test
    public void shouldValidateTrustlyDestroyNullValueWhenIsCalled() {
        when(mockTrustly.destroy()).thenReturn(null);
        Trustly result = trustlyImpl.callDestroy();
        verify(mockTrustly, times(1)).destroy();
        assertSame(null, result);
    }

    @Test
    public void shouldValidateTrustlyProceedToChooseAccountWhenIsCalled() {
        doNothing().when(mockTrustly).proceedToChooseAccount();
        trustlyImpl.callProceedToChooseAccount();
        verify(mockTrustly, times(1)).proceedToChooseAccount();
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