package net.trustly.android.sdk.util

import android.net.Uri
import android.util.Base64
import net.trustly.android.sdk.util.UrlUtils.encodeStringToBase64
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
    private val URL_SEARCH: String = "http://www.url.com/search"
    private val URL_SEARCH_WITH_QUERY: String = "http://www.url.com/search?q=value"

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
        assertEquals(emptySet<Any>(), parameterNames)
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetQueryParameterFromUrl() {
        `when`(mockUri.encodedQuery).thenReturn("q=value")

        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertNotEquals(URL_SEARCH, parameters["url"])
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetQueryParameterWithAmpersandFromUrl() {
        `when`(mockUri.encodedQuery).thenReturn("q=value&")

        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertNotEquals(URL_SEARCH, parameters["url"])
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetQueryParameterWithNoValueFromUrl() {
        `when`(mockUri.encodedQuery).thenReturn("q=")

        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertNotEquals(URL_SEARCH, parameters["url"])
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetQueryParameterWithJustQueryNameFromUrl() {
        `when`(mockUri.encodedQuery).thenReturn("q")

        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertNotEquals(URL_SEARCH, parameters["url"])
    }

    @Test
    fun shouldInvalidateReturnedValueWhenGetQueryParameterWithNoValueAndTwoSeparatorFromUrl() {
        `when`(mockUri.encodedQuery).thenReturn("q&a=")

        val parameters = getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY)
        assertNotEquals(URL_SEARCH, parameters["url"])
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

}