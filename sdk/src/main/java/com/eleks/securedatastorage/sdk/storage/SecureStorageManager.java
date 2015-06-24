package com.eleks.securedatastorage.sdk.storage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import com.eleks.securedatastorage.sdk.dialogs.AskUserDialog;
import com.eleks.securedatastorage.sdk.dialogs.ErrorDialog;
import com.eleks.securedatastorage.sdk.dialogs.PasswordDialog;
import com.eleks.securedatastorage.sdk.interfaces.OnGetPairedDeviceId;
import com.eleks.securedatastorage.sdk.interfaces.WearableDeviceError;
import com.eleks.securedatastorage.sdk.interfaces.WearableSecureDataInterface;
import com.eleks.securedatastorage.sdk.model.EntityHolder;
import com.eleks.securedatastorage.sdk.model.SecureAttributes;
import com.eleks.securedatastorage.sdk.security.SecurityKeyBuilder;
import com.eleks.securedatastorage.sdk.utils.Constants;
import com.eleks.securedatastorage.securestoragesdk.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Serhiy.Krasovskyy on 17.06.2015.
 */
public class SecureStorageManager {

    private final Context mContext;
    private final SecureStoragePreferences mPreferences;
    private final WearableSecureDataInterface mWearableSecureInterface;
    private ArrayList<EntityHolder> mEntities;

    public SecureStorageManager(Context context,
                                WearableSecureDataInterface wearableSecureInterface) {
        this.mContext = context;
        this.mWearableSecureInterface = wearableSecureInterface;
        mPreferences = new SecureStoragePreferences(context);
    }

    public String getString(String entityName, String defaultValue) {
        String result = defaultValue;
        if (mPreferences.getShouldUseSecureStorage() == Constants.Preferences.SHOULD_USE) {
            result = new SecureFileManager(mContext).getData(entityName);
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
                initSecureStorage();
            } else {
                storeDataSecurely();
            }
        }
    }

    private void storeDataSecurely() {
        //new SecureFileManager(mContext).storeData(mEntities);
    }

    private void initSecureStorage() {
        new AskUserDialog(
                mContext, mContext.getString(R.string.ask_user_about_secure_storage))
                .show(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final PasswordDialog passwordDialog = PasswordDialog.getInstance();
                        passwordDialog.setOnOkButtonClickListener(new PasswordDialog.OnOkButtonClickListener() {
                            @Override
                            public void onClick(String password) {
                                passwordDialog.dismiss();
                                processgetPairedDevice(password);
                            }
                        });
                        passwordDialog.show(((Activity) mContext).getFragmentManager(),
                                PasswordDialog.TAG);
                    }
                });
    }

    private void processgetPairedDevice(final String password) {
        mWearableSecureInterface.getPairedDeviceId(new OnGetPairedDeviceId() {
            @Override
            public void receivedDeviceId(String deviceId) {
                processInitSecureStorage(deviceId, password);
            }

            @Override
            public void getError(WearableDeviceError error, String errorMessage) {
                new ErrorDialog(mContext, mContext.getString(R.string.can_not_get_paired_device))
                        .show();
            }
        });
    }

    private void processInitSecureStorage(String deviceId, String password) {
        SecurityKeyBuilder securityKeyBuilder = new SecurityKeyBuilder(password);
        mWearableSecureInterface.setDeviceHalfOfKey(
                deviceId, securityKeyBuilder.getDeviceHalfOfKey());
        SecureAttributes secureAttributes =
                SecureAttributesBuilder.build(securityKeyBuilder, deviceId);
        try {
            SecureAttributesManager.storeSecureAttributes(mContext, secureAttributes);
        } catch (IOException e) {
            new ErrorDialog(mContext, mContext.getString(R.string.can_not_create_security_storage));
        }
        //mPreferences.setShouldUseSecureStorage(Constants.Preferences.SHOULD_USE);
    }
}
