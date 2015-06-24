package com.eleks.securedatastorage.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eleks.securedatastorage.R;
import com.eleks.securedatastorage.sdk.androidwatch.AndroidWatchSecureData;
import com.eleks.securedatastorage.sdk.interfaces.OnGetPairedDeviceId;
import com.eleks.securedatastorage.sdk.interfaces.WearableDeviceError;
import com.eleks.securedatastorage.sdk.mockdevice.MockSecureData;
import com.eleks.securedatastorage.sdk.storage.SecureStorageManager;
import com.eleks.securedatastorage.utils.Constants;


public class MainActivity extends ActionBarActivity {

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
    }

    private void initControls() {
        final SecureStorageManager secureStorageManager =
                new SecureStorageManager(MainActivity.this, new MockSecureData(MainActivity.this));
        mUserNameEditText = (EditText) findViewById(R.id.user_name);
//        String userName = secureStorageManager.getString(Constants.Extras.USER_NAME_ENTITY, null);
//        if (!TextUtils.isEmpty(userName)) {
//            mUserNameEditText.setText(userName);
//        }
        mPasswordEditText = (EditText) findViewById(R.id.password);
//        String password = secureStorageManager.getString(Constants.Extras.PASSWORD_ENTITY, null);
//        if (!TextUtils.isEmpty(password)) {
//            mPasswordEditText.setText(password);
//        }
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mUserNameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if (isUserNameValid(userName) && isPasswordValid(password)) {
                    secureStorageManager.setString(Constants.Extras.USER_NAME_ENTITY, userName);
                    secureStorageManager.setString(Constants.Extras.PASSWORD_ENTITY, password);
                    secureStorageManager.apply();
                }
            }
        });

        Button testButton = (Button) findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AndroidWatchSecureData androidWatchSecureData =
                        new AndroidWatchSecureData(MainActivity.this);
                androidWatchSecureData.getPairedDeviceId(new OnGetPairedDeviceId() {
                    @Override
                    public void getError(WearableDeviceError error, String errorMessage) {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void receivedDeviceId(final String deviceId) {
                        String message = "Doesn't exist paired device";
                        if (!TextUtils.isEmpty(deviceId)) {
                            message = "Device Id = " + deviceId;
                        }
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private boolean isPasswordValid(String password) {
        //TODO Need to implement
        return true;
    }

    private boolean isUserNameValid(String userName) {
        //TODO Need to implement
        return true;
    }

}
