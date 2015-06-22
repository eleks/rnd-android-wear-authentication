package com.eleks.securedatastorage.service;

import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
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
                    final String message = new String(messageEvent.getData());
                    NotificationCompat.Builder b = new NotificationCompat.Builder(this);
                    b.setContentText(message);
                    b.setContentTitle("Test Notification");
                    b.setLocalOnly(true);
                    NotificationManagerCompat man = NotificationManagerCompat.from(this);
                    man.notify(0, b.build());

                    MainActivity.start(this);
                    break;
            }
        }
    }
}
