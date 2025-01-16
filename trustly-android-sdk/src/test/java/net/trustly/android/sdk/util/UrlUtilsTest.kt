package net.trustly.android.sdk.util

import android.net.Uri
import android.util.Base64
import net.trustly.android.sdk.BuildConfig
import net.trustly.android.sdk.util.UrlUtils.encodeStringToBase64
import net.trustly.android.sdk.util.UrlUtils.getDomain
import net.trustly.android.sdk.util.UrlUtils.getEndpointUrl
import net.trustly.android.sdk.util.UrlUtils.getJsonFromParameters
import net.trustly.android.sdk.util.UrlUtils.getParameterString
import net.trustly.android.sdk.util.UrlUtils.getQueryParameterNames
import net.trustly.android.sdk.util.UrlUtils.getQueryParametersFromUrl
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito.CALLS_REAL_METHODS
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class UrlUtilsTest {

    private val VALUE_1: String = "value1"
    private val KEY_1: String = "key1"
    private val VALUE_2: String = "value2"
    private val KEY_2: String = "key2"
    private val VALUE_3: String = "value3"
    private val KEY_3: String = "key3"
    private val URL_SEARCH_WITH_QUERY: String = "http://www.url.com/search?q=value"
    private val SDK_VERSION = BuildConfig.SDK_VERSION

    @Mock
    private lateinit var mockUri: Uri

    private lateinit var mockedStaticUri: MockedStatic<Uri>

    private lateinit var mockedStaticURLEncoder: MockedStatic<URLEncoder>

    private lateinit var mockedStaticBase64: MockedStatic<Base64>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        mockedStaticUri = mockStatic(Uri::class.java, CALLS_REAL_METHODS)
        mockedStaticUri.`when`<Any?> { Uri.parse(ArgumentMatchers.anyString()) }.thenReturn(mockUri)
        mockedStaticURLEncoder = mockStatic(URLEncoder::class.java, CALLS_REAL_METHODS)
        mockedStaticBase64 = mockStatic(Base64::class.java, CALLS_REAL_METHODS)
    }

    @After
    fun tearDown() {
        mockedStaticUri.close()
        mockedStaticURLEncoder.close()
        mockedStaticBase64.close()
        clearInvocations(mockUri)
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetParameterString() {
        val values = mapOf<String?, String?>(
            KEY_1 to VALUE_1,
            KEY_2 to VALUE_2,
            KEY_3 to VALUE_3
        )
        val parameter = getParameterString(values)
        assertNotEquals("key1=value1,key2=value2,key3=value3", parameter)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetParameterString() {
        val values = mapOf<String?, String?>(
            KEY_1 to VALUE_1,
            KEY_2 to VALUE_2,
            KEY_3 to VALUE_3
        )
        val parameter = getParameterString(values)
        assertEquals("key1=value1&key2=value2&key3=value3", parameter)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetParameterStringWithNullKey() {
        val values = mapOf<String?, String?>(
            null to VALUE_1,
            KEY_2 to VALUE_2,
            KEY_3 to VALUE_3
        )
        val parameter = getParameterString(values)
        assertEquals("=value1&key2=value2&key3=value3", parameter)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetParameterStringWithNullValue() {
        val values = mapOf<String?, String?>(
            KEY_1 to VALUE_1,
            KEY_2 to null,
            KEY_3 to VALUE_3
        )
        val parameter = getParameterString(values)
        assertEquals("key1=value1&key2=&key3=value3", parameter)
    }

    @Test(expected = UnsupportedEncodingException::class)
    fun shouldValidateReturnedValueWhenGetParameterStringWithURLEncodeException() {
        mockedStaticURLEncoder.`when`<Any> { URLEncoder.encode(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()) }.thenThrow(UnsupportedEncodingException(""))

        val values = mapOf<String?, String?>(
            KEY_1 to VALUE_1,
            KEY_2 to VALUE_2,
            KEY_3 to VALUE_3
        )
        getParameterString(values)
    }

    @Test
    fun shouldInvalidadeReturnedValueWhenGetQueryParameterNames() {
        val parse = Uri.parse("http://www.url.com/search?q=random%20word%20%A3500%20bank%20%24")
        val parameterNames = getQueryParameterNames(parse)
        assertNotEquals(null, parameterNames)
    }

    @Test
    fun shouldValidadeReturnedValueWhenGetQueryParameterNames() {
        val encode = URLEncoder.encode("q=example", StandardCharsets.UTF_8)
        val parse = Uri.parse("http://www.google.com:80/help/me/book%20name+me/?$encode")
        val parameterNames = getQueryParameterNames(parse)
        assertEquals(emptyMap<String, String>(), parameterNames)
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetQueryParameterFromUrl() {
        val expected = mapOf(
            "url" to URL_SEARCH_WITH_QUERY,
            "transactionId" to "1234567",
            "transactionType" to "1",
            "merchantReference" to "123456",
            "status" to "2",
            "payment.paymentType" to "2",
            "payment.paymentProvider.type" to "1",
            "payment.account.verified" to "true",
            "panel" to "1"
        )
        `when`(mockUri.encodedQuery).thenReturn("transactionId=1234567&transactionType=1&merchantReference=123456&status=2&payment.paymentType=2&payment.paymentProvider.type=1&payment.account.verified=true&panel=1")

        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertEquals(expected, parameters)
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetQueryParameterWithAmpersandFromUrl() {
        val expected = mapOf(
            "url" to URL_SEARCH_WITH_QUERY,
            "q" to "value"
        )
        `when`(mockUri.encodedQuery).thenReturn("q=value&")

        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertEquals(expected, parameters)
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetQueryParameterWithNoValueFromUrl() {
        val expected = mapOf(
            "url" to URL_SEARCH_WITH_QUERY,
            "q" to ""
        )
        `when`(mockUri.encodedQuery).thenReturn("q=")

        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertEquals(expected, parameters)
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetQueryParameterWithJustQueryNameFromUrl() {
        val expected = mapOf(
            "url" to URL_SEARCH_WITH_QUERY
        )
        `when`(mockUri.encodedQuery).thenReturn("q")

        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertEquals(expected, parameters)
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetQueryParameterWithNoValueAndOneSeparatorFromUrl() {
        val expected = mapOf(
            "url" to URL_SEARCH_WITH_QUERY,
            "a" to ""
        )
        `when`(mockUri.encodedQuery).thenReturn("q&a=")

        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertEquals(expected, parameters)
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetQueryParameterWithNoValueForQueriesFromUrl() {
        val expected = mapOf(
            "url" to URL_SEARCH_WITH_QUERY
        )
        `when`(mockUri.encodedQuery).thenReturn("q&a")

        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertEquals(expected, parameters)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetQueryParameterFromUrl() {
        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertEquals(URL_SEARCH_WITH_QUERY, parameters["url"])
    }

    @Test
    fun shouldReplaceParameterRequestSignatureValueWhenGetQueryParameterFromUrl() {
        val parameters = getQueryParametersFromUrl("http://www.url.com/search?requestSignature=1234")
        assertEquals("http://www.url.com/search?", parameters["url"])
    }

    @Test
    fun shouldValidateReturnedValueWhenGetQueryParameterFromUrlWithManyParameters() {
        val parameters = getQueryParametersFromUrl("http://www.url.com/search?q=value&a=value2&b=value3")
        assertEquals("http://www.url.com/search?q=value&a=value2&b=value3", parameters["url"])
    }

    @Test
    fun shouldValidateReturnedValueWhenGetJsonFromParametersWithEstablishData() {
        val values = mapOf(
            KEY_1 to VALUE_1,
            KEY_2 to VALUE_2,
            KEY_3 to VALUE_3
        )
        val result = getJsonFromParameters(values)
        assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetJsonFromParametersWithEstablishDataWithDotKeys() {
        val values = mapOf(
            KEY_1 to VALUE_1,
            KEY_2 to VALUE_2,
            "key3.subKey3" to VALUE_3
        )
        val result = getJsonFromParameters(values)
        assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":{\"subKey3\":\"value3\"}}", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetJsonFromParametersWithEstablishDataWithExistingKeys() {
        val values = mapOf(
            KEY_1 to VALUE_1,
            KEY_2 to VALUE_2,
            "key3.subKey1" to VALUE_3,
            "key3.subKey2" to VALUE_3
        )
        val result = getJsonFromParameters(values)
        assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":{\"subKey1\":\"value3\",\"subKey2\":\"value3\"}}", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetJsonFromParametersWithEstablishDataWithNullKeys() {
        mockedStaticBase64.`when`<Any> { Base64.encodeToString(ArgumentMatchers.any(), ArgumentMatchers.anyInt()) }.thenReturn("eyJrZXkxIjoidmFsdWUxImtleTMiOiJ2YWx1ZTMifQ==")

        val jsonParameters = "{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}"
        val result = encodeStringToBase64(jsonParameters)
        assertEquals("eyJrZXkxIjoidmFsdWUxImtleTMiOiJ2YWx1ZTMifQ==", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetEndpointUrlWithEstablishDataWithSdkVersionValueAndNoValidFunctionDefaultDomain() {
        val values = mapOf(
            "metadata.sdkAndroidVersion" to SDK_VERSION,
        )

        val result = getEndpointUrl("widget", values)
        assertEquals("https://paywithmybank.com/start/selectBank/widget?v=${SDK_VERSION}-android-sdk", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetEndpointUrlWithEstablishDataWithSdkVersionValueAndMobileFunctionDefaultDomain() {
        val result = getEndpointUrl("mobile", mapOf())
        assertEquals("https://paywithmybank.com/frontend/mobile/establish", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetEndpointUrlWithEstablishDataWithSdkVersionValueAndIndexFunction() {
        val values = mapOf(
            "metadata.sdkAndroidVersion" to SDK_VERSION,
        )

        val result = getEndpointUrl("index", values)
        assertEquals("https://paywithmybank.com/start/selectBank/index?v=${SDK_VERSION}-android-sdk", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetEndpointUrlWithEstablishDataWithSdkVersionValueAndIndexFunctionWithPaymentType() {
        val values = mapOf(
            "metadata.sdkAndroidVersion" to SDK_VERSION,
            "paymentType" to "Deferred",
        )

        val result = getEndpointUrl("index", values)
        assertEquals("https://paywithmybank.com/start/selectBank/index?v=${SDK_VERSION}-android-sdk", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetEndpointUrlWithEstablishDataWithSdkVersionValueAndIndexFunctionWithPaymentTypeVerification() {
        val values = mapOf(
            "metadata.sdkAndroidVersion" to SDK_VERSION,
            "paymentType" to "Verification",
        )

        val result = getEndpointUrl("index", values)
        assertEquals("https://paywithmybank.com/start/selectBank/index?v=${SDK_VERSION}-android-sdk", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetEndpointUrlWithEstablishDataWithSdkVersionValueAndIndexFunctionWithPaymentId() {
        val values = mapOf(
            "metadata.sdkAndroidVersion" to SDK_VERSION,
            "paymentProviderId" to "123456789",
        )

        val result = getEndpointUrl("index", values)
        assertEquals("https://paywithmybank.com/start/selectBank/selectBank?v=${SDK_VERSION}-android-sdk", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetEndpointUrlWithEstablishDataWithSdkVersionValueAndIndexFunctionWithPaymentTypeAndId() {
        val values = mapOf(
            "metadata.sdkAndroidVersion" to SDK_VERSION,
            "paymentType" to "Deferred",
            "paymentProviderId" to "123456789",
        )

        val result = getEndpointUrl("index", values)
        assertEquals("https://paywithmybank.com/start/selectBank/selectBank?v=${SDK_VERSION}-android-sdk", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetDomainWithEmptyEstablishDataMobileFunction() {
        val result = getDomain("mobile", mapOf())
        assertEquals("https://paywithmybank.com", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetDomainWithEstablishDataMobileFunctionEnvDynamic() {
        val values = mapOf(
            "env" to "dynamic"
        )
        val result = getDomain("mobile", values)
        assertEquals("https://paywithmybank.int.trustly.one", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetDomainWithEstablishDataMobileFunctionWithEnvDynamic() {
        val values = mapOf(
            "env" to "dynamic",
            "envHost" to "paywithmybank"
        )
        val result = getDomain("mobile", values)
        assertEquals("https://paywithmybank.int.trustly.one", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetDomainWithEstablishDataMobileFunctionWithEnvProd() {
        val values = mapOf(
            "env" to "prod",
            "envHost" to "paywithmybank"
        )
        val result = getDomain("mobile", values)
        assertEquals("https://paywithmybank.com", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetDomainWithEstablishDataMobileFunctionWithEnvProduction() {
        val values = mapOf(
            "env" to "production",
            "envHost" to "paywithmybank"
        )
        val result = getDomain("mobile", values)
        assertEquals("https://paywithmybank.com", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetDomainWithEstablishDataMobileFunctionWithEnvUAT() {
        val values = mapOf(
            "env" to "uat",
            "envHost" to "paywithmybank"
        )
        val result = getDomain("mobile", values)
        assertEquals("https://uat.paywithmybank.com", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetDomainWithEstablishDataMobileFunctionWithEnvLocalWithEnvHost() {
        val values = mapOf(
            "env" to "local",
            "envHost" to "192.168.0.1"
        )
        val result = getDomain("mobile", values)
        assertEquals("http://192.168.0.1:10000", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetDomainWithEstablishDataMobileFunctionWithEnvLocalWithEnvHostLocalhost() {
        val values = mapOf(
            "env" to "local",
            "envHost" to "localhost"
        )
        val result = getDomain("mobile", values)
        assertEquals("http://10.0.2.2:10000", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetDomainWithEstablishDataMobileFunctionWithEnvLocalWithLocalhost() {
        val values = mapOf(
            "env" to "local",
            "localhost" to "192.168.0.1"
        )
        val result = getDomain("mobile", values)
        assertEquals("http://10.0.2.2:10000", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetDomainWithEstablishDataMobileFunctionWithEnvLocalLocalhost() {
        val values = mapOf(
            "env" to "local",
            "localhost" to "localhost"
        )
        val result = getDomain("mobile", values)
        assertEquals("http://10.0.2.2:10000", result)
    }

    @Test
    fun shouldValidateReturnedValueWhenGetDomainWithEstablishDataMobileIndexWithEnvLocalLocalhost() {
        val values = mapOf(
            "env" to "local",
            "localhost" to "localhost"
        )
        val result = getDomain("index", values)
        assertEquals("http://10.0.2.2:8000", result)
    }

}