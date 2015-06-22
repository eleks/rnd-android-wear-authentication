package com.eleks.securedatastorage.sdk.interfaces;

import android.content.Context;

/**
 * Created by Serhiy.Krasovskyy on 18.06.2015.
 */
public interface WearableSecureDataInterface {

    void getDeviceHalfOfKey(Context context, String deviceId,
                              OnGetHalfOfKeyListener getHalfOfKeyListener);

    void setDeviceHalfOfKey(OnSetHalfOfKeyListener setHalfOfKeyListener);

    boolean isPairedDeviceConnected(Context context, String deviceId);
}
