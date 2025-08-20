package net.trustly.android.sdk.util.error

import org.junit.Assert.assertNotNull
import org.junit.Test

class TrustlyExceptionHandlerTest {

    @Test
    fun shouldValidateTrustlyExceptionHandleUncaughtException() {
        val exceptionHandler = TrustlyExceptionHandler()
        exceptionHandler.uncaughtException(Thread.currentThread(), Exception())
        assertNotNull(exceptionHandler)
    }

    @Test
    fun shouldValidateTrustlyExceptionHandleUncaughtExceptionSDKPackage() {
        val exception = Exception("Something failed in net.trustly.android.sdk")
        val exceptionHandler = TrustlyExceptionHandler()
        exceptionHandler.uncaughtException(Thread.currentThread(), exception)
        assertNotNull(exceptionHandler)
    }

    @Test
    fun shouldValidateTrustlyExceptionHandleUncaughtExceptionAnotherPackage() {
        val exception = Exception("Something failed in com.trustly.android.sdk")
        val exceptionHandler = TrustlyExceptionHandler()
        exceptionHandler.uncaughtException(Thread.currentThread(), exception)
        assertNotNull(exceptionHandler)
    }

}