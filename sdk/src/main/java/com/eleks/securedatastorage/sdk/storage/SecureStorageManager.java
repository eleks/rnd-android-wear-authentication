package com.eleks.securedatastorage.sdk.storage;

import android.app.Activity;
import android.content.Context;

import com.eleks.securedatastorage.sdk.dialogs.NoWatchPasswordDialog;
import com.eleks.securedatastorage.sdk.dialogs.PasswordDialog;
import com.eleks.securedatastorage.sdk.interfaces.OnGetDecryptedData;
import com.eleks.securedatastorage.sdk.interfaces.OnGetDeviceHalfOfKey;
import com.eleks.securedatastorage.sdk.interfaces.OnGetPairedDevice;
import com.eleks.securedatastorage.sdk.interfaces.OnGetPairedDeviceId;
import com.eleks.securedatastorage.sdk.interfaces.OnInitSecureStorage;
import com.eleks.securedatastorage.sdk.interfaces.OnStoreData;
import com.eleks.securedatastorage.sdk.interfaces.WearableDeviceError;
import com.eleks.securedatastorage.sdk.interfaces.WearableSecureDataInterface;
import com.eleks.securedatastorage.sdk.model.EntityHolder;
import com.eleks.securedatastorage.sdk.model.SecureAttributes;
import com.eleks.securedatastorage.sdk.security.SecurityKeyBuilder;
import com.eleks.securedatastorage.securestoragesdk.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Serhiy.Krasovskyy on 17.06.2015.
 */
public class SecureStorageManager {

    private final Context mContext;
    private final WearableSecureDataInterface mWearableSecureInterface;
    private ArrayList<EntityHolder> mEntities;

    public SecureStorageManager(Context context,
                                WearableSecureDataInterface wearableSecureInterface) {
        this.mContext = context;
        this.mWearableSecureInterface = wearableSecureInterface;
    }

    public boolean isSecureStorageInitialized() {
        final SecureAttributes secureAttributes = SecureAttributesManager
                .loadSecureAttributes(mContext);
        return secureAttributes != null && secureAttributes.getSalt() != null &&
                secureAttributes.getInitialVector() != null &&
                secureAttributes.getPhoneHalfOfKey() != null;
    }

    public void getData(final String[] entityName, final OnGetDecryptedData getDecryptedData) {
        if (isSecureStorageInitialized()) {
            final SecureAttributes secureAttributes = SecureAttributesManager
                    .loadSecureAttributes(mContext);
            mWearableSecureInterface.isPairedDeviceConnected(secureAttributes.getDeviceId(),
                    new OnGetPairedDevice() {
                        @Override
                        public void getPairedDevice() {
                            mWearableSecureInterface
                                    .getDeviceHalfOfKey(secureAttributes.getDeviceId(),
                                            new OnGetDeviceHalfOfKey() {
                                                @Override
                                                public void OnGetHalfOfKey(byte[] deviceHalfOfKey) {
                                                    secureAttributes.setDeviceHalfOfKey(deviceHalfOfKey);
                                                    new SecureFileManager(
                                                            mContext, secureAttributes).getData(entityName,
                                                            getDecryptedData);
                                                }

                                                @Override
                                                public void getError(WearableDeviceError error,
                                                                     String errorMessage) {
                                                    getDecryptedData.getError(error, errorMessage);
                                                }
                                            });
                        }
                        @Override
                        public void getError(final WearableDeviceError error, final String errorMessage) {
                            final NoWatchPasswordDialog passwordDialog = NoWatchPasswordDialog.getInstance();
                            passwordDialog.setOnOkButtonClickListener(new PasswordDialog.OnOkButtonClickListener() {
                                @Override
                                public void onClick(String password) {
                                    secureAttributes.setDeviceHalfOfKey(new SecurityKeyBuilder(password).getDeviceHalfOfKey());
                                    new SecureFileManager(
                                            mContext, secureAttributes).getData(entityName,
                                            getDecryptedData);
                                }
                            });
                            passwordDialog.setOnCancelButtonClickListener(
                                    new PasswordDialog.OnCancelButtonClickListener() {
                                        @Override
                                        public void onClick() {
                                            getDecryptedData.getError(error, errorMessage);
                                        }
                                    });
                            passwordDialog.show(((Activity) mContext).getFragmentManager(),
                                    PasswordDialog.TAG);
                        }
                    });
        } else {
            getDecryptedData.getError(WearableDeviceError.SECURE_STORAGE_IS_NOT_INITIALIZED,
                    mContext.getString(R.string.secure_storage_is_not_initialized));
        }
    }

