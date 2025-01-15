package net.trustly.android.sdk.views.components

import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.views.TrustlyView
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyWidgetTest : TrustlyActivityTest() {

    @Test
    fun shouldValidateTrustlyWidgetInstance() {
        scenario.onActivity { activity ->
            val trustlyWidget = TrustlyWidget(activity, WebView(activity), TrustlyView.Status.WIDGET_LOADING, {}, {})
            assertNotNull(trustlyWidget)
        }
    }

}