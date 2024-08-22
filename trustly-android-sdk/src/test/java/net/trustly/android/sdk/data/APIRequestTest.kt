package net.trustly.android.sdk.data

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.ResponseBody
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
        api.getSettings("RXN0YWJsaXNoRGF0YVN0cmluZw==").cancel()
        mockCall.cancel()
        settingsResult = null
    }

    @Test
    fun testGetSettingDataWhenReturnSuccess() {
        val settingsFake = getSettingsFake()
        val mockResponse = Response.success(settingsFake)
        mockCallbackResponse(mockResponse)

        APIRequest(api) { settingsResult = it }.getSettingsData("RXN0YWJsaXNoRGF0YVN0cmluZw==")

        assertEquals(settingsFake, settingsResult)
    }

    @Test
    fun testGetSettingDataValuesWhenReturnSuccess() {
        val settingsFake = getSettingsFake()
        val mockResponse = Response.success(settingsFake)
        mockCallbackResponse(mockResponse)

        APIRequest(api) { settingsResult = it }.getSettingsData("RXN0YWJsaXNoRGF0YVN0cmluZw==")

        assertEquals(settingsFake, settingsResult)
        assertEquals("in-app-browser", settingsResult!!.settings.lightbox.context)
    }

    @Test
    fun testGetSettingDataWrongValuesWhenReturnSuccess() {
        val settingsFake = getSettingsFake()
        val mockResponse = Response.success(settingsFake)
        mockCallbackResponse(mockResponse)

        APIRequest(api) { settingsResult = it }.getSettingsData("RXN0YWJsaXNoRGF0YVN0cmluZw==")

        assertEquals(settingsFake, settingsResult)
        assertNotEquals("web-view", settingsResult!!.settings.lightbox.context)
    }

    @Test
    fun testGetSettingDataWhenReturnFailure() {
        val mockResponse = Response.error<Settings>(
            401,
            ResponseBody.create(MediaType.parse("application/json"), "")
        )
        mockCallbackResponse(mockResponse)

        APIRequest(api) { settingsResult = it }.getSettingsData("RXN0YWJsaXNoRGF0YVN0cmluZw==")

        assertEquals(null, settingsResult)
    }

    @Test
    fun testGetSettingDataWhenReturnNetworkExceptionError() {
        val mockResponse = Response.error<Settings>(
            500,
            ResponseBody.create(MediaType.parse("plain/text"), "API Not found")
        )
        mockCallbackResponse(mockResponse)

        APIRequest(api) { settingsResult = it }.getSettingsData("RXN0YWJsaXNoRGF0YVN0cmluZw==")

        assertEquals(null, settingsResult)
    }

    private fun mockCallbackResponse(mockResponse: Response<Settings>) {
        `when`(mockCall.enqueue(any())).then {
            val callback = it.arguments.first() as retrofit2.Callback<Settings>
            callback.onResponse(mockCall, mockResponse)
        }
    }

    private fun getSettingsFake(): Settings {
        return Gson().fromJson(getSettingsJson(), Settings::class.java)
    }

    private fun getSettingsJson() = "{'settings': {'lightbox': {'context': 'in-app-browser'}}}"

}