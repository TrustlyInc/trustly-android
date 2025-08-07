package net.trustly.android.sdk.views

import android.content.Context
import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.interfaces.Trustly
import net.trustly.android.sdk.interfaces.TrustlyCallback
import net.trustly.android.sdk.interfaces.TrustlyJsInterface
import net.trustly.android.sdk.mock.MockActivity
import net.trustly.android.sdk.views.components.TrustlyComponent
import net.trustly.android.sdk.views.events.TrustlyEvents
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyViewTest : TrustlyActivityTest() {

    companion object {

        const val SHARED_PREFERENCES_FILE_NAME: String = "PayWithMyBank"
        const val GRP_KEY: String = "grp"
        const val ENV: String = "env"
        const val ENV_LOCAL: String = "local"
        const val ENV_HOST: String = "envHost"
        const val DEVICE_TYPE: String = "deviceType"
        const val ANDROID: String = "android"
        const val PT_BR: String = "pt_BR"
        const val METADATA_LANG: String = "metadata.lang"
        const val METADATA_INTEGRATION_CONTEXT: String = "metadata.integrationContext"
        const val EVENT: String = "event"
        const val CUSTOMER_ADDRESS_COUNTRY: String = "customer.address.country"

    }

    private lateinit var trustlyView: TrustlyView

    @Test
    fun shouldValidateTrustlyViewInstance() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            assertNotNull(trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewInstanceWithAttributeSet() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext, null)
            assertNotNull(trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewInstanceWithAttributeSetAndStyle() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext, null)
            assertNotNull(trustlyView)
        }
    }

    @Test
    fun shouldValidateSharedPreferences() {
        scenario.onActivity { activity: MockActivity ->
            val editor = getSharedPreferences(activity.applicationContext).edit()
            editor.putInt(GRP_KEY, 32)
            editor.commit()

            trustlyView = TrustlyView(activity.applicationContext)
            assertNotNull(trustlyView)
            assertEquals(
                32,
                getSharedPreferences(activity.applicationContext).getInt(
                    GRP_KEY,
                    0
                ).toLong()
            )
        }
    }

    @Test
    fun shouldValidateSharedPreferencesNull() {
        scenario.onActivity { activity: MockActivity ->
            val editor = getSharedPreferences(activity.applicationContext).edit()
            editor.remove("PayWithMyBank")
            editor.commit()

            trustlyView = TrustlyView(activity.applicationContext)
            assertNotNull(trustlyView)
            assertNotEquals(
                101,
                getSharedPreferences(activity.applicationContext).getInt(
                    GRP_KEY,
                    0
                ).toLong()
            )
        }
    }

    @Test
    fun shouldValidateTrustlyViewInstanceSharedPreferencesLessThanZero() {
        scenario.onActivity { activity: MockActivity ->
            val editor = getSharedPreferences(activity.applicationContext).edit()
            editor.putInt(GRP_KEY, -10)
            editor.commit()

            trustlyView = TrustlyView(activity.applicationContext)
            assertNotNull(trustlyView)

            val result = getSharedPreferences(activity.applicationContext).getInt(
                GRP_KEY,
                0
            )
            assertNotEquals(-10, result.toLong())
        }
    }

    @Test
    fun shouldValidateTrustlyJsInterfaceResizeMethod() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyJsInterface = TrustlyJsInterface(trustlyView, TrustlyEvents(), TrustlyComponent.Type.WIDGET)
            trustlyJsInterface.resize(100f, 0f)
            assertNotNull(trustlyJsInterface)
        }
    }

    @Test
    fun shouldValidateTrustlyJsInterfaceResizeMethodSameWidthAndHeight() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val trustlyJsInterface = TrustlyJsInterface(trustlyView, TrustlyEvents(), TrustlyComponent.Type.WIDGET)
            trustlyJsInterface.resize(100f, 100f)
            assertNotNull(trustlyJsInterface)
        }
    }

    @Test
    fun shouldValidateTrustlyViewHybridMethod() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result = trustlyView.hybrid(
                "http://www.url.com",
                "http://www.url.com/return",
                "http://www.url.com/cancel"
            )
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewSetListenerMethod() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result =
                trustlyView.setListener { eventName: String, eventDetails: HashMap<String, String>? ->
                    assertEquals(EVENT, eventName)
                    assertEquals(HashMap<Any, Any>(), eventDetails)
                }
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewSetListenerMethodWithNullValue() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result = trustlyView.setListener(null)
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewOnReturnMethod() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result = trustlyView.onReturn { _: Trustly, _: Map<String, String>? -> }
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewOnReturnMethodWithNullValue() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result = trustlyView.onReturn(null)
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewOnCancelMethod() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result =
                trustlyView.onCancel { _: Trustly, _: Map<String, String>? -> }
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewOnCancelMethodWithNullValue() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result = trustlyView.onCancel(null)
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewOnBankSelectedMethod() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result =
                trustlyView.onBankSelected { _: Trustly, _: Map<String, String>? -> }
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewOnBankSelectedMethodWithNullValue() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result = trustlyView.onBankSelected(null)
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewDestroyMethod() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result = trustlyView.destroy()
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewProceedToChooseAccountMethod() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            trustlyView.proceedToChooseAccount()
            assertNotNull(trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewOnExternalUrlMethod() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result =
                trustlyView.onExternalUrl { _: Trustly, _: Map<String, String>? -> }
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewOnExternalUrlMethodWithNullValue() {
        scenario.onActivity { activity: MockActivity ->
            trustlyView = TrustlyView(activity.applicationContext)
            val result = trustlyView.onExternalUrl(null)
            assertTrue(result is TrustlyView)
            assertEquals(result, trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewNotifyListenerMethodWithNullListener() {
        scenario.onActivity { activity: MockActivity ->
            val eventDetails = HashMap<String, String>()
            eventDetails["page"] = "widget"
            eventDetails["type"] = "loading"

            trustlyView = TrustlyView(activity.applicationContext)
            trustlyView.setListener(null)
            TrustlyEvents().notifyListener(
                EVENT,
                eventDetails
            )
            assertNotNull(trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewNotifyListenerMethodWithListener() {
        scenario.onActivity { activity: MockActivity ->
            val eventDetailsMap = HashMap<String, String>()
            eventDetailsMap["page"] = "widget"
            eventDetailsMap["type"] = "loading"

            trustlyView = TrustlyView(activity.applicationContext)
            trustlyView.setListener { eventName: String, eventDetails: HashMap<String, String>? ->
                assertEquals(EVENT, eventName)
                assertEquals(eventDetailsMap, eventDetails)
            }
            TrustlyEvents().notifyListener(EVENT, eventDetailsMap)
            assertNotNull(trustlyView)
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethod() {
        scenario.onActivity { activity: MockActivity ->
            callTrustlyViewEstablishMethod(activity, getEstablishData())
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethodWithCompleteParameters() {
        scenario.onActivity { activity: MockActivity ->
            val establishData = getEstablishData()
            establishData[DEVICE_TYPE] = ANDROID
            establishData[ENV] = ENV_LOCAL
            establishData[METADATA_LANG] = PT_BR
            establishData[METADATA_INTEGRATION_CONTEXT] = "InAppBrowser"
            establishData["paymentProviderId"] = "10009899"
            callTrustlyViewEstablishMethod(activity, establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethodWithIntegrationContextEmpty() {
        scenario.onActivity { activity: MockActivity ->
            val establishData = getEstablishData()
            establishData[METADATA_INTEGRATION_CONTEXT] = ""
            callTrustlyViewEstablishMethod(activity, establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyViewSelectBankWidgetMethod() {
        scenario.onActivity { activity: MockActivity ->
            callTrustlyViewSelectBankWidgetMethod(activity, getEstablishData())
        }
    }

    @Test
    fun shouldValidateTrustlyViewSelectBankWidgetMethodWithCompleteParameters() {
        scenario.onActivity { activity: MockActivity ->
            val establishDataNewValues = HashMap<String, String>()
            establishDataNewValues[DEVICE_TYPE] = ANDROID
            establishDataNewValues[CUSTOMER_ADDRESS_COUNTRY] = "us"
            establishDataNewValues[METADATA_LANG] = PT_BR
            val establishData = getCustomEstablishData(establishDataNewValues)
            callTrustlyViewSelectBankWidgetMethod(activity, establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyViewSelectBankWidgetMethodWithCountryNotUS() {
        scenario.onActivity { activity: MockActivity ->
            val establishDataNewValues = HashMap<String, String>()
            establishDataNewValues[CUSTOMER_ADDRESS_COUNTRY] = "br"
            val establishData = getCustomEstablishData(establishDataNewValues)
            callTrustlyViewSelectBankWidgetMethod(activity, establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethodWithProdValueWithoutOnBankSelected() {
        scenario.onActivity { activity: MockActivity ->
            val establishDataNewValues = HashMap<String, String>()
            establishDataNewValues[ENV] = "prod"
            val establishData = getCustomEstablishData(establishDataNewValues)
            callTrustlyViewEstablishMethod(activity, establishData) { _: Trustly, _: Map<String, String>? -> }
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethodWithProdValue() {
        scenario.onActivity { activity: MockActivity ->
            val establishDataNewValues = HashMap<String, String>()
            establishDataNewValues[ENV] = "prod"
            val establishData = getCustomEstablishData(establishDataNewValues)
            callTrustlyViewEstablishMethod(activity, establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethodWithProductionValue() {
        scenario.onActivity { activity: MockActivity ->
            val establishDataNewValues = HashMap<String, String>()
            establishDataNewValues[ENV] = "production"
            val establishData = getCustomEstablishData(establishDataNewValues)
            callTrustlyViewEstablishMethod(activity, establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethodWithDynamicValue() {
        scenario.onActivity { activity: MockActivity ->
            val establishDataNewValues = HashMap<String, String>()
            establishDataNewValues[ENV] = "dynamic"
            val establishData = getCustomEstablishData(establishDataNewValues)
            callTrustlyViewEstablishMethod(activity, establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethodWithVerificationValue() {
        scenario.onActivity { activity: MockActivity ->
            val establishDataNewValues = HashMap<String, String>()
            establishDataNewValues["paymentType"] = "Verification"
            val establishData = getCustomEstablishData(establishDataNewValues)
            callTrustlyViewEstablishMethod(activity, establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethodWithEnvHostNullValue() {
        scenario.onActivity { activity: MockActivity ->
            val establishDataNewValues = HashMap<String, String>()
            establishDataNewValues[ENV] = "dynamic"
            establishDataNewValues[ENV_HOST] = "dev-224190"
            val establishData = getCustomEstablishData(establishDataNewValues)
            callTrustlyViewEstablishMethod(activity, establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethodWithEnvHostLocalhostValue() {
        scenario.onActivity { activity: MockActivity ->
            val establishDataNewValues = HashMap<String, String>()
            establishDataNewValues[ENV] = ENV_LOCAL
            establishDataNewValues[ENV_HOST] = "localhost"
            val establishData = getCustomEstablishData(establishDataNewValues)
            callTrustlyViewEstablishMethod(activity, establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethodWithEnvHostLocalhostEmptyValue() {
        scenario.onActivity { activity: MockActivity ->
            val establishDataNewValues = HashMap<String, String>()
            establishDataNewValues[ENV] = ENV_LOCAL
            establishDataNewValues[ENV_HOST] = ""
            val establishData = getCustomEstablishData(establishDataNewValues)
            callTrustlyViewEstablishMethod(activity, establishData)
        }
    }

    @Test
    fun shouldValidateTrustlyViewEstablishMethodWithEnvHostLocalhostWithIPValue() {
        scenario.onActivity { activity: MockActivity ->
            val establishDataNewValues = HashMap<String, String>()
            establishDataNewValues[ENV] = ENV_LOCAL
            establishDataNewValues[ENV_HOST] = "X.X.X.X"
            val establishData = getCustomEstablishData(establishDataNewValues)
            callTrustlyViewEstablishMethod(activity, establishData)
        }
    }

    private fun callTrustlyViewSelectBankWidgetMethod(
        activity: Context,
        establishData: Map<String, String>
    ) {
        trustlyView = TrustlyView(activity.applicationContext)
        val result = trustlyView.selectBankWidget(establishData)
        assertTrue(result is TrustlyView)
        assertEquals(result, trustlyView)
    }

    private fun callTrustlyViewEstablishMethod(
        activity: Context,
        establishData: Map<String, String>,
        onBankSelected: TrustlyCallback<Trustly, Map<String, String>>? = null
    ) {
        trustlyView = TrustlyView(activity.applicationContext)
        trustlyView.onBankSelected(onBankSelected)
        val result = trustlyView.establish(establishData)
        assertTrue(result is TrustlyView)
        assertEquals(result, trustlyView)
    }

    private fun getEstablishData(): HashMap<String, String> {
        val establishData = HashMap<String, String>()
        establishData["accessId"] = "123456"
        establishData["merchantId"] = "654321"
        return establishData
    }

    private fun getCustomEstablishData(establishDataNewValues: HashMap<String, String>): HashMap<String, String> {
        val establishData = getEstablishData()
        establishData.putAll(establishDataNewValues)
        return establishData
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            SHARED_PREFERENCES_FILE_NAME,
            Context.MODE_PRIVATE
        )
    }

}