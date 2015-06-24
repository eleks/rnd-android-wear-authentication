package com.eleks.securedatastorage.sdk.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.eleks.securedatastorage.securestoragesdk.R;

/**
 * Created by Serhiy.Krasovskyy on 24.06.2015.
 */
public class ErrorDialog {
    private final AlertDialog.Builder mAlertDialog;
    private final Context mContext;

    public ErrorDialog(Context context, String errorMessage) {
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.app_name))
                .setMessage(errorMessage)
                .setCancelable(true)
                .setPositiveButton(context.getString(R.string.error_dialog_positive_answer),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
    }

    public void show() {
        mAlertDialog.create().show();
    }
}
