package com.eleks.securedatastorage.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.eleks.securedatastorage.utils.Constants;
import com.eleks.securedatastorage.utils.IoHelper;

import java.io.File;

/**
 * Created by Sergey on 22.06.2015.
 */
public class SecurityManager {

    private static final String TAG = SecurityManager.class.getName();
    private static String sPhoneId;
    private final SharedPreferences mSharedPreferences;
    private final File mKeyFile;
    private Context mContext;

    public SecurityManager(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        mKeyFile = new File(mContext.getFilesDir(), Constants.Security.KEY_FILE_NAME);
    }

    public String getPhoneId() {
        if (TextUtils.isEmpty(sPhoneId)) {
            sPhoneId = mSharedPreferences.getString(Constants.Extras.PHONE_ID, null);
        }
        return sPhoneId;
    }

    public void setPhoneId(String phoneId) {
        sPhoneId = phoneId;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.Extras.PHONE_ID, phoneId);
        editor.apply();
    }

    public void setDeviceHalfOfKey(byte[] data) {
        IoHelper.writeFileSources(mKeyFile, data);
    }

    public byte[] getDeviceHalfOfKey(){
        return  IoHelper.loadFileSources(mKeyFile);
    }
}
