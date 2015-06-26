package com.eleks.securedatastorage.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

import com.eleks.securedatastorage.R;

/**
 * Created by Serhiy.Krasovskyy on 26.06.2015.
 */
public class InitializeActivity extends ActionBarActivity {

    private EditText mCardNumber;
    private EditText mExpirationMonth;
    private EditText mExpirationYear;
    private EditText mCardCvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);
        initControls();
    }

    private void initControls() {
        mCardNumber=(EditText) findViewById(R.id.card_number);
        mExpirationMonth=(EditText) findViewById(R.id.expiration_month);
        mExpirationYear=(EditText) findViewById(R.id.expiration_year);
        mCardCvv=(EditText)findViewById(R.id.card_cvv);
    }
}
