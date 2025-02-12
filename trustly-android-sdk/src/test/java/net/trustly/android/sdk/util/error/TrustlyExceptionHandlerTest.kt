package net.trustly.android.sdk.util.error

import org.junit.Assert.assertNotNull
import org.junit.Test

class TrustlyExceptionHandlerTest {

    @Test
    fun shouldHandleUncaughtException() {
        val exceptionHandler = TrustlyExceptionHandler()
        val exception = NullPointerException()
        exceptionHandler.uncaughtException(Thread.currentThread(), exception)
        assertNotNull(exceptionHandler)
    }

}