package net.trustly.android.sdk.data

import org.junit.Assert.assertNotNull
import org.junit.Test

class RetrofitInstanceTest {

    @Test
    fun testRetrofitInstance() {
        val instance = RetrofitInstance.getInstance("https://trustly.one/")
        assert(instance.baseUrl().toString() == "https://trustly.one/")
    }

    @Test
    fun testRetrofitInstanceNotNull() {
        val instance = RetrofitInstance.getInstance("https://trustly.one/")
        assertNotNull(instance)
    }

}