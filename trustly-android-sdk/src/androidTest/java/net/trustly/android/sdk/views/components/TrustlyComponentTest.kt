package net.trustly.android.sdk.views.components

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.data.Settings
import net.trustly.android.sdk.data.StrategySetting
import net.trustly.android.sdk.data.TrustlyUrlFetcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
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
    fun shouldValidateTrustlyComponentGetSettingsData() {
        scenario.onActivity {
            val settingsFake = Settings(StrategySetting("webview"))
            val trustlyUrlFetcher = TrustlyUrlFetcher()
            val trustlyComponent = MockTrustlyComponent()
            trustlyComponent.getSettingsData(trustlyUrlFetcher, URL, TOKEN) {
                assertEquals(settingsFake, it)
            }
        }
    }

    @Test
    fun shouldValidateTrustlyComponentPostLightboxUrl() {
        scenario.onActivity {
            val trustlyUrlFetcher = TrustlyUrlFetcher()
            val trustlyComponent = MockTrustlyComponent()
            trustlyComponent.postLightboxUrl(
                trustlyUrlFetcher,
                URL,
                "user-agent",
                byteArrayOf()
            ) {
                assertEquals(null, it)
            }
        }
    }

    class MockTrustlyComponent : TrustlyComponent() {

        val establishData = mutableMapOf<String, String>()

        override fun updateEstablishData(establishData: Map<String, String>, grp: Int) {
            this.establishData.putAll(establishData)
        }

    }

    companion object {

        const val TOKEN = "RXN0YWJsaXNoRGF0YVN0cmluZw=="
        const val URL = "http://www.url.com"

    }

}