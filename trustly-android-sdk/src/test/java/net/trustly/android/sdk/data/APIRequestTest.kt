package net.trustly.android.sdk.data

import com.google.gson.Gson
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response

class APIRequestTest {

    @Mock
    private lateinit var api: APIMethod

    @Mock
    private lateinit var mockCall: Call<Settings>

    private var settingsResult: Settings? = null

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        `when`(api.getSettings(anyString())).thenReturn(mockCall)
    }

    @After
    fun tearDown() {
        api.getSettings(TOKEN).cancel()
        mockCall.cancel()
        settingsResult = null
    }

    @Test
    fun testGetSettingDataWhenReturnSuccessSameObject() {
        val settingsFake = Settings(StrategySetting("webview"))
        val mockResponse = Response.success(settingsFake)
        mockCallbackResponse(mockResponse)

        APIRequest(api, { settingsResult = it }, { settingsResult = null }).getSettingsData(TOKEN)

        assertEquals(settingsFake, settingsResult)
    }

    @Test
    fun testGetSettingDataWhenReturnSuccess() {
        val settingsFake = getSettingsFake()
        val mockResponse = Response.success(settingsFake)
        mockCallbackResponse(mockResponse)

        APIRequest(api, { settingsResult = it }, { settingsResult = null }).getSettingsData(TOKEN)

        assertEquals(settingsFake, settingsResult)
    }

    @Test
    fun testGetSettingDataValuesWhenReturnSuccess() {
        val settingsFake = getSettingsFake()
        val mockResponse = Response.success(settingsFake)
        mockCallbackResponse(mockResponse)

        APIRequest(api, { settingsResult = it }, { settingsResult = null }).getSettingsData(TOKEN)

        assertEquals(settingsFake, settingsResult)
        assertEquals("webview", settingsResult!!.settings.integrationStrategy)
    }

    @Test
    fun testGetSettingDataWrongValuesWhenReturnSuccess() {
        val settingsFake = getSettingsFake()
        val mockResponse = Response.success(settingsFake)
        mockCallbackResponse(mockResponse)

        APIRequest(api, { settingsResult = it }, { settingsResult = null }).getSettingsData(TOKEN)

        assertEquals(settingsFake, settingsResult)
        assertNotEquals("inappbrowser", settingsResult!!.settings.integrationStrategy)
    }

    @Test
    fun testGetSettingDataValuesWhenReturnSuccessWithNullBody() {
        mockCallbackResponseWithNullBody()

        APIRequest(api, { settingsResult = it }, { settingsResult = null }).getSettingsData(TOKEN)

        assertEquals(null, settingsResult)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testGetSettingDataWrongValuesWhenReturnSuccessNotSuccessfulAndWithBody() {
        val settingsFake = getSettingsFake()
        Response.success(301, settingsFake)
    }

    @Test
    fun testGetSettingDataWhenReturnFailure() {
        val mockResponse = Throwable("Error 401")
        mockCallbackFailure(mockResponse)

        APIRequest(api, { settingsResult = it }, { settingsResult = null }).getSettingsData(TOKEN)

        assertEquals(null, settingsResult)
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

    private fun getSettingsFake(): Settings {
        return Gson().fromJson(getSettingsJson(), Settings::class.java)
    }

    private fun getSettingsJson() = "{'settings': {'integrationStrategy': 'webview'}}"

    companion object {

        const val TOKEN = "RXN0YWJsaXNoRGF0YVN0cmluZw=="

    }

}