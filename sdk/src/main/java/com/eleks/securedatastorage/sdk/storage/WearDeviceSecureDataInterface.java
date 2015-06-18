package com.eleks.securedatastorage.sdk.storage;

/**
 * Created by Serhiy.Krasovskyy on 18.06.2015.
 */
public interface WearDeviceSecureDataInterface {

    byte[] getWearHalfOfKey();
    boolean isPairedDeviceConnected();
}
