package com.eleks.securedatastorage.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eleks.securedatastorage.R;
import com.eleks.securedatastorage.dialogs.ErrorDialog;
import com.eleks.securedatastorage.fragment.PaymentParametersFragment;
import com.eleks.securedatastorage.sdk.interfaces.OnInitSecureStorage;
import com.eleks.securedatastorage.sdk.interfaces.OnStoreData;
import com.eleks.securedatastorage.sdk.interfaces.WearableDeviceError;
import com.eleks.securedatastorage.sdk.mockdevice.MockSecureData;
import com.eleks.securedatastorage.sdk.storage.SecureStorageManager;
import com.eleks.securedatastorage.utils.Constants;

/**
 * Created by Serhiy.Krasovskyy on 26.06.2015.
 */
public class InitializeActivity extends BaseActivity {

    private PaymentParametersFragment mPaymentParametersFragment;
    private SecureStorageManager mSecureStorageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);
        mSecureStorageManager = new SecureStorageManager(InitializeActivity.this,
                new MockSecureData(InitializeActivity.this));
        if (mSecureStorageManager.isSecureStorageInitialized()) {
            BuySomethingActivity.start(InitializeActivity.this);
            finish();
        }
        initControls();
    }

    private void initControls() {
        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processForm();
            }
        });
        initFragment();
    }

    private void processForm() {
        if (isFormCorrect()) {
            storePaymentParameters();
        } else {
            new ErrorDialog(InitializeActivity.this,
                    getString(R.string.all_fields_should_be_filled_message)).show();
        }
    }

    private void storePaymentParameters() {
        if (!mSecureStorageManager.isSecureStorageInitialized()) {
            mSecureStorageManager.initSecureStorage(new OnInitSecureStorage() {
                @Override
                public void initSecureStorageSuccessfully() {
                    storePaymentParametersToSecureStorage();
                }

                @Override
                public void getError(WearableDeviceError error, String errorMessage) {
                    new ErrorDialog(InitializeActivity.this, errorMessage).show();
                }
            });
        } else {
            storePaymentParametersToSecureStorage();
        }
    }

    private void storePaymentParametersToSecureStorage() {
        showProgressDialog(getString(R.string.store_payment_parameters_message));
        mSecureStorageManager.setString(Constants.PaymentParameters.CARD_NUMBER,
                mPaymentParametersFragment.getCardNumber());
        mSecureStorageManager.setString(Constants.PaymentParameters.EXPIRATION_MONTH,
                mPaymentParametersFragment.getExpirationMonth());
        mSecureStorageManager.setString(Constants.PaymentParameters.EXPIRATION_YEAR,
                mPaymentParametersFragment.getExpirationYear());
        mSecureStorageManager.setString(Constants.PaymentParameters.CARD_CVV,
                mPaymentParametersFragment.getCardCvv());
        mSecureStorageManager.storeData(new OnStoreData() {
            @Override
            public void dataStoredSuccessfully() {
                Toast.makeText(InitializeActivity.this,
                        InitializeActivity.this
                                .getString(R.string.payment_parameters_were_stored_successfully_message),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void getError(WearableDeviceError error, String errorMessage) {
                new ErrorDialog(InitializeActivity.this, errorMessage).show();
            }
        });
    }

    private boolean isFormCorrect() {
        return !TextUtils.isEmpty(mPaymentParametersFragment.getCardNumber()) &&
                !TextUtils.isEmpty(mPaymentParametersFragment.getExpirationMonth()) &&
                !TextUtils.isEmpty(mPaymentParametersFragment.getExpirationYear()) &&
                !TextUtils.isEmpty(mPaymentParametersFragment.getCardCvv());
    }

    private void initFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        mPaymentParametersFragment =
                PaymentParametersFragment.getInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.payment_parameters_container, mPaymentParametersFragment,
                        PaymentParametersFragment.TAG).commit();
    }
}
