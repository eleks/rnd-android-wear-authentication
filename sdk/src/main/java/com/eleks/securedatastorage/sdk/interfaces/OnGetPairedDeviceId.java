package com.eleks.securedatastorage.sdk.interfaces;

/**
 * Created by Serhiy.Krasovskyy on 23.06.2015.
 */
public interface OnGetPairedDeviceId extends OnGetError {
    void receivedDeviceId(String deviceId);
}
