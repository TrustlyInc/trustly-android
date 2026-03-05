package net.trustly.android.sdk.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import net.trustly.android.sdk.util.UrlUtils
import java.io.Serializable

class TrustlyRedirectActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.data != null && intent.data!!.getQueryParameter(STATUS_PARAM) != null) {
            val transactionDetail = UrlUtils.getQueryParameterNames(intent.data!!)
            val intent = Intent(this, TrustlyCustomTabsManagerActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(
                    TrustlyCustomTabsManagerActivity.ESTABLISH_DATA,
                    transactionDetail as Serializable
                )
            startActivity(intent)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 700)
        }
    }

    companion object {

        private const val STATUS_PARAM = "status"

    }

}