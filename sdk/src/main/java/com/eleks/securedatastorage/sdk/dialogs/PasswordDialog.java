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
import android.widget.EditText;
import android.widget.TextView;

import com.eleks.securedatastorage.securestoragesdk.R;

import java.util.ArrayList;

/**
 * Created by Serhiy.Krasovskyy on 19.06.2015.
 */
public class PasswordDialog extends DialogFragment {

    private ViewGroup mParent;
    private View mView;
    private ArrayList<OnOkButtonClickListener> mListeners;

    public static PasswordDialog getInstance() {
        return new PasswordDialog();
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
        mView = inflater.inflate(R.layout.fragment_password_dialog, mParent);
        builder.setView(mView);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        prepareDialogControls();
        return dialog;
    }

    private void prepareDialogControls() {
        final EditText passwordEditText = (EditText) mView.findViewById(R.id.master_password);
        Button okButton = (Button) mView.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCorrectPassword(passwordEditText.getText().toString())) {
                    if (mListeners != null) {
                        for (OnOkButtonClickListener listener : mListeners) {
                            listener.onClick(passwordEditText.getText().toString());
                        }
                    }
                }
            }
        });
        TextView cancelButton = (TextView) mView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setOnOkButtonClickListener(OnOkButtonClickListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
    }

    private boolean isCorrectPassword(String password) {
        //TODO need to implement password verification method
        return true;
    }

    public interface OnOkButtonClickListener {
        void onClick(String password);
    }
}
