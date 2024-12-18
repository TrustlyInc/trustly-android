package net.trustly.android.sdk.data

import org.junit.Assert.assertNotNull
import org.junit.Test

class RetrofitInstanceTest {

    companion object {

        const val TEST_URL = "https://trustly.one/"

    }

    @Test
    fun testRetrofitInstance() {
        val instance = RetrofitInstance.getInstance(TEST_URL)
        assert(instance.baseUrl().toString() == TEST_URL)
    }

    @Test
    fun testRetrofitInstanceNotNull() {
        val instance = RetrofitInstance.getInstance(TEST_URL)
        assertNotNull(instance)
    }

}