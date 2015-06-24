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
