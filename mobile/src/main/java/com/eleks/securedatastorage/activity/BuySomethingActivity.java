package com.eleks.securedatastorage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.eleks.securedatastorage.R;


public class BuySomethingActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, BuySomethingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_something);
        initControls();
    }

    private void initControls() {
    }

}
