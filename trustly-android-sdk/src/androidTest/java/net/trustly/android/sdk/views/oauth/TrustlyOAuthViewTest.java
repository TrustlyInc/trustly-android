package net.trustly.android.sdk.views.oauth;

import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import net.trustly.android.sdk.views.TrustlyActivityTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class TrustlyOAuthViewTest extends TrustlyActivityTest {

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