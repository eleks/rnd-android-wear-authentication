package com.eleks.securedatastorage.sdk.wearable;

import android.content.Context;

import com.eleks.securedatastorage.sdk.dialogs.PasswordDialog;
import com.eleks.securedatastorage.sdk.interfaces.OnGetDeviceHalfOfKey;
import com.eleks.securedatastorage.sdk.interfaces.WearableSecureDataInterface;

/**
 * Created by Serhiy.Krasovskyy on 19.06.2015.
 */
public class WearableManager {

    private final Context mContext;
    private final WearableSecureDataInterface mWearableInterface;
    private OnGetDeviceHalfOfKey mGetHalfOfKeyListener;

    public WearableManager(Context context, WearableSecureDataInterface wearableInterface) {
        this.mContext = context;
        this.mWearableInterface = wearableInterface;
    }

    public void getDeviceHalfOfKey(String deviceId, OnGetDeviceHalfOfKey getHalfOfKeyListener) {
        this.mGetHalfOfKeyListener = getHalfOfKeyListener;
        if (mWearableInterface != null) {
            mWearableInterface.getDeviceHalfOfKey(deviceId, getHalfOfKeyListener);
        } else {
            new PasswordDialog(mContext).show(getHalfOfKeyListener);
        }
    }
}
