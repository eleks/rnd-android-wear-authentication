package com.eleks.securedatastorage.sdk.storage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.eleks.securedatastorage.sdk.dialogs.AskUserDialog;
import com.eleks.securedatastorage.sdk.dialogs.ErrorDialog;
import com.eleks.securedatastorage.sdk.dialogs.PasswordDialog;
import com.eleks.securedatastorage.sdk.interfaces.OnGetDeviceHalfOfKey;
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
import java.util.concurrent.CountDownLatch;

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

    public String getString(final String entityName, String defaultValue) {
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] result = new String[1];
        result[0] = defaultValue;
        if (mPreferences.getShouldUseSecureStorage() == Constants.Preferences.SHOULD_USE) {
            final SecureAttributes secureAttributes = SecureAttributesManager
                    .loadSecureAttributes(mContext);
            if (secureAttributes != null && !TextUtils.isEmpty(secureAttributes.getDeviceId())) {
                mWearableSecureInterface
                        .getDeviceHalfOfKey(secureAttributes.getDeviceId(),
                                new OnGetDeviceHalfOfKey() {
                                    @Override
                                    public void OnGetHalfOfKey(byte[] deviceHalfOfKey) {
                                        secureAttributes.setDeviceHalfOfKey(deviceHalfOfKey);
                                        result[0] = new SecureFileManager(
                                                mContext, secureAttributes).getData(entityName);
                                        latch.countDown();
                                    }

                                    @Override
                                    public void getError(WearableDeviceError error,
                                                         String errorMessage) {
                                        latch.countDown();
                                    }
                                });
            } else {
                //TODO need to implement
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            //do nothing
        }
        return result[0];
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
        final SecureAttributes secureAttributes = SecureAttributesManager.loadSecureAttributes(mContext);
        if (secureAttributes != null && !TextUtils.isEmpty(secureAttributes.getDeviceId())) {
            mWearableSecureInterface.getDeviceHalfOfKey(secureAttributes.getDeviceId(),
                    new OnGetDeviceHalfOfKey() {
                        @Override
                        public void OnGetHalfOfKey(byte[] deviceHalfOfKey) {
                            secureAttributes.setDeviceHalfOfKey(deviceHalfOfKey);
                            new SecureFileManager(mContext, secureAttributes).storeData(mEntities);
                        }

                        @Override
                        public void getError(WearableDeviceError error, String errorMessage) {
                            new ErrorDialog(mContext,
                                    mContext.getString(R.string.can_not_get_device_half_of_key))
                                    .show();
                        }
                    });
        }
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
                                processGetPairedDevice(password);
                            }
                        });
                        passwordDialog.show(((Activity) mContext).getFragmentManager(),
                                PasswordDialog.TAG);
                    }
                });
    }

    private void processGetPairedDevice(final String password) {
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
            storeDataSecurely();
        } catch (IOException e) {
            new ErrorDialog(mContext, mContext.getString(R.string.can_not_create_security_storage));
        }
        //mPreferences.setShouldUseSecureStorage(Constants.Preferences.SHOULD_USE);
    }
}
