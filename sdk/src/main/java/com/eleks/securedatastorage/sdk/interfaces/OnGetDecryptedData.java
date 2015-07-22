package com.eleks.securedatastorage.sdk.interfaces;

import java.util.Map;

/**
 * Created by Serhiy.Krasovskyy on 25.06.2015.
 */
public interface OnGetDecryptedData extends OnGetError {
    void getDecryptedData(Map<String, String> data);
}
