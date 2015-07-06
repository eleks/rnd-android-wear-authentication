package com.eleks.securedatastorage.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;

import com.eleks.securedatastorage.R;
import com.eleks.securedatastorage.manager.AndroidWatchSecurityManager;
import com.eleks.securedatastorage.sdk.androidwatch.AndroidWatchMessages;
import com.eleks.securedatastorage.sdk.androidwatch.AndroidWatchMethods;
import com.eleks.securedatastorage.utils.Constants;

public class MainActivity extends Activity {

    private String mPhoneId;
    private Handler mWatchdogHandler;
    private Runnable mWatchDogRunnable;

    public static void start(Context context, String phoneId) {
        Intent startIntent = new Intent(context, MainActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startIntent.putExtra(Constants.Extras.PHONE_ID, phoneId);
        context.startActivity(startIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getExtras();
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                startWatchdogTimer();
                Button okButton = (Button) stub.findViewById(R.id.ok_button);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopWatchDogTimer();
                        pairDevice(true);
                        finish();
                    }
                });
                Button cancelButton = (Button) stub.findViewById(R.id.cancel_button);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopWatchDogTimer();
                        pairDevice(false);
                        finish();
                    }
                });
            }
        });
    }

    private void startWatchdogTimer() {
        mWatchdogHandler = new Handler();
        mWatchDogRunnable = new Runnable() {
            @Override
            public void run() {
                MainActivity.this.finish();
            }
        };
        mWatchdogHandler.postDelayed(mWatchDogRunnable, Constants.DEVICE_OPERATION_DELAY);
    }

    public void getExtras() {
        if (getIntent().getExtras() != null) {
            mPhoneId = getIntent().getExtras().getString(Constants.Extras.PHONE_ID, null);
        }
    }

    private void stopWatchDogTimer() {
        if (mWatchdogHandler != null && mWatchDogRunnable != null) {
            mWatchdogHandler.removeCallbacks(mWatchDogRunnable);
        }
    }

    private void pairDevice(boolean result) {
        if (result) {
            new AndroidWatchSecurityManager(MainActivity.this).setPhoneId(mPhoneId);
        }
        byte[] data = new byte[1];
        if (result) {
            data[0] = 1;
        } else {
            data[0] = 0;
        }
        AndroidWatchMethods.getInstance(MainActivity.this).sendMessage(mPhoneId,
                AndroidWatchMessages.Responses.SHOULD_USE_THIS_DEVICE_FOR_SECURE_STORAGE, data);
    }

}
