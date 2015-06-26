package com.eleks.securedatastorage.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.eleks.securedatastorage.securestoragesdk.R;

/**
 * Created by Serhiy.Krasovskyy on 18.06.2015.
 */
public class AskUserDialog {

    private final AlertDialog.Builder mAlertDialog;
    private final Context mContext;

    public AskUserDialog(Context context, String message) {
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.app_name))
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(context.getString(R.string.ask_user_dialog_negative_answer),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
    }

    public void show(DialogInterface.OnClickListener positiveButtonOnClickListener) {
        mAlertDialog.setPositiveButton(mContext.getString(R.string.ask_user_dialog_positive_answer),
                positiveButtonOnClickListener)
                .create()
                .show();
    }
}
