package com.eleks.securedatastorage.sdk.wearable;

import android.content.Context;

import com.eleks.securedatastorage.sdk.dialogs.PasswordDialog;
import com.eleks.securedatastorage.sdk.interfaces.OnGetHalfOfKeyListener;
import com.eleks.securedatastorage.sdk.interfaces.WearableSecureDataInterface;

/**
 * Created by Serhiy.Krasovskyy on 19.06.2015.
 */
public class WearableManager {

    private final Context mContext;
    private final WearableSecureDataInterface mWearableInterface;
    private OnGetHalfOfKeyListener mGetHalfOfKeyListener;

    public WearableManager(Context context, WearableSecureDataInterface wearableInterface) {
        this.mContext = context;
        this.mWearableInterface = wearableInterface;
    }

    public void getDeviceHalfOfKey(String deviceId, OnGetHalfOfKeyListener getHalfOfKeyListener) {
        this.mGetHalfOfKeyListener = getHalfOfKeyListener;
        if (mWearableInterface != null) {
            mWearableInterface.getDeviceHalfOfKey(deviceId, getHalfOfKeyListener);
        } else {
            new PasswordDialog(mContext).show(getHalfOfKeyListener);
        }
    }
}
