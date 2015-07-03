package com.eleks.securedatastorage.sdk.interfaces;

/**
 * Created by Sergey on 23.06.2015.
 */
public enum WearableDeviceError {
    CAN_NOT_GET_PAIRED_DEVICE,
    CAN_NOT_GET_HALF_OF_KEY_FROM_DEVICE,
    SECURE_STORAGE_IS_NOT_INITIALIZED,
    CAN_NOT_CREATE_SECURE_STORAGE,
    EXCEPTION_DURING_STORE_DATA,
    CAN_NOT_DECRYPT_DATA,
    USER_CANCEL_OPEARTION,
    CAN_NOT_GET_DEVICE_LIST
}
