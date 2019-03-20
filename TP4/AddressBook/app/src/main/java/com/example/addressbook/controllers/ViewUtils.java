package com.example.addressbook.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;


import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.example.addressbook.R;
import com.example.addressbook.controllers.adapters.BaseAdapter;
import com.example.addressbook.models.BaseModel;
import com.example.addressbook.models.IStringSerializable;
import com.example.addressbook.views.IDeferrableActivity;
import com.example.addressbook.views.dialogs.YesNoDialog;

public class ViewUtils {
    public final static int RESULT_DELETED = 2;

    public interface IOnClickEvent<Model extends IStringSerializable> {
        void onRemoveClick(Model item, int pos);
    }

    public interface IRemoveClickListener<Model extends IStringSerializable> {
        IOnClickEvent<Model> getRemoveCallback();
        Model getItem(int pos);
    }

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

    private static Response.Listener<String> wrappedOnDeleted(Activity activity) {
        return response -> {
            activity.setResult(RESULT_DELETED);
            activity.finish();
        };
    }

    public static <M extends IStringSerializable>View.OnClickListener wrapRecyclerRemoveItem(
            IRemoveClickListener<M> adapter, RecyclerView.ViewHolder holder) {

        return (v) -> {
            int pos = holder.getAdapterPosition();
            IOnClickEvent<M> listener = adapter.getRemoveCallback();

            if (pos == RecyclerView.NO_POSITION || listener == null) {
                return;
            }

            listener.onRemoveClick(adapter.getItem(pos), pos);
        };
    }
}
