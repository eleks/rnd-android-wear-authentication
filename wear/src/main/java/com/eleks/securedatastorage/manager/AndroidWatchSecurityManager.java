package com.eleks.securedatastorage.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.eleks.securedatastorage.utils.Constants;
import com.eleks.securedatastorage.utils.IOHelper;

import java.io.File;

/**
 * Created by Sergey on 22.06.2015.
 */
public class AndroidWatchSecurityManager {

    private static final String TAG = AndroidWatchSecurityManager.class.getName();
    private static String sPhoneId;
    private final SharedPreferences mSharedPreferences;
    private final File mKeyFile;

    public AndroidWatchSecurityManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        mKeyFile = new File(context.getFilesDir(), Constants.Security.KEY_FILE_NAME);
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

    public byte[] getDeviceHalfOfKey() {
        return IOHelper.loadFileSources(mKeyFile);
    }

    public void setDeviceHalfOfKey(byte[] data) {
        if (data == null) {
            if (mKeyFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                mKeyFile.delete();
            }
        } else {
            IOHelper.writeFileSources(mKeyFile, data);
        }
    }
}
