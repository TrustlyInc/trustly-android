package net.trustly.android.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import android.net.Uri;

import net.trustly.android.sdk.util.UrlUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Uri.class})
@PowerMockIgnore("jdk.internal.reflect.*")
public class UrlUtilsTest {

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenGetQueryParameterNamesReceiveNull() {
        UrlUtils.getQueryParameterNames(null);
    }

    @Test
    public void shouldInvalidadeReturnedValueWhenGetQueryParameterNames() throws Exception {
        mockUri();
        Uri parse = Uri.parse("http://www.url.com/search?q=random%20word%20%A3500%20bank%20%24");
        Set<String> parameterNames = UrlUtils.getQueryParameterNames(parse);
        assertNotEquals(null, parameterNames);
    }

    @Test
    public void shouldValidadeReturnedValueWhenGetQueryParameterNames() throws Exception {
        mockUri();
        String encode = URLEncoder.encode("q=example", "UTF-8");
        Uri parse = Uri.parse("http://www.google.com:80/help/me/book%20name+me/?" + encode);
        Set<String> parameterNames = UrlUtils.getQueryParameterNames(parse);
        assertEquals(Collections.emptySet(), parameterNames);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenGetQueryParameterFromUrlReceiveNull() throws Exception {
        mockUri();
        UrlUtils.getQueryParametersFromUrl(null);
    }

    @Test
    public void shouldInvalidateReturnedValueWhenGetQueryParameterFromUrl() throws Exception {
        mockUri();
        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl("http://www.url.com/search?q=value");
        assertNotEquals("http://www.url.com/search", parameters.get("url"));
    }

    @Test
    public void shouldValidateReturnedValueWhenGetQueryParameterFromUrl() throws Exception {
        mockUri();
        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl("http://www.url.com/search?q=value");
        assertEquals("http://www.url.com/search?q=value", parameters.get("url"));
    }

    @Test
    public void shouldReplaceParameterRequestSignatureValueWhenGetQueryParameterFromUrl() throws Exception {
        mockUri();
        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl("http://www.url.com/search?requestSignature=1234");
        assertEquals("http://www.url.com/search?", parameters.get("url"));
    }

    @Test
    public void shouldValidateReturnedValueWhenGetQueryParameterFromUrlWithManyParameters() throws Exception {
        mockUri();
        Map<String, String> parameters = UrlUtils.getQueryParametersFromUrl("http://www.url.com/search?q=value&a=value2&b=value3");
        assertEquals("http://www.url.com/search?q=value&a=value2&b=value3", parameters.get("url"));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenGetParameterStringReceiveNull() {
        UrlUtils.getParameterString(null);
    }

    @Test
    public void shouldInvalidateReturnedValueWhenGetParameterString() {
        Map<String, String> values = new HashMap<>();
        values.put("key1", "value1");
        values.put("key2", "value2");
        values.put("key3", "value3");
        String parameter = UrlUtils.getParameterString(values);
        assertNotEquals("key1=value1,key2=value2,key3=value3", parameter);
    }

    @Test
    public void shouldValidateReturnedValueWhenGetParameterString() {
        Map<String, String> values = new HashMap<>();
        values.put("key1", "value1");
        values.put("key2", "value2");
        values.put("key3", "value3");
        String parameter = UrlUtils.getParameterString(values);
        assertEquals("key1=value1&key2=value2&key3=value3", parameter);
    }

    @Test
    public void shouldValidateReturnedValueWhenGetParameterStringWithNullKey() {
        Map<String, String> values = new HashMap<>();
        values.put(null, "value1");
        values.put("key2", "value2");
        values.put("key3", "value3");
        String parameter = UrlUtils.getParameterString(values);
        assertEquals("=value1&key2=value2&key3=value3", parameter);
    }

    @Test
    public void shouldValidateReturnedValueWhenGetParameterStringWithNullValue() {
        Map<String, String> values = new HashMap<>();
        values.put("key1", "value1");
        values.put("key2", null);
        values.put("key3", "value3");
        String parameter = UrlUtils.getParameterString(values);
        assertEquals("key1=value1&key2=&key3=value3", parameter);
    }

    private void mockUri() throws Exception {
        mockStatic(Uri.class);
        PowerMockito.when(Uri.class, "parse", anyString())
                .thenReturn(mock(Uri.class));
    }

}