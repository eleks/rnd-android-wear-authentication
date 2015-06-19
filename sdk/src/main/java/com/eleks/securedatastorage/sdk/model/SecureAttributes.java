package com.eleks.securedatastorage.sdk.model;

import com.eleks.securedatastorage.sdk.security.SecurityUtils;
import com.eleks.securedatastorage.sdk.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.Random;

import javax.crypto.SecretKey;

/**
 * Created by Serhiy.Krasovskyy on 18.06.2015.
 */
public class SecureAttributes {
    @SerializedName("PhoneHalfOfKey")
    private byte[] mPhoneHalfOfKey;
    @SerializedName("WearDeviceHalfOfKey")
    private byte[] mWearDeviceHalfOfKey;
    @SerializedName("Salt")
    private String mSalt;
    @SerializedName("InitialVector")
    private byte[] mInitialVector;

    public SecureAttributes(byte[] phoneHalfOfKey) {
        this.mPhoneHalfOfKey = phoneHalfOfKey;
        this.mInitialVector = getRandomInitialVector();
        this.mSalt = getRandomSalt();
    }

    public void setDeviceHalfOfKey(byte[] wearDeviceHalfOfKey) {
        this.mWearDeviceHalfOfKey = wearDeviceHalfOfKey;
    }

    public SecretKey getSecretKey() {
        byte[] keyArray = null;
        if (mPhoneHalfOfKey != null && mWearDeviceHalfOfKey != null) {
            keyArray = new byte[mPhoneHalfOfKey.length + mWearDeviceHalfOfKey.length];
            System.arraycopy(mPhoneHalfOfKey, 0, keyArray, 0, mPhoneHalfOfKey.length);
            System.arraycopy(mWearDeviceHalfOfKey, 0, keyArray, mPhoneHalfOfKey.length,
                    mWearDeviceHalfOfKey.length);
        }
        SecretKey secretKey = null;
        if (keyArray != null) {
            try {
                secretKey = (SecretKey) SecurityUtils.deserializeObject(keyArray);
            } catch (IOException | ClassNotFoundException e) {
                //do nothing
            }
        }
        return secretKey;
    }

    public String getSalt() {
        return mSalt;
    }

    public byte[] getInitialVector() {
        return mInitialVector;
    }

    private String getRandomSalt() {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < Constants.Security.SALT_LENGTH; i++) {
            result.append(
                    Character.toString((char) random.nextInt(Constants.Security.MAX_BYTE_VALUE)));
        }
        return result.toString();
    }

    private byte[] getRandomInitialVector() {
        byte[] result = new byte[Constants.Security.INITIAL_VECTOR_LENGTH];
        Random random = new Random();
        for (int i = 0; i < Constants.Security.INITIAL_VECTOR_LENGTH; i++) {
            result[i] = (byte) random.nextInt(Constants.Security.MAX_BYTE_VALUE);
        }
        return result;
    }
}
