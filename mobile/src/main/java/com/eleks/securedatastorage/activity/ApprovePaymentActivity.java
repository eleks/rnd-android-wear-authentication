package com.eleks.securedatastorage.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.eleks.securedatastorage.R;
import com.eleks.securedatastorage.fragment.PaymentParametersFragment;
import com.eleks.securedatastorage.model.ParameterHolder;
import com.eleks.securedatastorage.utils.Constants;

import java.util.List;


public class ApprovePaymentActivity extends ActionBarActivity {

    private String mCardNumber;
    private String mExpirationMonth;
    private String mExpirationYear;
    private String mCardCvv;

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
        initFragment();
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
        PaymentParametersFragment mPaymentParametersFragment =
                PaymentParametersFragment.getInstance(mCardNumber, mExpirationMonth,
                mExpirationYear, mCardCvv);
        fragmentManager.beginTransaction()
                .replace(R.id.payment_parameters_container, mPaymentParametersFragment,
                        PaymentParametersFragment.TAG).commit();
    }

}
