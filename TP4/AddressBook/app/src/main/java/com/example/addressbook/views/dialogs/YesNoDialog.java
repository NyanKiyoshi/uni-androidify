package com.example.addressbook.views.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.StringRes;

import com.example.addressbook.R;

public class YesNoDialog {
    public static AlertDialog.Builder New(
            Context context,
            @StringRes int title,
            final DialogInterface.OnClickListener onAcceptlistener) {

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(R.string.yes, onAcceptlistener)
                .setNegativeButton(R.string.no, YesNoDialog::onCancel);
    }

    private static void onCancel(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}
