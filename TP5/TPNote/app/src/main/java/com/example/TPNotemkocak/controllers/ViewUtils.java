package com.example.TPNotemkocak.controllers;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TPNotemkocak.R;
import com.example.TPNotemkocak.models.BaseModel;
import com.example.TPNotemkocak.views.dialogs.YesNoDialog;

public class ViewUtils {
    public static final String EXTRA_ID =
            ".ViewUtils.EXTRA_ID";
    public final static int RESULT_DELETED = 404;

    public interface IOnClickEvent<Model extends BaseModel> {
        void onRemoveClick(Model item, int pos);
    }

    public interface IRemoveClickListener<Model extends BaseModel> {
        IOnClickEvent<Model> getRemoveCallback();
        Model getItem(int pos);
    }

    public static void PromptDelete(AppCompatActivity activity, BaseModel item) {
        YesNoDialog.New(
                activity,
                R.string.are_you_sure,
                (dialog, which) -> {
                    // Close the dialog
                    dialog.dismiss();

                    Intent data = new Intent();
                    data.putExtra(ViewUtils.EXTRA_ID, item.getId());

                    // Close the activity, and dispatch the deletion event
                    activity.setResult(RESULT_DELETED, data);
                    activity.finish();
                }
        ).show();
    }

    public static <M extends BaseModel>View.OnClickListener wrapRecyclerRemoveItem(
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
