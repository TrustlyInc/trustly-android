package net.trustly.android.sdk.data

import org.junit.Assert.assertNotNull
import org.junit.Test

class RetrofitInstanceTest {

    @Test
    fun testRetrofitInstance() {
        val instance = RetrofitInstance.getInstance()
        assert(instance.baseUrl().toString() == "https://dogapi.dog/")
    }

    @Test
    fun testRetrofitInstanceNotNull() {
        val instance = RetrofitInstance.getInstance()
        assertNotNull(instance)
    }

}