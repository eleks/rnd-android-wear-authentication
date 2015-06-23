package com.eleks.securedatastorage.sdk.androidwatch;

import android.test.InstrumentationTestCase;

import com.eleks.securedatastorage.sdk.interfaces.OnGetPairedDevice;

/**
 * Created by Serhiy.Krasovskyy on 22.06.2015.
 */
public class AndroidWatchMethods_Test extends InstrumentationTestCase {

    public void test_checkIfWearableConnected() {
        AndroidWatchSecureData androidWatchSecureData=new AndroidWatchSecureData(getInstrumentation().getTargetContext());
        androidWatchSecureData.isPairedDeviceConnected("49db30f6", new OnGetPairedDevice() {
            @Override
            public void getPairedDevice(boolean result) {
                assertTrue(result);
            }

            @Override
            public void getError(String errorMessage) {

            }
        });
    }
}
