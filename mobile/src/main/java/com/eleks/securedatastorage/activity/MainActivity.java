package com.eleks.securedatastorage.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eleks.securedatastorage.R;
import com.eleks.securedatastorage.sdk.interfaces.OnGetDecryptedData;
import com.eleks.securedatastorage.sdk.interfaces.OnInitSecureStorage;
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
        mPasswordEditText = (EditText) findViewById(R.id.password);

        Button initSecureStorage = (Button) findViewById(R.id.init_security_storage_button);
        initSecureStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!secureStorageManager.isSecureStorageInitialized()) {
                    secureStorageManager.initSecureStorage(new OnInitSecureStorage() {
                        @Override
                        public void initSecureStorageSuccessfully() {
                            Toast.makeText(MainActivity.this,
                                    MainActivity.this
                                            .getString(R.string.security_storage_was_initialized_successfully_message),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }

                        @Override
                        public void getError(WearableDeviceError error, String errorMessage) {
                            Toast.makeText(MainActivity.this,
                                    errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this,
                            MainActivity.this
                                    .getString(R.string.security_storage_was_initialized_early_message),
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        Button storeCredentialsButton = (Button) findViewById(R.id.store_credentials_button);
        storeCredentialsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secureStorageManager.isSecureStorageInitialized()) {
                    String userName = mUserNameEditText.getText().toString();
                    String password = mPasswordEditText.getText().toString();
                    secureStorageManager.setString(Constants.Extras.USER_NAME_ENTITY, userName);
                    secureStorageManager.setString(Constants.Extras.PASSWORD_ENTITY, password);
                    secureStorageManager.apply();
                } else {
                    Toast.makeText(MainActivity.this,
                            MainActivity.this
                                    .getString(R.string.security_storage_is_not_initialized_message),
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        Button restoreCredentialsButton = (Button) findViewById(R.id.restore_credentials_button);
        restoreCredentialsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secureStorageManager.isSecureStorageInitialized()) {
                    secureStorageManager
                            .getString(Constants.Extras.USER_NAME_ENTITY, "default username",
                                    new OnGetDecryptedData() {
                                        @Override
                                        public void getDecryptedData(String data) {
                                            if (!TextUtils.isEmpty(data)) {
                                                mUserNameEditText.setText(data);
                                            }
                                        }

                                        @Override
                                        public void getError(WearableDeviceError error,
                                                             String errorMessage) {
                                            Toast.makeText(MainActivity.this, errorMessage,
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    });
                    secureStorageManager
                            .getString(Constants.Extras.PASSWORD_ENTITY, "default password",
                                    new OnGetDecryptedData() {
                                        @Override
                                        public void getDecryptedData(String data) {
                                            if (!TextUtils.isEmpty(data)) {
                                                mPasswordEditText.setText(data);
                                            }
                                        }

                                        @Override
                                        public void getError(WearableDeviceError error,
                                                             String errorMessage) {
                                            Toast.makeText(MainActivity.this, errorMessage,
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    });
                } else {
                    Toast.makeText(MainActivity.this,
                            MainActivity.this
                                    .getString(R.string.security_storage_is_not_initialized_message),
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

}
