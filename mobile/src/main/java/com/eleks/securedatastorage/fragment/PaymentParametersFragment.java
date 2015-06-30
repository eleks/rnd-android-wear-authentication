package com.eleks.securedatastorage.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.eleks.securedatastorage.R;

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

    public static PaymentParametersFragment getInstance() {
        return new PaymentParametersFragment();
    }

    public String getCardNumber() {
        return mCardNumber.getText().toString();
    }

    public void setCardNumber(String cardNumber) {
        this.mCardNumber.setText(cardNumber);
    }

    public String getExpirationMonth() {
        return mExpirationMonth.getText().toString();
    }

    public void setExpirationMonth(String expirationMonth) {
        this.mExpirationMonth.setText(expirationMonth);
    }

    public String getExpirationYear() {
        return mExpirationYear.getText().toString();
    }

    public void setExpirationYear(String expirationYear) {
        this.mExpirationYear.setText(expirationYear);
    }

    public String getCardCvv() {
        return mCardCvv.getText().toString();
    }

    public void setCardCvv(String cardCvv) {
        this.mCardCvv.setText(cardCvv);
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
        initFragmentControls();
        return mFragmentView;
    }

    private void initFragmentControls() {
        mCardNumber = (EditText) mFragmentView.findViewById(R.id.card_number);
        mExpirationMonth = (EditText) mFragmentView.findViewById(R.id.expiration_month);
        mExpirationYear = (EditText) mFragmentView.findViewById(R.id.expiration_year);
        mCardCvv = (EditText) mFragmentView.findViewById(R.id.card_cvv);
    }
}
