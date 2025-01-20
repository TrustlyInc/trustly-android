package net.trustly.android.sdk

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import net.trustly.android.sdk.mock.MockActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.TimeUnit

abstract class TrustlyActivityTest {

    @JvmField
    @Rule
    var activityRule: ActivityScenarioRule<MockActivity> = ActivityScenarioRule(MockActivity::class.java)

    protected lateinit var scenario: ActivityScenario<MockActivity>

    @Before
    open fun setUp() {
        scenario = activityRule.scenario
    }

    @After
    open fun tearDown() {
        scenario.close()
    }

    open fun waitToCloseCustomTabs() {
        TimeUnit.SECONDS.sleep(2L)
    }

}