package com.eleks.securedatastorage.sdk.dialogs;

import android.content.Context;

import com.eleks.securedatastorage.securestoragesdk.R;

/**
 * Created by r.ieromchenko.
 */
public class NoWatchPasswordDialog extends PasswordDialog {

    public static NoWatchPasswordDialog getInstance() {
        return new NoWatchPasswordDialog();
    }

    @Override
    protected CharSequence getTitle(Context context) {
        return context.getString(R.string.no_connected_smart_watch_enter_master_password_text);
    }
}
