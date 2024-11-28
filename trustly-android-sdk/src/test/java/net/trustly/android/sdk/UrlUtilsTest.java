 package net.trustly.android.sdk;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNotEquals;

 import net.trustly.android.sdk.util.UrlUtils;

 import org.junit.Test;

 import java.util.HashMap;
 import java.util.Map;

 public class UrlUtilsTest {

     private static final String VALUE_1 = "value1";
     private static final String VALUE_2 = "value2";
     private static final String VALUE_3 = "value3";

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
         values.put("key1", VALUE_1);
         values.put("key2", VALUE_2);
         values.put("key3", VALUE_3);
         String parameter = UrlUtils.getParameterString(values);
         assertNotEquals("key1=value1,key2=value2,key3=value3", parameter);
     }

     @Test
     public void shouldValidateReturnedValueWhenGetParameterString() {
         Map<String, String> values = new HashMap<>();
         values.put("key1", VALUE_1);
         values.put("key2", VALUE_2);
         values.put("key3", VALUE_3);
         String parameter = UrlUtils.getParameterString(values);
         assertEquals("key1=value1&key2=value2&key3=value3", parameter);
     }

     @Test
     public void shouldValidateReturnedValueWhenGetParameterStringWithNullKey() {
         Map<String, String> values = new HashMap<>();
         values.put(null, VALUE_1);
         values.put("key2", VALUE_2);
         values.put("key3", VALUE_3);
         String parameter = UrlUtils.getParameterString(values);
         assertEquals("=value1&key2=value2&key3=value3", parameter);
     }

     @Test
     public void shouldValidateReturnedValueWhenGetParameterStringWithNullValue() {
         Map<String, String> values = new HashMap<>();
         values.put("key1", VALUE_1);
         values.put("key2", null);
         values.put("key3", VALUE_3);
         String parameter = UrlUtils.getParameterString(values);
         assertEquals("key1=value1&key2=&key3=value3", parameter);
     }

 }
