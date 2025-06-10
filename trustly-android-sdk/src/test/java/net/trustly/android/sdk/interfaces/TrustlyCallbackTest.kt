package net.trustly.android.sdk.interfaces

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class TrustlyCallbackTest {

    @Mock
    private lateinit var mockTrustlyCallback: TrustlyCallback<Trustly?, HashMap<String, String>?>

    @Mock
    private lateinit var mockTrustly: Trustly

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun tearDown() {
        clearInvocations(mockTrustlyCallback, mockTrustly)
    }

    @Test
    fun shouldValidateTrustlyCallbackWhenEventAndParametersArePassed() {
        val parameters = hashMapOf(
            "key1" to "value1",
            "key2" to "value2"
        )

        TrustlyCallbackImpl(mockTrustlyCallback).handleParameters(mockTrustly, parameters)
        verify(mockTrustlyCallback).handle(mockTrustly, parameters)
    }

    @Test
    fun shouldValidateTrustlyCallbackWhenEventAndNullParametersArePassed() {
        TrustlyCallbackImpl(mockTrustlyCallback).handleParameters(mockTrustly, null)
        verify(mockTrustlyCallback).handle(mockTrustly, null)
    }

    private class TrustlyCallbackImpl(private val trustlyCallback: TrustlyCallback<Trustly?, HashMap<String, String>?>) {

        fun handleParameters(trustly: Trustly, parameters: HashMap<String, String>?) {
            trustlyCallback.handle(trustly, parameters)
        }

    }

}