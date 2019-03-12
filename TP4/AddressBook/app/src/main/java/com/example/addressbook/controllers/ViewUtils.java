package com.example.addressbook.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.widget.ImageView;


import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.example.addressbook.R;
import com.example.addressbook.models.BaseModel;
import com.example.addressbook.views.IDeferrableActivity;
import com.example.addressbook.views.dialogs.YesNoDialog;

public class ViewUtils {
    public final static int RESULT_DELETED = 2;

    public static void PromptDelete(IDeferrableActivity deferrable, BaseModel item) {
        Activity activity = deferrable.getActivity();

        YesNoDialog.New(
            activity,
            R.string.are_you_sure,
            (dialog, which) -> {
                dialog.dismiss();
                deferrable.getLoadingBar().show();
                deferrable.getListener().startDeleteEntry(item, wrappedOnDeleted(activity));
            }
        ).show();
    }

    public static @Nullable
    SharedPreferences GetSharedPrefs(Context context) {
        if (context != null) {
            return context.getSharedPreferences("global", Context.MODE_PRIVATE);
        }
        return null;
    }

    public static void SetImage(
            ImageView imageView, @Nullable String path, @DrawableRes int placeholder) {

        if (path == null) {
            imageView.setImageResource(R.drawable.ic_menu_gallery_gray);
            return;
        }

        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    private static Response.Listener<String> wrappedOnDeleted(Activity activity) {
        return response -> {
            activity.setResult(RESULT_DELETED);
            activity.finish();
        };
    }
}
