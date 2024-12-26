package net.trustly.android.sdk.views.clients;

import static org.junit.Assert.assertNotNull;

import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import net.trustly.android.sdk.TrustlyActivityTest;
import net.trustly.android.sdk.views.TrustlyView;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrustlyWebViewClientTest extends TrustlyActivityTest {

    private static final String URL = "www.trustly.com";

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
    public void shouldValidateTrustlyWebViewClientInstance() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            assertNotNull(trustlyWebViewClient);
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithUrlString() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.shouldOverrideUrlLoading(webView, URL);
            assertNotNull(trustlyWebViewClient);
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientShouldOverrideUrlLoadingWithWebResourceRequest() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.shouldOverrideUrlLoading(webView, getWebResourceRequest());
            assertNotNull(trustlyWebViewClient);
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnPageFinished() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onPageFinished(webView, URL);
            assertNotNull(trustlyWebViewClient);
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceived() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL);
            assertNotNull(trustlyWebViewClient);
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancel() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            trustlyView.onCancel((trustly, returnParameters) -> assertNotNull(trustly));
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", null);
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelNotLocalEnvironment() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            TrustlyView.setIsLocalEnvironment(false);
            trustlyView.onCancel((trustly, returnParameters) -> assertNotNull(trustly));
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL);
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelJPG() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL + "/image.jpg");
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelDOC() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL + "/document.doc");
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelLocalEnvironment() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            TrustlyView.setIsLocalEnvironment(true);
            trustlyView.onCancel((trustly, returnParameters) -> assertNotNull(trustly));
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL + "/image.jpg");
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelNullLocalEnvironment() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            TrustlyView.setIsLocalEnvironment(true);
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL + "/image.jpg");
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelLocalEnvironmentDOC() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            TrustlyView.setIsLocalEnvironment(true);
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL + "/document.doc");
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedCallOnCancelNotLocalEnvironmentDOC() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            TrustlyView.setIsLocalEnvironment(false);
            trustlyView.onCancel((trustly, returnParameters) -> assertNotNull(trustly));
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL + "/document.doc");
        });
    }

    @Test(expected = NullPointerException.class)
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedNullPointerExceptionOnCancel() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", null);
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedFailureJPG() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL + "/image.jpg");
            assertNotNull(trustlyWebViewClient);
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedFailureDOC() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, 0, "", URL + "/document.doc");
            assertNotNull(trustlyWebViewClient);
        });
    }

    @Test
    public void shouldValidateTrustlyWebViewClientOnErrorReceivedWithWebResourceRequest() {
        scenario.onActivity(activity -> {
            trustlyView = new TrustlyView(activity.getApplicationContext());
            WebView webView = new WebView(activity.getApplicationContext());
            TrustlyWebViewClient trustlyWebViewClient = new TrustlyWebViewClient(trustlyView);
            trustlyWebViewClient.onReceivedError(webView, getWebResourceRequest(), null);
            assertNotNull(trustlyWebViewClient);
        });
    }

    private WebResourceRequest getWebResourceRequest() {
        return new WebResourceRequest() {
            @Override
            public Uri getUrl() {
                return Uri.parse(URL);
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
