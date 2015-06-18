package com.eleks.securedatastorage.sdk.model;

import com.eleks.securedatastorage.sdk.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.Random;

/**
 * Created by Serhiy.Krasovskyy on 18.06.2015.
 */
public class SecureAttributes {
    @SerializedName("PhoneHalfOfKey")
    private byte[] mPhoneHalfOfKey;
    @SerializedName("Salt")
    private String mSalt;
    @SerializedName("InitialVector")
    private byte[] mInitialVector;

    public SecureAttributes(byte[] phoneHalfOfKey) {
        this.mPhoneHalfOfKey = phoneHalfOfKey;
        this.mInitialVector = getRandomInitialVector();
        this.mSalt = getRandomSalt();
    }

    public byte[] getPhoneHalfOfKey() {
        return mPhoneHalfOfKey;
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
