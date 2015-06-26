package com.eleks.securedatastorage.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eleks.securedatastorage.R;
import com.eleks.securedatastorage.dialogs.AskUserDialog;
import com.eleks.securedatastorage.sdk.androidwatch.AndroidWatchSecureData;
import com.eleks.securedatastorage.sdk.interfaces.OnGetDecryptedData;
import com.eleks.securedatastorage.sdk.interfaces.OnInitSecureStorage;
import com.eleks.securedatastorage.sdk.interfaces.OnStoreData;
import com.eleks.securedatastorage.sdk.interfaces.WearableDeviceError;
import com.eleks.securedatastorage.sdk.storage.SecureStorageManager;
import com.eleks.securedatastorage.utils.Constants;


public class MainActivity extends ActionBarActivity {

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private SecureStorageManager mSecureStorageManager;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSecureStorageManager = new SecureStorageManager(
                MainActivity.this, new AndroidWatchSecureData(MainActivity.this));
        initControls();
    }

    void showProgressDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = ProgressDialog.show(MainActivity.this,
                        MainActivity.this.getString(R.string.app_name),
                        message);
            }
        });
    }

    void dismissProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        });
    }

    private void initControls() {
        mUserNameEditText = (EditText) findViewById(R.id.user_name);
        mPasswordEditText = (EditText) findViewById(R.id.password);

        final Button initSecureStorageButton = (Button) findViewById(R.id.init_security_storage_button);
        initSecureStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSecureStorageManager.isSecureStorageInitialized()) {
                    new AskUserDialog(
                            MainActivity.this,
                            MainActivity.this
                                    .getString(com.eleks.securedatastorage.securestoragesdk.R.string.ask_user_about_secure_storage))
                            .show(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    initSecureStorage();
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
                storeCredentials();
            }
        });

        Button restoreCredentialsButton = (Button) findViewById(R.id.restore_credentials_button);
        restoreCredentialsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreCredentials();
            }
        });
    }

    private void restoreCredentials() {
        if (mSecureStorageManager.isSecureStorageInitialized()) {
            showProgressDialog(getString(R.string.restore_credentials_message));
            mSecureStorageManager
                    .getString(Constants.Extras.USER_NAME_ENTITY,
                            new OnGetDecryptedData() {
                                @Override
                                public void getDecryptedData(String data) {
                                    if (!TextUtils.isEmpty(data)) {
                                        mUserNameEditText.setText(data);
                                        mSecureStorageManager
                                                .getString(Constants.Extras.PASSWORD_ENTITY,
                                                        new OnGetDecryptedData() {
                                                            @Override
                                                            public void getDecryptedData(String data) {
                                                                if (!TextUtils.isEmpty(data)) {
                                                                    mPasswordEditText.setText(data);
                                                                }
                                                                dismissProgressDialog();
                                                            }

                                                            @Override
                                                            public void getError(WearableDeviceError error,
                                                                                 String errorMessage) {
                                                                Toast.makeText(MainActivity.this, errorMessage,
                                                                        Toast.LENGTH_LONG)
                                                                        .show();
                                                                dismissProgressDialog();
                                                            }
                                                        });
                                    }
                                }

                                @Override
                                public void getError(WearableDeviceError error,
                                                     String errorMessage) {
                                    Toast.makeText(MainActivity.this, errorMessage,
                                            Toast.LENGTH_LONG)
                                            .show();
                                    dismissProgressDialog();
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

    private void storeCredentials() {
        if (mSecureStorageManager.isSecureStorageInitialized()) {
            showProgressDialog(getString(R.string.store_credentials_message));
            String userName = mUserNameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            mSecureStorageManager.clearData();
            mSecureStorageManager.setString(Constants.Extras.USER_NAME_ENTITY, userName);
            mSecureStorageManager.setString(Constants.Extras.PASSWORD_ENTITY, password);
            mSecureStorageManager.storeData(new OnStoreData() {
                @Override
                public void dataStoredSuccessfully() {
                    dismissProgressDialog();
                    Toast.makeText(MainActivity.this,
                            MainActivity.this
                                    .getString(R.string.data_was_stored_successfully),
                            Toast.LENGTH_LONG)
                            .show();
                }

                @Override
                public void getError(WearableDeviceError error, String errorMessage) {
                    dismissProgressDialog();
                    Toast.makeText(MainActivity.this,
                            errorMessage, Toast.LENGTH_LONG).show();
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

    private void initSecureStorage() {
        mSecureStorageManager.initSecureStorage(new OnInitSecureStorage() {
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
    }

}
