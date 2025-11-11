package net.trustly.android.sdk.views

import android.content.Intent
import androidx.core.net.toUri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import net.trustly.android.sdk.TrustlyActivityTest
import net.trustly.android.sdk.mock.MockActivity
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TrustlyRedirectActivityTest : TrustlyActivityTest() {

    @Test
    fun shouldValidateRedirectActivity() {
        scenario.onActivity { activity: MockActivity ->
            val intent = Intent(activity, TrustlyRedirectActivity::class.java)
            activity.startActivity(intent)
            Assert.assertEquals(
                3,
                TrustlyRedirectActivity::class.java.declaredMethods.size
            )
        }
        waitToCloseCustomTabs()
    }

    @Test
    fun shouldValidateRedirectActivityWithSuccessStatusTransactionDetail() {
        scenario.onActivity { activity: MockActivity ->
            val transactionDetail =
                "trustly_url_scheme://transactionId=1234567&transactionType=1&merchantReference=123456&status=2&payment.paymentType=2&payment.paymentProvider.type=1&payment.account.verified=true&panel=1"

            val intent = Intent(
                Intent.ACTION_VIEW, transactionDetail.toUri(), activity,
                TrustlyRedirectActivity::class.java
            )
            activity.startActivity(intent)
            Assert.assertEquals(
                3,
                TrustlyRedirectActivity::class.java.declaredMethods.size
            )
        }
        waitToCloseCustomTabs()
    }

    @Test
    fun shouldValidateRedirectActivityWithFailedStatusTransactionDetail() {
        scenario.onActivity { activity: MockActivity ->
            activity.runOnUiThread {
                val transactionDetail =
                    "trustly_url_scheme://transactionId=1234567&transactionType=1&merchantReference=123456&status=1&payment.paymentType=2&payment.paymentProvider.type=1&payment.account.verified=true&panel=1"

                val intent = Intent(
                    Intent.ACTION_VIEW, transactionDetail.toUri(), activity,
                    TrustlyRedirectActivity::class.java
                )
                activity.startActivity(intent)
            }
            Assert.assertEquals(
                3,
                TrustlyRedirectActivity::class.java.declaredMethods.size
            )
        }
        waitToCloseCustomTabs()
    }

}