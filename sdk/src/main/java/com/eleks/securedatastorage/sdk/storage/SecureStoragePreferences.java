package com.eleks.securedatastorage.sdk.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.eleks.securedatastorage.sdk.utils.Constants;

/**
 * Created by Serhiy.Krasovskyy on 18.06.2015.
 */
public class SecureStoragePreferences {

    private final Context mContext;

    public SecureStoragePreferences(Context context) {
        this.mContext = context;
    }

    public int getShouldUseSecureStorage() {
        SharedPreferences preferences = mContext
                .getSharedPreferences(Constants.TAG, Context.MODE_PRIVATE);
        return preferences.getInt(Constants.Preferences.SHOULD_USE_SECURE_STORAGE,
                Constants.Preferences.UNSPECIFIED);
    }

    public void setShouldUseSecureStorage(int value) {
        SharedPreferences.Editor preferencesEditor = mContext
                .getSharedPreferences(Constants.TAG, Context.MODE_PRIVATE).edit();
        preferencesEditor.putInt(Constants.Preferences.SHOULD_USE_SECURE_STORAGE, value);
        preferencesEditor.apply();
    }
}
