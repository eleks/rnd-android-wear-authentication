package com.eleks.securedatastorage.sdk.interfaces;

/**
 * Created by Serhiy.Krasovskyy on 18.06.2015.
 */
public interface WearableSecureDataInterface {

    byte[] getDeviceHalfOfKey(OnGetHalfOfKeyListener getHalfOfKeyListener);

    boolean isPairedDeviceConnected();
}
