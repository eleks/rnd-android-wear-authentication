package com.eleks.securedatastorage.sdk.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eleks.securedatastorage.securestoragesdk.R;

public class InvalidPasswordDialog extends DialogFragment {

    public static final String TAG = InvalidPasswordDialog.class.getName();
    private ViewGroup mParent;
    private View mView;
    private OnOkButtonClickListener mOkButtonListener;

    public static InvalidPasswordDialog getInstance() {
        return new InvalidPasswordDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mParent = container;
        return container;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.fragment_invalid_password_dialog, mParent);
        TextView tvTitle = (TextView) mView.findViewById(R.id.master_password_title);
        tvTitle.setText(getTitle(getActivity()));
        builder.setView(mView);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        prepareDialogControls();
        return dialog;
    }

    protected CharSequence getTitle(Context context) {
        return context.getResources().getString(R.string.invalid_password_text);
    }

    private void prepareDialogControls() {
        Button okButton = (Button) mView.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOkButtonListener != null) {
                    mOkButtonListener.onClick();
                }
            }
        });
    }

    public void setOnOkButtonClickListener(OnOkButtonClickListener listener) {
        mOkButtonListener = listener;
    }

    public interface OnOkButtonClickListener {
        void onClick();
    }

    public interface OnCancelButtonClickListener {
        void onClick();
    }
}
