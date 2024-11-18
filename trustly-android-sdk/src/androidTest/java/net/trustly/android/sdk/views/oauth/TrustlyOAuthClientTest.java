package net.trustly.android.sdk.views.oauth;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import net.trustly.android.sdk.mock.MockActivity;
import net.trustly.android.sdk.views.TrustlyView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrustlyOAuthClientTest {

    @Rule
    public ActivityScenarioRule<MockActivity> activityRule = new ActivityScenarioRule<>(MockActivity.class);

    private ActivityScenario<MockActivity> scenario;

    @Before
    public void setUp() {
        scenario = activityRule.getScenario();
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void shouldValidateTrustlyOAuthClientInstance() {
        scenario.onActivity(activity -> {
            TrustlyOAuthClient trustlyOAuthClient = new TrustlyOAuthClient();
            assertNotNull(trustlyOAuthClient);
        });
    }

    @Test(expected = NullPointerException.class)
    public void shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithNullUrl() {
        scenario.onActivity(activity -> new TrustlyOAuthClient().shouldOverrideUrlLoading(new WebView(activity), (String) null));
    }

    @Test
    public void shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithUrl() {
        scenario.onActivity(activity -> {
            TrustlyView.setIsLocalEnvironment(false);
            boolean result = new TrustlyOAuthClient().shouldOverrideUrlLoading(new WebView(activity), "www.url.com");
            assertTrue(result);
        });
    }

    @Test
    public void shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithPayWithMyBankUrl() {
        scenario.onActivity(activity -> {
            TrustlyView.setIsLocalEnvironment(false);
            boolean result = new TrustlyOAuthClient().shouldOverrideUrlLoading(new WebView(activity), getWebResourceRequest("www.paywithmybank.com"));
            assertTrue(result);
        });
    }

    @Test
    public void shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithBothPayWithMyBankUrlAndOAuthLoginPath() {
        scenario.onActivity(activity -> {
            TrustlyView.setIsLocalEnvironment(false);
            boolean result = new TrustlyOAuthClient().shouldOverrideUrlLoading(new WebView(activity), getWebResourceRequest("www.paywithmybank.com/oauth/login/1223456"));
            assertTrue(result);
        });
    }

    @Test
    public void shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithTrustlyOneUrl() {
        scenario.onActivity(activity -> {
            TrustlyView.setIsLocalEnvironment(false);
            boolean result = new TrustlyOAuthClient().shouldOverrideUrlLoading(new WebView(activity), getWebResourceRequest("www.trustly.one"));
            assertTrue(result);
        });
    }

    @Test
    public void shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithBothTrustlyOneUrlAndOAuthLoginPath() {
        scenario.onActivity(activity -> {
            TrustlyView.setIsLocalEnvironment(false);
            boolean result = new TrustlyOAuthClient().shouldOverrideUrlLoading(new WebView(activity), getWebResourceRequest("www.trustly.one/oauth/login/1223456"));
            assertTrue(result);
        });
    }

    @Test
    public void shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithOAuthLoginPath() {
        scenario.onActivity(activity -> {
            TrustlyView.setIsLocalEnvironment(false);
            boolean result = new TrustlyOAuthClient().shouldOverrideUrlLoading(new WebView(activity), getWebResourceRequest("www.url.com/oauth/login/1223456"));
            assertTrue(result);
        });
    }

    @Test
    public void shouldValidateTrustlyOAuthClientShouldOverrideUrlLoadingWithIsLocalEnvironment() {
        scenario.onActivity(activity -> {
            TrustlyView.setIsLocalEnvironment(true);
            boolean result = new TrustlyOAuthClient().shouldOverrideUrlLoading(new WebView(activity), getWebResourceRequest("www.url.com"));
            assertTrue(result);
        });
    }

    private WebResourceRequest getWebResourceRequest(String url) {
        return new WebResourceRequest() {
            @Override
            public Uri getUrl() {
                return Uri.parse(url);
            }

            @Override
            public boolean isForMainFrame() {
                return false;
            }

            @Override
            public boolean isRedirect() {
                return false;
            }

            @Override
            public boolean hasGesture() {
                return false;
            }

            @Override
            public String getMethod() {
                return "GET";
            }

            @Override
            public Map<String, String> getRequestHeaders() {
                return Collections.emptyMap();
            }
        };
    }

}