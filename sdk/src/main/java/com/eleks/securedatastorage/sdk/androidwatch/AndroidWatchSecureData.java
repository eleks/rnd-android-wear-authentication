package com.eleks.securedatastorage.sdk.androidwatch;

import android.content.Context;

import com.eleks.securedatastorage.sdk.interfaces.OnGetHalfOfKeyListener;
import com.eleks.securedatastorage.sdk.interfaces.OnSetHalfOfKeyListener;
import com.eleks.securedatastorage.sdk.interfaces.WearableSecureDataInterface;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;

/**
 * Created by Serhiy.Krasovskyy on 22.06.2015.
 */
public class AndroidWatchSecureData implements WearableSecureDataInterface {


    @Override
    public void getDeviceHalfOfKey(Context context, final String deviceId,
                                   final OnGetHalfOfKeyListener getHalfOfKeyListener) {
        AndroidWatchMethods androidWatchMethods = new AndroidWatchMethods(context);
        androidWatchMethods.setMessageListener(new MessageApi.MessageListener() {
            @Override
            public void onMessageReceived(MessageEvent messageEvent) {
                if (messageEvent != null && messageEvent.getSourceNodeId().equals(deviceId) &&
                        messageEvent.getPath()
                                .equals(AndroidWatchMessages.Responses.GET_DEVICE_HALF_OF_KEY)) {
                    getHalfOfKeyListener.OnGetHalfOfKey(messageEvent.getData());
                }
            }
        });
        androidWatchMethods.sendMessage(deviceId, AndroidWatchMessages.Requests.GET_DEVICE_HALF_OF_KEY, null);
    }

    @Override
    public void setDeviceHalfOfKey(OnSetHalfOfKeyListener setHalfOfKeyListener) {

    }

    @Override
    public boolean isPairedDeviceConnected(Context context, String deviceId) {
        return new AndroidWatchMethods(context).checkIfWearableConnected(deviceId);
    }


}
