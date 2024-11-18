package net.trustly.android.sdk.views.oauth;

import static org.junit.Assert.assertNotNull;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import net.trustly.android.sdk.mock.MockActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class TrustlyOAuthViewTest {

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
    public void shouldValidateTrustlyOAuthViewInstance() {
        scenario.onActivity(activity -> {
            TrustlyOAuthView trustlyOAuthView = new TrustlyOAuthView(activity.getApplicationContext());
            assertNotNull(trustlyOAuthView);
        });
    }

    @Test
    public void shouldValidateTrustlyOAuthViewGetWebViewMethod() {
        scenario.onActivity(activity -> {
            TrustlyOAuthView trustlyOAuthView = new TrustlyOAuthView(activity.getApplicationContext());
            assertNotNull(trustlyOAuthView.getWebView());
        });
    }

}