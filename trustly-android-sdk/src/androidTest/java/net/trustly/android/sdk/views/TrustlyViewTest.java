package net.trustly.android.sdk.views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import net.trustly.android.sdk.interfaces.Trustly;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrustlyViewTest extends TrustlyActivityTest {

    private static final String SDK_VERSION = TrustlyView.version;
    private static final String SHARED_PREFERENCES_FILE_NAME = "PayWithMyBank";
    private static final String GRP_KEY = "grp";
    private static final String ENV = "env";
    private static final String ENV_LOCAL = "local";
    private static final String ENV_HOST = "envHost";
    private static final String DEVICE_TYPE = "deviceType";
    private static final String ANDROID = "android";
    private static final String PT_BR = "pt_BR";
    private static final String METADATA_LANG = "metadata.lang";
    private static final String METADATA_INTEGRATION_CONTEXT = "metadata.integrationContext";
    private static final String EVENT = "event";
    private static final String CUSTOMER_ADDRESS_COUNTRY = "customer.address.country";

    private TrustlyView trustlyView;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        TrustlyView.resetGrp();
    }

    @After
    @Override
    public void tearDown() {
        super.tearDown();
        if (trustlyView != null) {
            trustlyView = null;
        }
    }

    @Test
    public void shouldValidateTrustlyViewInstance() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            assertNotNull(trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewInstanceWithEnvironmentLocal() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext(), ENV_LOCAL);
            assertNotNull(trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewInstanceWithAttributeSet() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext(), (AttributeSet) null);
            assertNotNull(trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewInstanceWithAttributeSetAndEnvironmentLocal() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext(), null, ENV_LOCAL);
            assertNotNull(trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewInstanceWithAttributeSetAndStyle() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext(), null, 0);
            assertNotNull(trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewInstanceWithAttributeSetAndStyleAndEnvironmentLocal() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext(), null, 0, ENV_LOCAL);
            assertNotNull(trustlyView);
        });
    }

    @Test
    public void shouldValidateSharedPreferences() {
        scenario.onActivity(activity -> {
            SharedPreferences.Editor editor = getSharedPreferences(activity.getApplicationContext()).edit();
            editor.putInt(GRP_KEY, 32);
            editor.commit();

            trustlyView = new TrustlyView(activity.getApplicationContext());
            assertNotNull(trustlyView);

            assertEquals(32, getSharedPreferences(activity.getApplicationContext()).getInt(GRP_KEY, 0));
        });
    }

    @Test
    public void shouldValidateTrustlyViewInstanceSharedPreferencesLessThanZero() {
        scenario.onActivity(activity -> {
            SharedPreferences.Editor editor = getSharedPreferences(activity.getApplicationContext()).edit();
            editor.putInt(GRP_KEY, -10);
            editor.commit();

            trustlyView = new TrustlyView(activity.getApplicationContext());
            assertNotNull(trustlyView);

            int result = getSharedPreferences(activity.getApplicationContext()).getInt(GRP_KEY, 0);
            assertNotEquals(-10, result);
        });
    }

    @Test
    public void shouldValidateTrustlyViewGetInAppBrowserLaunchUrlMethodWithDefaultParameters() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());

            String result = trustlyView.getInAppBrowserLaunchURL(getEstablishData());

            int grp = getSharedPreferences(activity.getApplicationContext()).getInt(GRP_KEY, -1);
            assertEquals("accessId=123456&deviceType=mobile%3Aandroid%3Aiab&metadata.sdkAndroidVersion=" + SDK_VERSION + "&grp=" + grp + "&merchantId=654321", result);
        });
    }

    @Test
    public void shouldValidateTrustlyViewGetInAppBrowserLaunchUrlMethodWithCompleteParameters() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put(DEVICE_TYPE, ANDROID);
            establishData.put(METADATA_LANG, PT_BR);
            establishData.put("paymentProviderId", "10009899");

            String result = trustlyView.getInAppBrowserLaunchURL(establishData);

            int grp = getSharedPreferences(activity.getApplicationContext()).getInt(GRP_KEY, -1);
            assertEquals("accessId=123456&deviceType=android%3Aandroid%3Aiab&grp=" + grp + "&widgetLoaded=true&merchantId=654321&metadata.lang=pt_BR&paymentProviderId=10009899&lang=pt_BR&metadata.sdkAndroidVersion=" + SDK_VERSION, result);
        });
    }

    @Test
    public void shouldValidateTrustlyViewHybridMethod() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.hybrid("http://www.url.com", "http://www.url.com/return", "http://www.url.com/cancel");
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewSetListenerMethod() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.setListener((eventName, eventDetails) -> {
                assertEquals(EVENT, eventName);
                assertEquals(new HashMap<>(), eventDetails);
            });
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewSetListenerMethodWithNullValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.setListener(null);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewOnReturnMethod() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.onReturn((trustly, returnParameters) -> {});
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewOnReturnMethodWithNullValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.onReturn(null);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewOnCancelMethod() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.onCancel((trustly, returnParameters) -> {});
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewOnCancelMethodWithNullValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.onCancel(null);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewOnBankSelectedMethod() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.onBankSelected((trustly, returnParameters) -> {});
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewOnBankSelectedMethodWithNullValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.onBankSelected(null);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewDestroyMethod() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.destroy();
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewProceedToChooseAccountMethod() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            trustlyView.proceedToChooseAccount();
            assertNotNull(trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewOnExternalUrlMethod() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.onExternalUrl((trustly, returnParameters) -> {});
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewOnExternalUrlMethodWithNullValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.onExternalUrl(null);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewNotifyListenerMethodWithNullListener() {
        scenario.onActivity(activity -> {
            HashMap<String, String> eventDetails = new HashMap<>();
            eventDetails.put("page", "widget");
            eventDetails.put("type", "loading");

            trustlyView = new TrustlyView(activity.getApplicationContext());
            trustlyView.setListener(null);
            trustlyView.notifyListener(EVENT, eventDetails);
            assertNotNull(trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewNotifyListenerMethodWithListener() {
        scenario.onActivity(activity -> {
            HashMap<String, String> eventDetailsMap = new HashMap<>();
            eventDetailsMap.put("page", "widget");
            eventDetailsMap.put("type", "loading");

            trustlyView = new TrustlyView(activity.getApplicationContext());
            trustlyView.setListener((eventName, eventDetails) -> {
                assertEquals(EVENT, eventName);
                assertEquals(eventDetailsMap, eventDetails);
            });
            trustlyView.notifyListener(EVENT, eventDetailsMap);
            assertNotNull(trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodNullValue() {
        scenario.onActivity(activity -> {
            callTrustlyViewEstablishMethod(activity, null);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethod() {
        scenario.onActivity(activity -> {
            callTrustlyViewEstablishMethod(activity, getEstablishData());
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithCompleteParameters() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishData = getEstablishData();
            establishData.put(DEVICE_TYPE, ANDROID);
            establishData.put(ENV, ENV_LOCAL);
            establishData.put(METADATA_LANG, PT_BR);
            establishData.put(METADATA_INTEGRATION_CONTEXT, "InAppBrowser");
            establishData.put("paymentProviderId", "10009899");

            callTrustlyViewEstablishMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithIntegrationContextEmpty() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishData = getEstablishData();
            establishData.put(METADATA_INTEGRATION_CONTEXT, "");

            callTrustlyViewEstablishMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithIntegrationContextNull() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishData = getEstablishData();
            establishData.put(METADATA_INTEGRATION_CONTEXT, null);

            callTrustlyViewEstablishMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewSelectBankWidgetMethod() {
        scenario.onActivity(activity -> callTrustlyViewSelectBankWidgetMethod(activity, getEstablishData()));
    }

    @Test
    public void shouldValidateTrustlyViewSelectBankWidgetMethodWithNullValue() {
        scenario.onActivity(activity -> callTrustlyViewSelectBankWidgetMethod(activity, null));
    }

    @Test
    public void shouldValidateTrustlyViewSelectBankWidgetMethodWithCompleteParameters() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishDataNewValues = new HashMap<>();
            establishDataNewValues.put(DEVICE_TYPE, ANDROID);
            establishDataNewValues.put(CUSTOMER_ADDRESS_COUNTRY, "us");
            establishDataNewValues.put(METADATA_LANG, PT_BR);
            HashMap<String, String> establishData = getCustomEstablishData(establishDataNewValues);

            callTrustlyViewSelectBankWidgetMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewSelectBankWidgetMethodWithCountryNull() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishDataNewValues = new HashMap<>();
            establishDataNewValues.put(CUSTOMER_ADDRESS_COUNTRY, null);
            HashMap<String, String> establishData = getCustomEstablishData(establishDataNewValues);

            callTrustlyViewSelectBankWidgetMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewSelectBankWidgetMethodWithCountryNotUS() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishDataNewValues = new HashMap<>();
            establishDataNewValues.put(CUSTOMER_ADDRESS_COUNTRY, "br");
            HashMap<String, String> establishData = getCustomEstablishData(establishDataNewValues);

            callTrustlyViewSelectBankWidgetMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithProdValue() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishDataNewValues = new HashMap<>();
            establishDataNewValues.put(ENV, "prod");
            HashMap<String, String> establishData = getCustomEstablishData(establishDataNewValues);

            callTrustlyViewEstablishMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithProductionValue() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishDataNewValues = new HashMap<>();
            establishDataNewValues.put(ENV, "production");
            HashMap<String, String> establishData = getCustomEstablishData(establishDataNewValues);

            callTrustlyViewEstablishMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithDynamicValue() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishDataNewValues = new HashMap<>();
            establishDataNewValues.put(ENV, "dynamic");
            HashMap<String, String> establishData = getCustomEstablishData(establishDataNewValues);

            callTrustlyViewEstablishMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithVerificationValue() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishDataNewValues = new HashMap<>();
            establishDataNewValues.put("paymentType", "Verification");
            HashMap<String, String> establishData = getCustomEstablishData(establishDataNewValues);

            callTrustlyViewEstablishMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithEnvHostNullValue() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishDataNewValues = new HashMap<>();
            establishDataNewValues.put(ENV, "dynamic");
            establishDataNewValues.put(ENV_HOST, "dev-224190");
            HashMap<String, String> establishData = getCustomEstablishData(establishDataNewValues);

            callTrustlyViewEstablishMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithEnvHostLocalhostValue() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishDataNewValues = new HashMap<>();
            establishDataNewValues.put(ENV, ENV_LOCAL);
            establishDataNewValues.put(ENV_HOST, "localhost");
            HashMap<String, String> establishData = getCustomEstablishData(establishDataNewValues);

            callTrustlyViewEstablishMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithEnvHostLocalhostNullValue() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishDataNewValues = new HashMap<>();
            establishDataNewValues.put(ENV, ENV_LOCAL);
            establishDataNewValues.put(ENV_HOST, null);
            HashMap<String, String> establishData = getCustomEstablishData(establishDataNewValues);

            callTrustlyViewEstablishMethod(activity, establishData);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithEnvHostLocalhostWithIPValue() {
        scenario.onActivity(activity -> {
            HashMap<String, String> establishDataNewValues = new HashMap<>();
            establishDataNewValues.put(ENV, ENV_LOCAL);
            establishDataNewValues.put(ENV_HOST, "X.X.X.X");
            HashMap<String, String> establishData = getCustomEstablishData(establishDataNewValues);

            callTrustlyViewEstablishMethod(activity, establishData);
        });
    }

    private void callTrustlyViewSelectBankWidgetMethod(Context activity, HashMap<String, String> establishData) {
        trustlyView = new TrustlyView(activity.getApplicationContext());
        Trustly result = trustlyView.selectBankWidget(establishData);
        assertTrue(result instanceof TrustlyView);
        assertEquals(result, trustlyView);
    }

    private void callTrustlyViewEstablishMethod(Context activity, HashMap<String, String> establishData) {
        trustlyView = new TrustlyView(activity.getApplicationContext());
        Trustly result = trustlyView.establish(establishData);
        assertTrue(result instanceof TrustlyView);
        assertEquals(result, trustlyView);
    }

    private HashMap<String, String> getEstablishData() {
        HashMap<String, String> establishData = new HashMap<>();
        establishData.put("accessId", "123456");
        establishData.put("merchantId", "654321");
        return establishData;
    }

    private HashMap<String, String> getCustomEstablishData(HashMap<String, String> establishDataNewValues) {
        HashMap<String, String> establishData = getEstablishData();
        establishData.putAll(establishDataNewValues);
        return establishData;
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

}