package com.eleks.securedatastorage.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.eleks.securedatastorage.R;
import com.eleks.securedatastorage.utils.Constants;

/**
 * Created by Serhiy.Krasovskyy on 30.06.2015.
 */
public class PaymentParametersFragment extends Fragment {

    public static final String TAG = PaymentParametersFragment.class.getName();
    private View mFragmentView;
    private EditText mCardNumber;
    private EditText mExpirationMonth;
    private EditText mExpirationYear;
    private EditText mCardCvv;
    private String mCardNumberValue;
    private String mExpirationMonthValue;
    private String mExpirationYearValue;
    private String mCardCvvValue;
    private boolean mIsReadOnlyFields = false;

    public static PaymentParametersFragment getInstance(String cardNumber, String expirationMonth,
                                                        String expirationYear, String cardCvv,
                                                        boolean isReadOnlyFields) {
        PaymentParametersFragment fragment = new PaymentParametersFragment();
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(cardNumber)) {
            bundle.putString(Constants.PaymentParameters.CARD_NUMBER, cardNumber);
        }
        if (!TextUtils.isEmpty(expirationMonth)) {
            bundle.putString(Constants.PaymentParameters.EXPIRATION_MONTH, expirationMonth);
        }
        if (!TextUtils.isEmpty(expirationYear)) {
            bundle.putString(Constants.PaymentParameters.EXPIRATION_YEAR, expirationYear);
        }
        if (!TextUtils.isEmpty(cardCvv)) {
            bundle.putString(Constants.PaymentParameters.CARD_CVV, cardCvv);
        }
        bundle.putBoolean(Constants.Extras.IS_READONLY_FIELDS, isReadOnlyFields);
        fragment.setArguments(bundle);
        return fragment;
    }

    public String getCardNumber() {
        return mCardNumber.getText().toString();
    }

    public String getExpirationMonth() {
        return mExpirationMonth.getText().toString();
    }

    public String getExpirationYear() {
        return mExpirationYear.getText().toString();
    }

    public String getCardCvv() {
        return mCardCvv.getText().toString();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_payment_parameters, container, false);
        getExtras();
        initFragmentControls();
        fillFragmentControls();
        return mFragmentView;
    }

    private void fillFragmentControls() {
       setEditTextValue(mCardNumber, mCardNumberValue);
        setEditTextValue(mExpirationMonth, mExpirationMonthValue);
        setEditTextValue(mExpirationYear, mExpirationYearValue);
        setEditTextValue(mCardCvv, mCardCvvValue);
    }

    private void setEditTextValue(EditText editText, String value) {
        if (!TextUtils.isEmpty(value)) {
            editText.setText(value);
            if (mIsReadOnlyFields) {
                editText.setEnabled(false);
                editText.setClickable(false);
            }
        }
    }

    private void getExtras() {
        if (getArguments() != null) {
            mCardNumberValue = getArguments()
                    .getString(Constants.PaymentParameters.CARD_NUMBER, null);
            mExpirationMonthValue = getArguments()
                    .getString(Constants.PaymentParameters.EXPIRATION_MONTH, null);
            mExpirationYearValue = getArguments()
                    .getString(Constants.PaymentParameters.EXPIRATION_YEAR, null);
            mCardCvvValue = getArguments().getString(Constants.PaymentParameters.CARD_CVV, null);
            mIsReadOnlyFields = getArguments().getBoolean(Constants.Extras.IS_READONLY_FIELDS, false);
        }
    }

    private void initFragmentControls() {
        mCardNumber = (EditText) mFragmentView.findViewById(R.id.card_number);
        mExpirationMonth = (EditText) mFragmentView.findViewById(R.id.expiration_month);
        mExpirationYear = (EditText) mFragmentView.findViewById(R.id.expiration_year);
        mCardCvv = (EditText) mFragmentView.findViewById(R.id.card_cvv);
    }
}
