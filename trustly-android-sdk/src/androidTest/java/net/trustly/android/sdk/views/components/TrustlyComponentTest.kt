package net.trustly.android.sdk.views.components

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.data.APIMethod
import net.trustly.android.sdk.data.Settings
import net.trustly.android.sdk.data.StrategySetting
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
@SmallTest
class TrustlyComponentTest : TrustlyActivityTest() {

    @Mock
    private lateinit var mockAPIMethod: APIMethod

    @Mock
    private lateinit var mockCall: Call<Settings>

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.openMocks(this)

        `when`(mockAPIMethod.getSettings(anyString())).thenReturn(mockCall)
    }

    @After
    override fun tearDown() {
        super.tearDown()

        clearInvocations(mockAPIMethod)
    }

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
    fun shouldValidateTrustlyComponentGetSettingsDataSuccess() {
        scenario.onActivity {
            val settingsFake = Settings(StrategySetting("webview"))
            val mockResponse = Response.success(settingsFake)
            mockCallbackResponse(mockResponse)

            val trustlyComponent = MockTrustlyComponent()
            trustlyComponent.getSettingsData(mockAPIMethod, TOKEN) {
                assertEquals(settingsFake, it)
            }
        }
    }

    @Test
    fun shouldValidateTrustlyComponentGetSettingsDataFailedNullBody() {
        scenario.onActivity {
            val settingsFake = Settings(StrategySetting("webview"))
            mockCallbackResponseWithNullBody()

            val trustlyComponent = MockTrustlyComponent()
            trustlyComponent.getSettingsData(mockAPIMethod, TOKEN) {
                assertEquals(settingsFake, it)
            }
        }
    }

    @Test
    fun shouldValidateTrustlyComponentGetSettingsDataFailed() {
        scenario.onActivity {
            val settingsFake = Settings(StrategySetting("webview"))
            val mockResponse = Throwable("Error 401")
            mockCallbackFailure(mockResponse)

            val trustlyComponent = MockTrustlyComponent()
            trustlyComponent.getSettingsData(mockAPIMethod, TOKEN) {
                assertEquals(settingsFake, it)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun mockCallbackResponse(mockResponse: Response<Settings>) {
        `when`(mockCall.enqueue(any())).then {
            val callback = it.arguments.first() as retrofit2.Callback<Settings>
            callback.onResponse(mockCall, mockResponse)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun mockCallbackResponseWithNullBody() {
        `when`(mockCall.enqueue(any())).then {
            val callback = it.arguments.first() as retrofit2.Callback<Settings>
            callback.onResponse(mockCall, Response.success(null))
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun mockCallbackFailure(mockResponse: Throwable) {
        `when`(mockCall.enqueue(any())).then {
            val callback = it.arguments.first() as retrofit2.Callback<Settings>
            callback.onFailure(mockCall, mockResponse)
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

    }

}