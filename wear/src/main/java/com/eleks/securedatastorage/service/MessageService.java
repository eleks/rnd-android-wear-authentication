package com.eleks.securedatastorage.service;

import android.text.TextUtils;

import com.eleks.securedatastorage.activity.MainActivity;
import com.eleks.securedatastorage.sdk.gwatch.GWatchMessages;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Serhiy.Krasovskyy on 22.06.2015.
 */
public class MessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent != null && !TextUtils.isEmpty(messageEvent.getPath())) {
            switch (messageEvent.getPath()) {
                case GWatchMessages.Requests.SHOULD_USE_THIS_DEVICE_FOR_SECURE_STORAGE:
                    MainActivity.start(this, messageEvent.getSourceNodeId());
                    break;
            }
        }
    }
}
