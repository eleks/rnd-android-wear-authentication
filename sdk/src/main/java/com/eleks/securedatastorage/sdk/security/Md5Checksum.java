package com.eleks.securedatastorage.sdk.security;

import android.text.TextUtils;
import android.util.Log;

import com.eleks.securedatastorage.sdk.utils.Constants;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Serhiy.Krasovskyy on 17.06.2015.
 */
public class Md5Checksum {
    private static final String TAG = "MD5";

    public static boolean checkMD5(String md5, String data) {
        if (TextUtils.isEmpty(md5) || TextUtils.isEmpty(data)) {
            Log.e(TAG, "MD5 string empty or updateFile null");
            return false;
        }

        String calculatedDigest = calculateMD5(data);
        if (calculatedDigest == null) {
            Log.e(TAG, "calculatedDigest null");
            return false;
        }

        Log.v(TAG, "Calculated digest: " + calculatedDigest);
        Log.v(TAG, "Provided digest: " + md5);

        return calculatedDigest.equalsIgnoreCase(md5);
    }

    public static String calculateMD5(String data) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Exception while getting digest", e);
            return null;
        }


        try {
            byte[] buffer = data.getBytes(Constants.Security.DEFAULT_CHARSET_NAME);
            digest.update(buffer);
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        }
    }
}
