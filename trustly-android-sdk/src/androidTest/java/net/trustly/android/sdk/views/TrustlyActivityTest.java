package net.trustly.android.sdk.views;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import net.trustly.android.sdk.mock.MockActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

public abstract class TrustlyActivityTest {

    @Rule
    public ActivityScenarioRule<MockActivity> activityRule = new ActivityScenarioRule<>(MockActivity.class);

    protected ActivityScenario<MockActivity> scenario;

    @Before
    public void setUp() {
        scenario = activityRule.getScenario();
    }

    @After
    public void tearDown() {
        scenario.close();
    }

}
