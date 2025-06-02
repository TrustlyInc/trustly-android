package net.trustly.android.sdk

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApplicationTest {

    private var appContext: Context? = null

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun tearDown() {
        appContext = null
    }

    @Test
    fun shouldValidateAppContextTest() {
        assertEquals("net.trustly.android.sdk.test", appContext?.packageName)
    }

    @Test
    fun shouldVerifyMinimumSDKVersionIsNineteen() {
        assertEquals(19, appContext?.applicationInfo?.minSdkVersion)
    }

}