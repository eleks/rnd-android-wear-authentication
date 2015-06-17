package com.eleks.securedatastorage.sdk.security;


import com.eleks.securedatastorage.utils.Constants;

/**
 * Created by Sergey on 25.05.2015.
 */
public class SecurityProvider {
    private static Encryption sEncryption;

    public static synchronized Encryption getEncryption() {
        if (sEncryption == null) {
            sEncryption = Encryption.getDefault(Constants.DEFAULT_ENCRYPTION_KEY,
                    Constants.DEFAULT_ENCRYPTION_SALT, Constants.DEFAULT_ENCRYPTION_IV);
        }
        return sEncryption;
    }
}
