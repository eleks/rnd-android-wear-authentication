package com.eleks.securedatastorage.sdk.model;

import com.eleks.securedatastorage.sdk.security.SecurityUtils;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import javax.crypto.SecretKey;

/**
 * Created by Serhiy.Krasovskyy on 18.06.2015.
 */
public class SecureAttributes {
    @SerializedName("WearableDeviceId")
    private String deviceId;
    @SerializedName("PhoneHalfOfKey")
    private byte[] mPhoneHalfOfKey;
    private byte[] mWearDeviceHalfOfKey;
    @SerializedName("Salt")
    private String mSalt;
    @SerializedName("InitialVector")
    private byte[] mInitialVector;

    public SecureAttributes() {
    }

    public void setDeviceHalfOfKey(byte[] wearDeviceHalfOfKey) {
        this.mWearDeviceHalfOfKey = wearDeviceHalfOfKey;
    }

    public SecretKey getSecretKey() {
        SecretKey secretKey = null;
        byte[] keyArray = mergeByteArrays(mPhoneHalfOfKey, mWearDeviceHalfOfKey);
        if (keyArray != null) {
            try {
                secretKey = (SecretKey) SecurityUtils.deserializeObject(keyArray);
            } catch (IOException | ClassNotFoundException e) {
                //do nothing
            }
        }
        return secretKey;
    }

    private byte[] mergeByteArrays(byte[] a1, byte[] a2) {
        byte[] result = null;
        if (a1 != null && a2 != null) {
            result = new byte[a1.length + a2.length];
            System.arraycopy(a1, 0, result, 0, a1.length);
            System.arraycopy(a2, 0, result, a1.length, a2.length);
        }
        return result;
    }

    public String getSalt() {
        return mSalt;
    }

    public void setSalt(String mSalt) {
        this.mSalt = mSalt;
    }

    public byte[] getInitialVector() {
        return mInitialVector;
    }

    public void setInitialVector(byte[] mInitialVector) {
        this.mInitialVector = mInitialVector;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setPhoneHalfOfKey(byte[] mPhoneHalfOfKey) {
        this.mPhoneHalfOfKey = mPhoneHalfOfKey;
    }
}
