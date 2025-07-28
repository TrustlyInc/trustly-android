package net.trustly.android.sdk.views.components

import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.views.events.TrustlyEvents
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyWidgetTest : TrustlyActivityTest() {

    private lateinit var trustlyEvents: TrustlyEvents

    @Before
    override fun setUp() {
        super.setUp()

        trustlyEvents = TrustlyEvents()
    }

    @Test
    fun shouldValidateTrustlyWidgetInstance() {
        scenario.onActivity { activity ->
            val trustlyWidget = TrustlyWidget(activity, WebView(activity), trustlyEvents)
            assertNotNull(trustlyWidget)
        }
    }

    @Test
    fun shouldValidateTrustlyWidgetInstanceWithEmptyEstablishData() {
        scenario.onActivity { activity ->
            val trustlyWidget = TrustlyWidget(activity, WebView(activity), trustlyEvents)
            trustlyWidget.updateEstablishData(mapOf(), 0)
            assertNotNull(trustlyWidget)
        }
    }

    @Test
    fun shouldValidateTrustlyWidgetInstanceWithEstablishData() {
        scenario.onActivity { activity ->
            val trustlyWidget = TrustlyWidget(activity, WebView(activity), trustlyEvents)
            trustlyWidget.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            assertNotNull(trustlyWidget)
        }
    }

    @Test
    fun shouldValidateTrustlyWidgetInstanceWithEstablishDataStatusWidgetLoaded() {
        scenario.onActivity { activity ->
            val trustlyWidget = TrustlyWidget(activity, WebView(activity), trustlyEvents)
            trustlyWidget.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            assertNotNull(trustlyWidget)
        }
    }

    @Test
    fun shouldValidateTrustlyWidgetInstanceWithEstablishDataStatusWidgetLoadedCustomerFromCanada() {
        scenario.onActivity { activity ->
            val trustlyWidget = TrustlyWidget(activity, WebView(activity), trustlyEvents)
            val establishDataValues = EstablishDataMock.getEstablishDataValues()
            establishDataValues["customer.address.country"] = "CA"
            trustlyWidget.updateEstablishData(establishDataValues, 0)
            assertNotNull(trustlyWidget)
        }
    }

}