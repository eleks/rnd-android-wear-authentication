package com.eleks.securedatastorage.sdk.interfaces;

/**
 * Created by Serhiy.Krasovskyy on 18.06.2015.
 */
public interface WearableSecureDataInterface {

    void getDeviceHalfOfKey(String deviceId, OnGetDeviceHalfOfKey getHalfOfKeyListener);

    void setDeviceHalfOfKey(String deviceId, byte[] deviceHalfOfKey);

    void isPairedDeviceConnected(String deviceId,
                                 OnGetPairedDevice getPairedDeviceListener);

    void getPairedDeviceId(OnGetPairedDeviceId getPairedDeviceId);
}
