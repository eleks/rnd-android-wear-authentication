package com.eleks.securedatastorage.sdk.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.eleks.securedatastorage.securestoragesdk.R;

public class InvalidPasswordDialog extends DialogFragment {
    private View.OnClickListener mOnClickListener;

    public static InvalidPasswordDialog getInstance() {
        return new InvalidPasswordDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_invalid_password_dialog, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.master_password_title);
        view.findViewById(R.id.ok_button).setOnClickListener(mOnClickListener);
        tvTitle.setText(getActivity().getString(R.string.invalid_password_text));
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public void setOnOkButtonClickListener(final View.OnClickListener listener) {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        };
    }

}
