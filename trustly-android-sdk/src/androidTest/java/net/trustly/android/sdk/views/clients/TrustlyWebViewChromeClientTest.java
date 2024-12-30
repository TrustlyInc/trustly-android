package net.trustly.android.sdk.views.clients;

import static org.junit.Assert.assertNotNull;

import android.os.Message;
import android.webkit.WebView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import net.trustly.android.sdk.TrustlyActivityTest;
import net.trustly.android.sdk.views.TrustlyView;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrustlyWebViewChromeClientTest extends TrustlyActivityTest {

    private TrustlyView trustlyView;

    @After
    @Override
    public void tearDown() {
        super.tearDown();
        if (trustlyView != null) {
            trustlyView = null;
        }
    }

    @Test
    public void shouldValidateTrustlyViewChromeClientInstance() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            TrustlyWebViewChromeClient trustlyViewChromeClient = new TrustlyWebViewChromeClient(trustlyView);
            assertNotNull(trustlyViewChromeClient);
        });
    }

    @Test(expected = NullPointerException.class)
    public void shouldValidateTrustlyViewChromeClientOnCreateWindowMethodNullMessage() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewChromeClient trustlyViewChromeClient = new TrustlyWebViewChromeClient(trustlyView);
            trustlyViewChromeClient.onCreateWindow(webView, false, false, null);
        });
    }

    @Test(expected = ClassCastException.class)
    public void shouldValidateTrustlyViewChromeClientOnCreateWindowMethod() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            Message message = Message.obtain();
            message.obj = new Object();
            TrustlyWebViewChromeClient trustlyViewChromeClient = new TrustlyWebViewChromeClient(trustlyView);
            trustlyViewChromeClient.onCreateWindow(webView, false, false, message);
            assertNotNull(trustlyViewChromeClient);
        });
    }

}