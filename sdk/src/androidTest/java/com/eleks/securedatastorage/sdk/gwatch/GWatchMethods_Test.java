package com.eleks.securedatastorage.sdk.gwatch;

import android.test.InstrumentationTestCase;

/**
 * Created by Serhiy.Krasovskyy on 22.06.2015.
 */
public class GWatchMethods_Test extends InstrumentationTestCase {

    public void test_checkIfWearableConnected() {
        GWatchMethods gWatchMethods = new GWatchMethods(getInstrumentation().getTargetContext());
        boolean result = gWatchMethods.checkIfWearableConnected("cbd903a");
        assertTrue(result);
    }
}
