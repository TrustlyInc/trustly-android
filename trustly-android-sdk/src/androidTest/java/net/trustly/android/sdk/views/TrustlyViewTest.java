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
import net.trustly.android.sdk.test.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrustlyViewTest extends TrustlyActivityTest {

    private static final String SDK_VERSION = BuildConfig.SDK_VERSION;
    private static final String SHARED_PREFERENCES_FILE_NAME = "PayWithMyBank";
    private static final String GRP_KEY = "grp";

    private TrustlyView trustlyView;

    @Before
    public void setUp() {
        super.setUp();
        TrustlyView.resetGrp();
    }

    @After
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
            trustlyView = new TrustlyView(activity.getApplicationContext(), "local");
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
            trustlyView = new TrustlyView(activity.getApplicationContext(), null, "local");
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
            trustlyView = new TrustlyView(activity.getApplicationContext(), null, 0, "local");
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
            establishData.put("deviceType", "android");
            establishData.put("metadata.lang", "pt_BR");
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
                assertEquals(eventName, "event");
                assertEquals(eventDetails, new HashMap<>());
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
            trustlyView.notifyListener("event", eventDetails);
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
                assertEquals(eventName, "event");
                assertEquals(eventDetails, eventDetailsMap);
            });
            trustlyView.notifyListener("event", eventDetailsMap);
            assertNotNull(trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodNullValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.establish(null);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethod() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.establish(getEstablishData());
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithCompleteParameters() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("deviceType", "android");
            establishData.put("env", "local");
            establishData.put("metadata.lang", "pt_BR");
            establishData.put("metadata.integrationContext", "InAppBrowser");
            establishData.put("paymentProviderId", "10009899");
            Trustly result = trustlyView.establish(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithIntegrationContextEmpty() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("metadata.integrationContext", "");
            Trustly result = trustlyView.establish(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithIntegrationContextNull() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("metadata.integrationContext", null);
            Trustly result = trustlyView.establish(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewSelectBankWidgetMethod() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.selectBankWidget(getEstablishData());
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewSelectBankWidgetMethodWithNullValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            Trustly result = trustlyView.selectBankWidget(null);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewSelectBankWidgetMethodWithCompleteParameters() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("deviceType", "android");
            establishData.put("customer.address.country", "us");
            establishData.put("metadata.lang", "pt_BR");
            Trustly result = trustlyView.selectBankWidget(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewSelectBankWidgetMethodWithCountryNull() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("customer.address.country", null);
            Trustly result = trustlyView.selectBankWidget(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewSelectBankWidgetMethodWithCountryNotUS() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("customer.address.country", "br");
            Trustly result = trustlyView.selectBankWidget(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithProdValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("env", "prod");
            Trustly result = trustlyView.establish(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithProductionValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("env", "production");
            Trustly result = trustlyView.establish(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithDynamicValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("env", "dynamic");
            Trustly result = trustlyView.establish(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithVerificationValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("paymentType", "Verification");
            Trustly result = trustlyView.establish(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithEnvHostNullValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("env", "dynamic");
            establishData.put("envHost", "dev-224190");
            Trustly result = trustlyView.establish(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithEnvHostLocalhostValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("env", "local");
            establishData.put("envHost", "localhost");
            Trustly result = trustlyView.establish(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithEnvHostLocalhostNullValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("env", "local");
            establishData.put("envHost", null);
            Trustly result = trustlyView.establish(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    @Test
    public void shouldValidateTrustlyViewEstablishMethodWithEnvHostLocalhostWithIPValue() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            HashMap<String, String> establishData = getEstablishData();
            establishData.put("env", "local");
            establishData.put("envHost", "192.168.0.1");
            Trustly result = trustlyView.establish(establishData);
            assertTrue(result instanceof TrustlyView);
            assertEquals(result, trustlyView);
        });
    }

    private HashMap<String, String> getEstablishData() {
        HashMap<String, String> establishData = new HashMap<>();
        establishData.put("accessId", "123456");
        establishData.put("merchantId", "654321");
        return establishData;
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

}