    public void setString(String entityName, String value) {
        if (mEntities == null) {
            mEntities = new ArrayList<>();
        }
        mEntities.add(new EntityHolder(entityName, value));
    }

    public void storeData(final OnStoreData storeData) {
        if (isSecureStorageInitialized()) {
            final SecureAttributes secureAttributes = SecureAttributesManager
                    .loadSecureAttributes(mContext);
            mWearableSecureInterface.isPairedDeviceConnected(secureAttributes.getDeviceId(),
                    new OnGetPairedDevice() {
                        @Override
                        public void getPairedDevice() {
                            storeDataSecurely(secureAttributes, storeData);
                        }

                        @Override
                        public void getError(WearableDeviceError error, String errorMessage) {
                            storeData.getError(error, errorMessage);
                        }
                    });
        } else {
            storeData.getError(WearableDeviceError.SECURE_STORAGE_IS_NOT_INITIALIZED,
                    mContext.getString(R.string.secure_storage_is_not_initialized));
        }
    }

    private void storeDataSecurely(final SecureAttributes secureAttributes,
                                   final OnStoreData storeData) {
        mWearableSecureInterface.getDeviceHalfOfKey(secureAttributes.getDeviceId(),
                new OnGetDeviceHalfOfKey() {
                    @Override
                    public void OnGetHalfOfKey(byte[] deviceHalfOfKey) {
                        secureAttributes.setDeviceHalfOfKey(deviceHalfOfKey);
                        new SecureFileManager(mContext, secureAttributes)
                                .storeData(mEntities, storeData);
                    }

                    @Override
                    public void getError(WearableDeviceError error, String errorMessage) {
                        storeData.getError(error, errorMessage);
                    }
                });
    }

    public void initSecureStorage(final OnInitSecureStorage initSecureStorage) {
        final PasswordDialog passwordDialog = PasswordDialog.getInstance();
        passwordDialog.setOnOkButtonClickListener(new PasswordDialog.OnOkButtonClickListener() {
            @Override
            public void onClick(String password) {
                passwordDialog.dismiss();
                processGetPairedDevice(password, initSecureStorage);
            }
        });
        passwordDialog.setOnCancelButtonClickListener(new PasswordDialog.OnCancelButtonClickListener() {
            @Override
            public void onClick() {
                initSecureStorage.getError(WearableDeviceError.USER_CANCEL_OPERATION,
                        mContext.getString(R.string.user_cancel_operation_message));
            }
        });
        passwordDialog.show(((Activity) mContext).getFragmentManager(),
                PasswordDialog.TAG);
    }

    private void processGetPairedDevice(final String password,
                                        final OnInitSecureStorage initSecureStorage) {
        mWearableSecureInterface.getPairedDeviceId(new OnGetPairedDeviceId() {
            @Override
            public void receivedDeviceId(String deviceId) {
                processInitSecureStorage(deviceId, password, initSecureStorage);
            }

            @Override
            public void getError(WearableDeviceError error, String errorMessage) {
                initSecureStorage.getError(WearableDeviceError.CAN_NOT_GET_PAIRED_DEVICE,
                        mContext.getString(R.string.can_not_get_paired_device));
            }
        });
    }

    private void processInitSecureStorage(String deviceId, String password,
                                          OnInitSecureStorage initSecureStorage) {
        SecurityKeyBuilder securityKeyBuilder = new SecurityKeyBuilder(password);
        mWearableSecureInterface.setDeviceHalfOfKey(
                deviceId, securityKeyBuilder.getDeviceHalfOfKey());
        SecureAttributes secureAttributes =
                SecureAttributesBuilder.build(securityKeyBuilder, deviceId);
        try {
            SecureAttributesManager.storeSecureAttributes(mContext, secureAttributes);
            new SecureFileManager(mContext, secureAttributes).clearData();
            initSecureStorage.initSecureStorageSuccessfully();
        } catch (IOException e) {
            initSecureStorage.getError(WearableDeviceError.CAN_NOT_CREATE_SECURE_STORAGE,
                    mContext.getString(R.string.can_not_create_secure_storage));
        }
    }

    public void clearData() {
        mEntities = null;
    }
}
