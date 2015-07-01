package com.eleks.securedatastorage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.eleks.securedatastorage.R;

public class CongratulationsActivity extends ActionBarActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, CongratulationsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);
    }

}
