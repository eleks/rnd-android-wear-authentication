package com.eleks.securedatastorage.sdk.interfaces;

/**
 * Created by Serhiy.Krasovskyy on 25.06.2015.
 */
public interface OnGetDecryptedData extends OnGetError {
    void getDecryptedData(String data);
}
