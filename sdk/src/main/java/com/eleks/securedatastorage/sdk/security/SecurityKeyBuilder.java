package com.eleks.securedatastorage.sdk.security;

import com.eleks.securedatastorage.sdk.utils.Constants;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.SecretKey;

/**
 * Created by Serhiy.Krasovskyy on 24.06.2015.
 */
public class SecurityKeyBuilder {

    private final String mSalt;
    private final byte[] mInitialVector;
    private int mDeviceHalfOfKeyLength;
    private int mPhoneHalfOfKeyLength;
    private byte[] mSecretKeyByteArray;

    public SecurityKeyBuilder(String password) {
        mSalt = getRandomSalt();
        mInitialVector = getRandomInitialVector();
        Encryption encryption = Encryption.getDefault(password, mSalt, mInitialVector);
        try {
            SecretKey secretKey = encryption.getSecretKey();
            mSecretKeyByteArray = SecurityUtils.serializeObject(secretKey);
            mPhoneHalfOfKeyLength = mSecretKeyByteArray.length / 2;
            mDeviceHalfOfKeyLength = mSecretKeyByteArray.length - mPhoneHalfOfKeyLength;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            e.printStackTrace();
        }
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

    public byte[] getPhoneHalfOfKey() {
        byte[] result = new byte[mPhoneHalfOfKeyLength];
        System.arraycopy(mSecretKeyByteArray, 0, result, 0, mPhoneHalfOfKeyLength);
        return result;
    }

    public byte[] getDeviceHalfOfKey() {
        byte[] result = new byte[mDeviceHalfOfKeyLength];
        System.arraycopy(mSecretKeyByteArray, mPhoneHalfOfKeyLength - 1,
                result, 0, mDeviceHalfOfKeyLength);
        return result;
    }
}
