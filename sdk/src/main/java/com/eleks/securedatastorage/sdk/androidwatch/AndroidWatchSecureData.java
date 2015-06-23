package com.eleks.securedatastorage.sdk.androidwatch;

import android.content.Context;
import android.os.Handler;

import com.eleks.securedatastorage.sdk.interfaces.OnGetDeviceList;
import com.eleks.securedatastorage.sdk.interfaces.OnGetError;
import com.eleks.securedatastorage.sdk.interfaces.OnGetHalfOfKeyListener;
import com.eleks.securedatastorage.sdk.interfaces.OnGetPairedDevice;
import com.eleks.securedatastorage.sdk.interfaces.OnGetPairedDeviceId;
import com.eleks.securedatastorage.sdk.interfaces.WearableSecureDataInterface;
import com.eleks.securedatastorage.sdk.utils.Constants;
import com.eleks.securedatastorage.securestoragesdk.R;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * Created by Serhiy.Krasovskyy on 22.06.2015.
 */
public class AndroidWatchSecureData implements WearableSecureDataInterface, Closeable {


    private final Context mContext;
    private AndroidWatchMethods mAndroidWatchMethods;
    private boolean mWatchDogFlag;
    private Handler mWatchdogHandler;
    private Runnable mWatchdogRunnable;

    public AndroidWatchSecureData(Context context) {
        mContext = context;
    }

    @Override
    public void getDeviceHalfOfKey(final String deviceId,
                                   final OnGetHalfOfKeyListener getHalfOfKeyListener) {
        mWatchDogFlag = true;
        startWatchdogTimer(getHalfOfKeyListener,
                mContext.getString(R.string.can_not_get_device_half_of_key));
        mAndroidWatchMethods = new AndroidWatchMethods(mContext);
        mAndroidWatchMethods.setMessageListener(new MessageApi.MessageListener() {
            @Override
            public void onMessageReceived(MessageEvent messageEvent) {
                if (messageEvent != null && messageEvent.getSourceNodeId().equals(deviceId) &&
                        messageEvent.getPath()
                                .equals(AndroidWatchMessages.Responses.GET_DEVICE_HALF_OF_KEY)) {
                    mWatchDogFlag = false;
                    stopWatchDogTask();
                    getHalfOfKeyListener.OnGetHalfOfKey(messageEvent.getData());
                }
            }
        });
        mAndroidWatchMethods
                .sendMessage(deviceId, AndroidWatchMessages.Requests.GET_DEVICE_HALF_OF_KEY, null);
    }

    @Override
    public void setDeviceHalfOfKey(String deviceId, byte[] deviceHalfOfKey) {
        mAndroidWatchMethods = new AndroidWatchMethods(mContext);
        mAndroidWatchMethods.sendMessage(deviceId,
                AndroidWatchMessages.Requests.SET_DEVICE_HALF_OF_KEY, deviceHalfOfKey);
    }

    @Override
    public void isPairedDeviceConnected(final String deviceId,
                                        final OnGetPairedDevice getPairedDeviceListener) {
        mAndroidWatchMethods = new AndroidWatchMethods(mContext);
        mAndroidWatchMethods.getWearableDevicesList(new OnGetDeviceList() {
            @Override
            public void foundDevices(List<String> devices) {
                if (devices != null) {
                    for (String internalDeviceId : devices) {
                        if (internalDeviceId.equals(deviceId)) {
                            getPairedDeviceListener.getPairedDevice(true);
                        }
                    }
                } else {
                    getPairedDeviceListener
                            .getError(mContext.getString(R.string.can_not_get_device_list));
                }
            }
        });
    }

    @Override
    public void getPairedDeviceId(final OnGetPairedDeviceId getPairedDeviceId) {
        mWatchDogFlag = true;
        startWatchdogTimer(getPairedDeviceId,
                mContext.getString(R.string.can_not_get_paired_device));
        mAndroidWatchMethods = new AndroidWatchMethods(mContext);
        mAndroidWatchMethods.setMessageListener(new MessageApi.MessageListener() {
            @Override
            public void onMessageReceived(MessageEvent messageEvent) {
                if (messageEvent.getPath()
                        .equals(AndroidWatchMessages.Responses
                                .SHOULD_USE_THIS_DEVICE_FOR_SECURE_STORAGE)) {
                    if (messageEvent.getData() != null) {
                        boolean result = messageEvent.getData()[0] == 1;
                        if (result) {
                            mWatchDogFlag = false;
                            stopWatchDogTask();
                            getPairedDeviceId.receivedDeviceId(messageEvent.getSourceNodeId());
                        }
                    }
                }
            }
        });
        mAndroidWatchMethods.getWearableDevicesList(new OnGetDeviceList() {
            @Override
            public void foundDevices(List<String> devices) {
                if (devices != null) {
                    for (String deviceId : devices) {
                        mAndroidWatchMethods.sendMessage(deviceId,
                                AndroidWatchMessages.Requests
                                        .SHOULD_USE_THIS_DEVICE_FOR_SECURE_STORAGE,
                                deviceId.getBytes());
                    }
                } else {
                    getPairedDeviceId
                            .getError(mContext.getString(R.string.can_not_get_device_list));
                }
            }
        });
    }

    private void stopWatchDogTask() {
        if (mWatchdogHandler != null && mWatchdogRunnable != null) {
            mWatchdogHandler.removeCallbacks(mWatchdogRunnable);
        }
    }

    private void startWatchdogTimer(final OnGetError getErrorListener, final String errorMessage) {
        mWatchdogHandler = new Handler();
        mWatchdogRunnable = new Runnable() {
            @Override
            public void run() {
                if (mWatchDogFlag) {
                    getErrorListener.getError(errorMessage);
                }
            }
        };
        mWatchdogHandler.postDelayed(mWatchdogRunnable,
                Constants.AndroidWatch.DEVICE_OPERATION_DELAY);
    }

    @Override
    public void close() throws IOException {
        if (mAndroidWatchMethods != null) {
            mAndroidWatchMethods.disconnectWearableClient();
        }
    }
}