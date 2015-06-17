package com.eleks.securedatastorage.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.eleks.securedatastorage.R;
import com.eleks.securedatastorage.sdk.storage.SecuredStorage;
import com.eleks.securedatastorage.utils.Constants;


public class MainActivity extends ActionBarActivity {

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
    }

    private void initControls() {
        final SecuredStorage securedStorage = new SecuredStorage(MainActivity.this);
        mUserNameEditText = (EditText) findViewById(R.id.user_name);
        String userName = securedStorage.getString(Constants.Extras.USER_NAME_ENTITY, null);
        if (!TextUtils.isEmpty(userName)) {
            mUserNameEditText.setText(userName);
        }
        mPasswordEditText = (EditText) findViewById(R.id.password);
        String password = securedStorage.getString(Constants.Extras.PASSWORD_ENTITY, null);
        if (!TextUtils.isEmpty(password)) {
            mPasswordEditText.setText(password);
        }
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mUserNameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if (isUserNameValid(userName) && isPasswordValid(password)) {
                    securedStorage.setString(Constants.Extras.USER_NAME_ENTITY, userName);
                    securedStorage.setString(Constants.Extras.PASSWORD_ENTITY, password);
                }
            }
        });
    }

    private boolean isPasswordValid(String password) {
        //TODO Need to implement
        return true;
    }

    private boolean isUserNameValid(String userName) {
        //TODO Need to implement
        return true;
    }

}
