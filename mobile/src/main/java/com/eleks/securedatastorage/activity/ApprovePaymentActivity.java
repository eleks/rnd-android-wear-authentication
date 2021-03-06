package com.eleks.securedatastorage.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.eleks.securedatastorage.R;
import com.eleks.securedatastorage.dialogs.ErrorDialog;
import com.eleks.securedatastorage.fragment.PaymentParametersFragment;
import com.eleks.securedatastorage.model.ParameterHolder;
import com.eleks.securedatastorage.utils.Constants;

import java.util.List;


public class ApprovePaymentActivity extends BaseActivity {

    private String mCardNumber;
    private String mExpirationMonth;
    private String mExpirationYear;
    private String mCardCvv;
    private PaymentParametersFragment mPaymentParametersFragment;

    public static void start(Context context, List<ParameterHolder> paymentParameters) {
        Intent intent = new Intent(context, ApprovePaymentActivity.class);
        for (ParameterHolder parameterHolder : paymentParameters) {
            intent.putExtra(parameterHolder.parameterName, parameterHolder.parameterValue);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_payment);
        getExtras();
        initControls();
    }

    private void initControls() {
        initFragment();
        Button approveButton = (Button) findViewById(R.id.approve_payment_button);
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPaymentInfoCorrect()) {
                    processPayment();
                } else {
                    new ErrorDialog(ApprovePaymentActivity.this,
                            ApprovePaymentActivity.this
                                    .getString(R.string.all_fields_should_be_filled_message))
                            .show();
                }
            }
        });
    }

    private boolean isPaymentInfoCorrect() {
        return !TextUtils.isEmpty(mPaymentParametersFragment.getCardNumber()) &&
                !TextUtils.isEmpty(mPaymentParametersFragment.getExpirationMonth()) &&
                !TextUtils.isEmpty(mPaymentParametersFragment.getExpirationYear()) &&
                !TextUtils.isEmpty(mPaymentParametersFragment.getCardCvv());
    }

    private void processPayment() {
        showProgressDialog(getString(R.string.make_payment_message));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                CongratulationsActivity.start(ApprovePaymentActivity.this);
                finish();
            }
        }, Constants.PAYMENT_DELAY);
    }

    private void getExtras() {
        mCardNumber = getIntent().getExtras()
                .getString(Constants.PaymentParameters.CARD_NUMBER, null);
        mExpirationMonth = getIntent().getExtras()
                .getString(Constants.PaymentParameters.EXPIRATION_MONTH, null);
        mExpirationYear = getIntent().getExtras()
                .getString(Constants.PaymentParameters.EXPIRATION_YEAR, null);
        mCardCvv = getIntent().getExtras()
                .getString(Constants.PaymentParameters.CARD_CVV, null);
    }

    private void initFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        mPaymentParametersFragment =
                PaymentParametersFragment.getInstance(mCardNumber, mExpirationMonth,
                        mExpirationYear, mCardCvv, true);
        mPaymentParametersFragment.setInformationMessage(
                getString(R.string.approve_payment_information_message));
        fragmentManager.beginTransaction()
                .replace(R.id.payment_parameters_container, mPaymentParametersFragment,
                        PaymentParametersFragment.TAG).commit();
    }

}
