package com.eleks.securedatastorage.sdk.androidwatch;

import android.app.Activity;
import android.content.Context;

import com.eleks.securedatastorage.sdk.interfaces.OnGetDeviceList;
import com.eleks.securedatastorage.sdk.utils.Constants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Serhiy.Krasovskyy on 22.06.2015.
 */
public class AndroidWatchMethods implements MessageApi.MessageListener {

    private static AndroidWatchMethods sAndroidWatchMethods;
    private GoogleApiClient sWearableApiClient;
    private Context mContext;
    private MessageApi.MessageListener mMessageListener;

    private AndroidWatchMethods(Context context) {
        mContext = context;
        getGoogleApiClient(mContext);
        Wearable.MessageApi.addListener(sWearableApiClient, this);
    }

    public static synchronized AndroidWatchMethods getInstance(Context context) {
        if (sAndroidWatchMethods == null) {
            sAndroidWatchMethods = new AndroidWatchMethods(context);
        }
        return sAndroidWatchMethods;
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        if (sWearableApiClient == null)
            sWearableApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .build();
        return sWearableApiClient;
    }

//    public void disconnectWearableClient() {
//        if (sWearableApiClient.isConnected()) {
//            sWearableApiClient.disconnect();
//        }
//    }

    public void sendMessage(final String deviceId, final String message, final byte[] data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!sWearableApiClient.isConnected()) {
                    sWearableApiClient.blockingConnect(
                            Constants.AndroidWatch.CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                }
                Wearable.MessageApi.sendMessage(sWearableApiClient, deviceId, message, data);
            }
        }).start();

    }

    public void getWearableDevicesList(final OnGetDeviceList callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sWearableApiClient.blockingConnect
                        (Constants.AndroidWatch.CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(sWearableApiClient).await();
                List<Node> nodes = result.getNodes();
                List<String> devices = null;
                if (nodes.size() > 0) {
                    devices = new ArrayList<>();
                    for (Node node : nodes) {
                        devices.add(node.getId().toLowerCase());
                    }
                }
                final List<String> finalDevices = devices;
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.foundDevices(finalDevices);
                    }
                });
            }
        }).start();
    }

    public void setMessageListener(MessageApi.MessageListener listener) {
        mMessageListener = listener;
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        if (mMessageListener != null) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMessageListener.onMessageReceived(messageEvent);
                }
            });

        }
    }

}
