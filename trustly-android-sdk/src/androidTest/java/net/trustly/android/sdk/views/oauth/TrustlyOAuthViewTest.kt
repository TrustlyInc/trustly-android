package net.trustly.android.sdk.views.oauth

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.mock.MockActivity
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class TrustlyOAuthViewTest : TrustlyActivityTest() {

    @Test
    fun shouldValidateTrustlyOAuthViewInstance() {
        scenario.onActivity { activity: MockActivity ->
            val trustlyOAuthView = TrustlyOAuthView(activity.applicationContext)
            assertNotNull(trustlyOAuthView)
        }
    }

    @Test
    fun shouldValidateTrustlyOAuthViewGetWebViewMethod() {
        scenario.onActivity { activity: MockActivity ->
            val trustlyOAuthView = TrustlyOAuthView(activity.applicationContext)
            assertNotNull(trustlyOAuthView.webView)
        }
    }

}