package com.eleks.securedatastorage.sdk.storage;

import android.content.Context;
import android.content.DialogInterface;

import com.eleks.securedatastorage.sdk.dialogs.AskUserDialog;
import com.eleks.securedatastorage.sdk.model.EntityHolder;
import com.eleks.securedatastorage.sdk.utils.Constants;
import com.eleks.securedatastorage.securestoragesdk.R;

import java.util.ArrayList;

/**
 * Created by Serhiy.Krasovskyy on 17.06.2015.
 */
public class SecureStorageManager {

    private final Context mContext;
    private final SecureStoragePreferences mPreferences;
    private final WearDeviceSecureDataInterface mWearInterface;
    private ArrayList<EntityHolder> mEntities;

    public SecureStorageManager(Context context, WearDeviceSecureDataInterface wearInterface) {
        this.mContext = context;
        this.mWearInterface = wearInterface;
        mPreferences = new SecureStoragePreferences(context);
    }

    public String getString(String entityName, String defaultValue) {
        String result = defaultValue;
        if (mPreferences.getShouldUseSecureStorage() == Constants.Preferences.SHOULD_USE) {
            result = new SecureFileManager(mContext, mWearInterface).getData(entityName);
        }
        return result;
    }

    public void setString(String entityName, String value) {
        if (mEntities == null) {
            mEntities = new ArrayList<>();
        }
        mEntities.add(new EntityHolder(entityName, value));
    }

    public void apply() {
        int shouldUseSecureStorage = mPreferences.getShouldUseSecureStorage();
        if (shouldUseSecureStorage != Constants.Preferences.DONT_USE) {
            if (shouldUseSecureStorage == Constants.Preferences.UNSPECIFIED) {
                new AskUserDialog(
                        mContext, mContext.getString(R.string.ask_user_about_secure_storage))
                        .show(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mPreferences.setShouldUseSecureStorage(
                                        Constants.Preferences.SHOULD_USE);
                                storeDataSecurely();
                            }
                        });
            } else {
                storeDataSecurely();
            }
        }
    }

    private void storeDataSecurely() {
        new SecureFileManager(mContext, mWearInterface).storeData(mEntities);
    }
}
