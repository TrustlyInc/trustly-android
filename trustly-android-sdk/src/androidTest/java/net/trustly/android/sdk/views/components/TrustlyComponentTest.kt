package net.trustly.android.sdk.views.components

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import net.trustly.android.sdk.TrustlyActivityTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class TrustlyComponentTest : TrustlyActivityTest() {

    @Test
    fun shouldValidateTrustlyComponentInstance() {
        scenario.onActivity {
            val trustlyComponent = MockTrustlyComponent()
            assertNotNull(trustlyComponent)
        }
    }

    @Test
    fun shouldValidateTrustlyComponentUpdateEstablishDataValues() {
        scenario.onActivity {
            val values = mapOf(
                "key1" to "value1",
                "key2" to "value2"
            )

            val trustlyComponent = MockTrustlyComponent()
            trustlyComponent.updateEstablishData(values, 0)

            assertEquals(values, trustlyComponent.establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyComponentShowErrorMessage() {
        scenario.onActivity {
            val values = mapOf(
                "key1" to "value1",
                "key2" to "value2"
            )

            val trustlyComponent = MockTrustlyComponent()
            trustlyComponent.updateEstablishData(values, 0)
            trustlyComponent.showErrorMessage("Message error")

            assertEquals(values, trustlyComponent.establishData)
        }
    }

    class MockTrustlyComponent : TrustlyComponent() {

        private val TAG = "MockTrustlyComponent"
        val establishData = mutableMapOf<String, String>()

        override fun updateEstablishData(establishData: Map<String, String>, grp: Int) {
            this.establishData.putAll(establishData)
        }

        fun showErrorMessage(errorMsg: String) {
            super.showErrorMessage(TAG, Exception(errorMsg))
        }

    }

}