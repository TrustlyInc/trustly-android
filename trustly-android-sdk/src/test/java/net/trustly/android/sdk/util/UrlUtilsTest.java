package net.trustly.android.sdk.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import android.net.Uri;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UrlUtilsTest {

    private static final String VALUE_1 = "value1";
    private static final String KEY_1 = "key1";
    private static final String VALUE_2 = "value2";
    private static final String KEY_2 = "key2";
    private static final String VALUE_3 = "value3";
    private static final String KEY_3 = "key3";
    private static final String URL_SEARCH = "http://www.url.com/search";
    private static final String URL_SEARCH_WITH_QUERY = "http://www.url.com/search?q=value";

    @Mock
    private Uri mockUri;

    private MockedStatic<Uri> mockedStaticUri;

    private MockedStatic<URLEncoder> mockedStaticURLEncoder;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockedStaticUri = mockStatic(Uri.class, CALLS_REAL_METHODS);
        mockedStaticUri.when(() -> Uri.parse(anyString())).thenReturn(mockUri);
        mockedStaticURLEncoder = mockStatic(URLEncoder.class, CALLS_REAL_METHODS);
    }

    @After
    public void tearDown() {
        mockedStaticUri.close();
        mockedStaticURLEncoder.close();
        clearInvocations(mockUri);
    }

    @Test
    public void shouldThrowExceptionWhenCidManagerInstanceIsCalled() {
        Throwable exception = assertThrows(IllegalStateException.class, UrlUtils::new);
        assertEquals("Utility class cannot be instantiated", exception.getMessage());
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenGetQueryParameterNamesReceiveNull() {
        UrlUtils.getQueryParameterNames(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenGetParameterStringReceiveNull() {
        UrlUtils.getParameterString(null);
    }

    @Test
    public void shouldInvalidateReturnedValueWhenGetParameterString() {
        Map<String, String> values = new HashMap<>();
        values.put(KEY_1, VALUE_1);
        values.put(KEY_2, VALUE_2);
        values.put(KEY_3, VALUE_3);
        String parameter = UrlUtils.getParameterString(values);
        assertNotEquals("key1=value1,key2=value2,key3=value3", parameter);
    }

    @Test
    public void shouldValidateReturnedValueWhenGetParameterString() {
        Map<String, String> values = new HashMap<>();
        values.put(KEY_1, VALUE_1);
        values.put(KEY_2, VALUE_2);
        values.put(KEY_3, VALUE_3);
        String parameter = UrlUtils.getParameterString(values);
        assertEquals("key1=value1&key2=value2&key3=value3", parameter);
    }

    @Test
    public void shouldValidateReturnedValueWhenGetParameterStringWithNullKey() {
        Map<String, String> values = new HashMap<>();
        values.put(null, VALUE_1);
        values.put(KEY_2, VALUE_2);
        values.put(KEY_3, VALUE_3);
        String parameter = UrlUtils.getParameterString(values);
        assertEquals("=value1&key2=value2&key3=value3", parameter);
    }

    @Test
    public void shouldValidateReturnedValueWhenGetParameterStringWithNullValue() {
        Map<String, String> values = new HashMap<>();
        values.put(KEY_1, VALUE_1);
        values.put(KEY_2, null);
        values.put(KEY_3, VALUE_3);
        String parameter = UrlUtils.getParameterString(values);
        assertEquals("key1=value1&key2=&key3=value3", parameter);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenGetQueryParameterFromUrlReceiveNull() {
        UrlUtils.getQueryParametersFromUrl(null);
    }

    @Test
    public void shouldValidateReturnedValueWhenGetParameterStringWithURLEncodeException() {
        mockedStaticURLEncoder.when(() -> URLEncoder.encode(anyString(), anyString())).thenThrow(new UnsupportedEncodingException(""));

        Map<String, String> values = new HashMap<>();
        values.put(KEY_1, VALUE_1);
        values.put(KEY_2, VALUE_2);
        values.put(KEY_3, VALUE_3);
        String parameter = UrlUtils.getParameterString(values);
        assertEquals("null=null&null=null&null=null", parameter);
    }

    @Test
    public void shouldInvalidadeReturnedValueWhenGetQueryParameterNames() {
        Uri parse = Uri.parse("http://www.url.com/search?q=random%20word%20%A3500%20bank%20%24");
        Set<String> parameterNames = UrlUtils.getQueryParameterNames(parse);
        assertNotEquals(null, parameterNames);
    }

    @Test
    public void shouldValidadeReturnedValueWhenGetQueryParameterNames() {
        String encode = URLEncoder.encode("q=example", StandardCharsets.UTF_8);
        Uri parse = Uri.parse("http://www.google.com:80/help/me/book%20name+me/?" + encode);
        Set<String> parameterNames = UrlUtils.getQueryParameterNames(parse);
        assertEquals(Collections.emptySet(), parameterNames);
    }

    @Test
    public void shouldInvalidateReturnedValueWhenGetQueryParameterFromUrl() {
        when(mockUri.getEncodedQuery()).thenReturn("q=value");

        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY);
        assertNotEquals(URL_SEARCH, parameters.get("url"));
    }

    @Test
    public void shouldInvalidateReturnedValueWhenGetQueryParameterWithAmpersandFromUrl() {
        when(mockUri.getEncodedQuery()).thenReturn("q=value&");

        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY);
        assertNotEquals(URL_SEARCH, parameters.get("url"));
    }

    @Test
    public void shouldInvalidateReturnedValueWhenGetQueryParameterWithNoValueFromUrl() {
        when(mockUri.getEncodedQuery()).thenReturn("q=");

        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY);
        assertNotEquals(URL_SEARCH, parameters.get("url"));
    }

    @Test
    public void shouldInvalidateReturnedValueWhenGetQueryParameterWithJustQueryNameFromUrl() {
        when(mockUri.getEncodedQuery()).thenReturn("q");

        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY);
        assertNotEquals(URL_SEARCH, parameters.get("url"));
    }

    @Test
    public void shouldInvalidateReturnedValueWhenGetQueryParameterWithNoValueAndTwoSeparatorFromUrl() {
        when(mockUri.getEncodedQuery()).thenReturn("q&a=");

        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY);
        assertNotEquals(URL_SEARCH, parameters.get("url"));
    }

    @Test
    public void shouldValidateReturnedValueWhenGetQueryParameterFromUrl() {
        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl(URL_SEARCH_WITH_QUERY);
        assertEquals(URL_SEARCH_WITH_QUERY, parameters.get("url"));
    }

    @Test
    public void shouldReplaceParameterRequestSignatureValueWhenGetQueryParameterFromUrl() {
        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl("http://www.url.com/search?requestSignature=1234");
        assertEquals("http://www.url.com/search?", parameters.get("url"));
    }

    @Test
    public void shouldValidateReturnedValueWhenGetQueryParameterFromUrlWithManyParameters() {
        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl("http://www.url.com/search?q=value&a=value2&b=value3");
        assertEquals("http://www.url.com/search?q=value&a=value2&b=value3", parameters.get("url"));
    }

}