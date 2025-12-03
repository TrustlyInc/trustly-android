package net.trustly.android.sdk.views.oauth

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.interfaces.TrustlyEvents
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.views.TrustlyView
import net.trustly.android.sdk.views.events.TrustlyEventsImpl
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class TrustlyOAuthViewTest : TrustlyActivityTest() {

    private lateinit var trustlyEvents: TrustlyEvents

    @Before
    override fun setUp() {
        super.setUp()

        trustlyEvents = TrustlyEventsImpl
    }

    @Test
    fun shouldValidateTrustlyOAuthViewInstance() {
        scenario.onActivity { activity: MockActivity ->
            val trustlyOAuthView = TrustlyOAuthView(
                activity.applicationContext,
                TrustlyView(activity.applicationContext),
                trustlyEvents
            )
            assertNotNull(trustlyOAuthView)
        }
    }

    @Test
    fun shouldValidateTrustlyOAuthViewGetWebViewMethod() {
        scenario.onActivity { activity: MockActivity ->
            val trustlyOAuthView = TrustlyOAuthView(
                activity.applicationContext,
                TrustlyView(activity.applicationContext),
                trustlyEvents
            )
            assertNotNull(trustlyOAuthView.webView)
        }
    }

}