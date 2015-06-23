package com.eleks.securedatastorage.sdk.interfaces;

/**
 * Created by Serhiy.Krasovskyy on 19.06.2015.
 */
public interface OnGetDeviceHalfOfKey extends OnGetError {
    void OnGetHalfOfKey(byte[] deviceHalfOfKey);
}
