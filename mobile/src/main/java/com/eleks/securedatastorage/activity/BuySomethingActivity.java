package com.eleks.securedatastorage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.eleks.securedatastorage.R;
import com.eleks.securedatastorage.dialogs.ErrorDialog;
import com.eleks.securedatastorage.model.ParameterHolder;
import com.eleks.securedatastorage.sdk.androidwatch.AndroidWatchSecureData;
import com.eleks.securedatastorage.sdk.interfaces.OnGetDecryptedData;
import com.eleks.securedatastorage.sdk.interfaces.WearableDeviceError;
import com.eleks.securedatastorage.sdk.mockdevice.MockSecureData;
import com.eleks.securedatastorage.sdk.storage.SecureStorageManager;
import com.eleks.securedatastorage.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class BuySomethingActivity extends BaseActivity {

    private SecureStorageManager mSecureStorageManager;
    private List<ParameterHolder> mPaymentParameters;

    public static void start(Context context) {
        Intent intent = new Intent(context, BuySomethingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_something);
        mSecureStorageManager = new SecureStorageManager(BuySomethingActivity.this,
                new AndroidWatchSecureData(BuySomethingActivity.this));
        initControls();
    }

    private void initControls() {
        Button buyButton = (Button) findViewById(R.id.buy_button);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processUserAction(new OnFinishReadSecureData() {
                    @Override
                    public void finishedSuccessfully() {
                        ApprovePaymentActivity.start(BuySomethingActivity.this, mPaymentParameters);
                    }
                });
            }
        });
    }

    private void processUserAction(OnFinishReadSecureData finishReadSecureData) {
        showProgressDialog(getString(R.string.getting_payment_parameters_from_secure_storage));
        mPaymentParameters = new ArrayList<>();
        mPaymentParameters.add(new ParameterHolder(Constants.PaymentParameters.CARD_NUMBER));
        mPaymentParameters.add(new ParameterHolder(Constants.PaymentParameters.EXPIRATION_MONTH));
        mPaymentParameters.add(new ParameterHolder(Constants.PaymentParameters.EXPIRATION_YEAR));
        mPaymentParameters.add(new ParameterHolder(Constants.PaymentParameters.CARD_CVV));
        getPaymentParametersFromSecureStorage(0, finishReadSecureData);
    }

    private void getPaymentParametersFromSecureStorage(final int parameterIdx,
                                                       final OnFinishReadSecureData finishReadSecureData) {
        if (parameterIdx < mPaymentParameters.size()) {
            mSecureStorageManager.getString(mPaymentParameters.get(parameterIdx).parameterName,
                    new OnGetDecryptedData() {
                        @Override
                        public void getDecryptedData(String data) {
                            mPaymentParameters.get(parameterIdx).parameterValue = data;
                            getPaymentParametersFromSecureStorage(parameterIdx + 1,
                                    finishReadSecureData);
                        }

                        @Override
                        public void getError(WearableDeviceError error, String errorMessage) {
                            dismissProgressDialog();
                            finishReadSecureData.finishedSuccessfully();
                        }
                    });
        } else {
            dismissProgressDialog();
            finishReadSecureData.finishedSuccessfully();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.buy_something_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit_payment_info:
                processUserAction(new OnFinishReadSecureData() {
                    @Override
                    public void finishedSuccessfully() {
                        startInitializationActivity();
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startInitializationActivity() {
        InitializeActivity.start(BuySomethingActivity.this, false, mPaymentParameters);
    }

    public interface OnFinishReadSecureData {
        void finishedSuccessfully();
    }
}
