package com.eleks.securedatastorage.sdk.mockdevice;

import android.content.Context;
import android.text.TextUtils;

import com.eleks.securedatastorage.sdk.interfaces.OnGetDeviceHalfOfKey;
import com.eleks.securedatastorage.sdk.interfaces.OnGetPairedDevice;
import com.eleks.securedatastorage.sdk.interfaces.OnGetPairedDeviceId;
import com.eleks.securedatastorage.sdk.interfaces.WearableSecureDataInterface;
import com.eleks.securedatastorage.sdk.utils.Constants;
import com.eleks.securedatastorage.sdk.utils.IOHelper;

import java.io.File;

/**
 * Created by Serhiy.Krasovskyy on 24.06.2015.
 */
public class MockSecureData implements WearableSecureDataInterface {


    private Context mContext;

    public MockSecureData(Context context) {
        mContext = context;
    }

    @Override
    public void getDeviceHalfOfKey(String deviceId, OnGetDeviceHalfOfKey getHalfOfKeyListener) {
        if (!TextUtils.isEmpty(deviceId) && Constants.MockDevice.DEVICE_ID.equals(deviceId)) {
            File keyFile = new File(mContext.getFilesDir(), Constants.MockDevice.KEY_FILE_NAME);
            if (keyFile.exists()) {
                byte[] deviceHalfOfKey = IOHelper.loadFileSources(keyFile);
                getHalfOfKeyListener.OnGetHalfOfKey(deviceHalfOfKey);
            } else {
                getHalfOfKeyListener.OnGetHalfOfKey(null);
            }
        }
    }

    @Override
    public void setDeviceHalfOfKey(String deviceId, byte[] deviceHalfOfKey) {
        if (!TextUtils.isEmpty(deviceId) && deviceHalfOfKey != null) {
            File keyFile = new File(mContext.getFilesDir(), Constants.MockDevice.KEY_FILE_NAME);
            IOHelper.writeFileSources(keyFile, deviceHalfOfKey);
        }
    }

    @Override
    public void isPairedDeviceConnected(String deviceId, OnGetPairedDevice getPairedDeviceListener) {
        if (!TextUtils.isEmpty(deviceId) && Constants.MockDevice.DEVICE_ID.equals(deviceId)) {
            getPairedDeviceListener.getPairedDevice(true);
        } else {
            getPairedDeviceListener.getPairedDevice(false);
        }
    }

    @Override
    public void getPairedDeviceId(OnGetPairedDeviceId getPairedDeviceId) {
        getPairedDeviceId.receivedDeviceId(Constants.MockDevice.DEVICE_ID);
    }
}
