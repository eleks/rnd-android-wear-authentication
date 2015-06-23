package com.eleks.securedatastorage.service;

import android.text.TextUtils;

import com.eleks.securedatastorage.activity.MainActivity;
import com.eleks.securedatastorage.manager.AndroidWatchSecurityManager;
import com.eleks.securedatastorage.sdk.androidwatch.AndroidWatchMessages;
import com.eleks.securedatastorage.sdk.androidwatch.AndroidWatchMethods;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Serhiy.Krasovskyy on 22.06.2015.
 */
public class AndroidWatchMessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent != null && !TextUtils.isEmpty(messageEvent.getPath())) {
            switch (messageEvent.getPath()) {
                case AndroidWatchMessages.Requests.SHOULD_USE_THIS_DEVICE_FOR_SECURE_STORAGE:
                    MainActivity.start(this, messageEvent.getSourceNodeId());
                    break;
                case AndroidWatchMessages.Requests.GET_DEVICE_HALF_OF_KEY:
                    if (isPhoneIdValid(messageEvent.getSourceNodeId())) {
                        byte[] data = new AndroidWatchSecurityManager(
                                AndroidWatchMessageService.this)
                                .getDeviceHalfOfKey();
                        new AndroidWatchMethods(AndroidWatchMessageService.this)
                                .sendMessage(messageEvent.getSourceNodeId(),
                                        AndroidWatchMessages.Responses.GET_DEVICE_HALF_OF_KEY,
                                        data);
                    }
                    break;
                case AndroidWatchMessages.Requests.SET_DEVICE_HALF_OF_KEY:
                    if (isPhoneIdValid(messageEvent.getSourceNodeId())) {
                        new AndroidWatchSecurityManager(AndroidWatchMessageService.this)
                                .setDeviceHalfOfKey(messageEvent.getData());
                    }
                    break;
            }
        }
    }

    private boolean isPhoneIdValid(String phoneId) {
        String phoneIdFromWatch = new AndroidWatchSecurityManager(AndroidWatchMessageService.this)
                .getPhoneId();
        return !TextUtils.isEmpty(phoneId) && phoneId.equals(phoneIdFromWatch);
    }
}
