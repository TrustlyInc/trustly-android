 package net.trustly.android.sdk;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNotEquals;

 import net.trustly.android.sdk.util.UrlUtils;

 import org.junit.Test;

 import java.util.HashMap;
 import java.util.Map;

 public class UrlUtilsTest {

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

 }
