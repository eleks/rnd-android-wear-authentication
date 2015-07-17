package com.eleks.securedatastorage.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.eleks.securedatastorage.R;

public class SplashActivity extends Activity {

    private static final long SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InitializeActivity.start(SplashActivity.this);
                SplashActivity.this.finish();
            }
        }, SPLASH_DELAY);
    }
}
