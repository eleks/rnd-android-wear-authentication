package com.eleks.securedatastorage.sdk.gwatch;

import android.content.Context;
import android.text.TextUtils;

import com.eleks.securedatastorage.sdk.utils.Constants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Serhiy.Krasovskyy on 22.06.2015.
 */
public class GWatchMethods implements MessageApi.MessageListener {

    private GoogleApiClient mClient;
    private Context mContext;
    private MessageApi.MessageListener mMessageListener;

    public GWatchMethods(Context context) {
        mContext = context;
        final GoogleApiClient client = getGoogleApiClient(mContext);
        Wearable.MessageApi.addListener(client, this);
    }

    public boolean checkIfWearableConnected(String deviceId) {
        final Lock lock = new ReentrantLock();
        final boolean[] stillWaiting = {true};
        final boolean[] result = new boolean[1];
        lock.lock();
        retrieveDeviceNode(new Callback() {
            @Override
            public void findResult(boolean isDeviceExist) {
                if (isDeviceExist) {
                    result[0] = true;
                }
                stillWaiting[0] = false;
            }

        }, deviceId);
        try {
            while (stillWaiting[0]) ;
            return result[0];
        } finally {
            lock.unlock();
        }
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        if (mClient == null)
            mClient = new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .build();
        return mClient;
    }

    public void sendMessage(String deviceId, String message, byte[] data) {
        final GoogleApiClient client = getGoogleApiClient(mContext);
        client.blockingConnect(Constants.G_WATCH.CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
        Wearable.MessageApi.sendMessage(client, deviceId, message, data);
        client.disconnect();
    }

    private void retrieveDeviceNode(final Callback callback, final String deviceId) {
        final GoogleApiClient client = getGoogleApiClient(mContext);
        new Thread(new Runnable() {

            @Override
            public void run() {
                client.blockingConnect
                        (Constants.G_WATCH.CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0 && !TextUtils.isEmpty(deviceId)) {
                    for (Node node : nodes) {
                        if (deviceId.toLowerCase().equals(node.getId().toLowerCase())) {
                            callback.findResult(true);
                            break;
                        }
                    }
                    callback.findResult(false);
                } else {
                    callback.findResult(false);
                }
                client.disconnect();
            }
        }).start();
    }

    public void setMessageListener(MessageApi.MessageListener listener) {
        mMessageListener = listener;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (mMessageListener != null) {
            mMessageListener.onMessageReceived(messageEvent);
        }
    }

    private interface Callback {
        void findResult(final boolean isDeviceExist);
    }
}
