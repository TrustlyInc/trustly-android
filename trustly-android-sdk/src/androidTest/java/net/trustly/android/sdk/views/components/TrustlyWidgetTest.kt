package net.trustly.android.sdk.views.components

import android.webkit.WebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.interfaces.TrustlyEvents
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.util.last_used.LastUsedBankManager
import net.trustly.android.sdk.views.TrustlyView
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyWidgetTest : TrustlyActivityTest() {

    @Mock
    private lateinit var mockWebView: WebView

    @Mock
    private lateinit var mockTrustlyEvents: TrustlyEvents

    @Before
    override fun setUp() {
        super.setUp()

        MockitoAnnotations.openMocks(this)
    }

    @After
    override fun tearDown() {
        super.tearDown()

        clearInvocations(mockWebView, mockTrustlyEvents)
    }

    @Test
    fun shouldValidateTrustlyWidgetInstance() {
        scenario.onActivity { activity ->
            val trustlyWidget = getTrustlyWidgetInstance(activity)
            assertNotNull(trustlyWidget)
        }
    }

    @Test
    fun shouldValidateTrustlyWidgetInstanceWithEmptyEstablishData() {
        scenario.onActivity { activity ->
            val trustlyWidget = getTrustlyWidgetInstance(activity)
            trustlyWidget.updateEstablishData(mapOf(), 0)
            verify(mockWebView).loadUrl(argThat { url ->
                !url.contains("accessId")
            })
        }
    }

    @Test
    fun shouldValidateTrustlyWidgetInstanceWithEstablishData() {
        scenario.onActivity { activity ->
            val trustlyWidget = getTrustlyWidgetInstance(activity)
            trustlyWidget.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            verify(mockWebView).loadUrl(argThat { url ->
                url.contains("accessId")
            })
        }
    }

    @Test
    fun shouldValidateTrustlyWidgetInstanceWithEstablishDataStatusWidgetLoading() {
        scenario.onActivity { activity ->
            val trustlyWidget = getTrustlyWidgetInstance(activity)
            trustlyWidget.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            verify(mockTrustlyEvents, times(1)).notifyWidgetLoading()
        }
    }

    @Test
    fun shouldValidateTrustlyWidgetInstanceWithEstablishDataStatusWidgetLoaded() {
        scenario.onActivity { activity ->
            val trustlyWidget = getTrustlyWidgetInstance(activity)
            trustlyWidget.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            verify(mockTrustlyEvents, times(1)).notifyWidgetLoaded()
        }
    }

    @Test
    fun shouldValidateTrustlyWidgetInstanceWithEstablishDataStatusWidgetLoadedCustomerFromCanada() {
        scenario.onActivity { activity ->
            val trustlyWidget = getTrustlyWidgetInstance(activity)
            val establishDataValues = EstablishDataMock.getEstablishDataValues()
            establishDataValues["customer.address.country"] = "CA"
            trustlyWidget.updateEstablishData(establishDataValues, 0)
            verify(mockWebView).loadUrl(argThat { url ->
                url.contains("customer.address.country=CA")
            })
        }
    }

    @Test
    fun shouldValidateTrustlyWidgetInstanceWithEstablishDataWithLastUsedBank() {
        scenario.onActivity { activity ->
            LastUsedBankManager.saveLastUsedBank(activity, "eyJsYXN0VXNlZCI6eyJVUyI6IjEyMzQ1Njc4OSJ9fQ==")

            val trustlyWidget = getTrustlyWidgetInstance(activity)
            trustlyWidget.updateEstablishData(EstablishDataMock.getEstablishDataValues(), 0)
            verify(mockWebView).loadUrl(argThat { url ->
                url.contains("lastUsed=123456789")
            })
        }
    }

    private fun getTrustlyWidgetInstance(activity: MockActivity): TrustlyWidget {
        return TrustlyWidget(TrustlyView(activity), activity, mockWebView, mockTrustlyEvents)
    }

}