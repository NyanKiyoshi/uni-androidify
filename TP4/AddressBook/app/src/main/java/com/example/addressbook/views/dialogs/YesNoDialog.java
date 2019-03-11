package com.example.addressbook.views.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class YesNoDialog {
    public static AlertDialog.Builder New(
            Context context,
            String title,
            final DialogInterface.OnClickListener onAcceptlistener) {

        return new AlertDialog.Builder(context)
                .setMessage(title)
                .setPositiveButton("Yes", onAcceptlistener)
                .setNegativeButton("No", YesNoDialog::onCancel);
    }

    private static void onCancel(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}
