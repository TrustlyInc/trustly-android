package net.trustly.android.sdk.interfaces

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class TrustlyListenerTest {

    @Mock
    private lateinit var mockTrustlyListener: TrustlyListener

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun tearDown() {
        clearInvocations(mockTrustlyListener)
    }

    @Test
    fun shouldValidateTrustlyListenerWhenEventAndDetailsArePassed() {
        val eventNames = hashMapOf(
            "key1" to "value1",
            "key2" to "value2"
        )
        TrustlyListenerImpl(mockTrustlyListener).sendEventChange(EVENT_WITH_DETAILS, eventNames)
        verify(mockTrustlyListener).onChange(EVENT_WITH_DETAILS, eventNames)
    }

    private class TrustlyListenerImpl(private val trustlyListener: TrustlyListener) {

        fun sendEventChange(event: String, eventDetails: HashMap<String, String>) {
            trustlyListener.onChange(event, eventDetails)
        }

    }

    companion object {
        private const val EVENT_WITH_DETAILS = "eventWithDetails"
    }

}