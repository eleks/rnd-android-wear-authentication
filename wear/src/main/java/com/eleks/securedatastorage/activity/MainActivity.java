package com.eleks.securedatastorage.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eleks.securedatastorage.R;
import com.eleks.securedatastorage.utils.Constants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity {

    private GoogleApiClient mClient;
    private String mPhoneId;


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
        initApi();
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Button okButton = (Button) stub.findViewById(R.id.ok_button);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pairDevices();
                        finish();
                    }
                });
                Button cancelButton = (Button) stub.findViewById(R.id.cancel_button);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        });
    }

    private void pairDevices() {
        Toast.makeText(this, "Phone Id = " + mPhoneId, Toast.LENGTH_LONG).show();
    }

    private void initApi() {
        mClient = getGoogleApiClient(this);
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    public void getExtras() {
        mPhoneId = getIntent().getExtras().getString(Constants.Extras.PHONE_ID, null);
    }
}
