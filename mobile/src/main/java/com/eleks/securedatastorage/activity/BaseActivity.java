package com.eleks.securedatastorage.activity;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;

import com.eleks.securedatastorage.R;

/**
 * Created by Serhiy.Krasovskyy on 30.06.2015.
 */
public class BaseActivity extends ActionBarActivity {

    private ProgressDialog mProgressDialog;

    protected void showProgressDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = ProgressDialog.show(BaseActivity.this,
                        BaseActivity.this.getString(R.string.app_name),
                        message);
            }
        });
    }

    protected void dismissProgressDialog() {
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
}
