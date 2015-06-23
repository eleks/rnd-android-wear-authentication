package com.eleks.securedatastorage.sdk.androidwatch;

import android.test.InstrumentationTestCase;

/**
 * Created by Serhiy.Krasovskyy on 22.06.2015.
 */
public class AndroidWatchMethods_Test extends InstrumentationTestCase {

    public void test_checkIfWearableConnected() {
        AndroidWatchMethods androidWatchMethods = new AndroidWatchMethods(getInstrumentation().getTargetContext());
        boolean result = androidWatchMethods.checkIfWearableConnected("49db30f6");
        assertTrue(result);
    }
}
