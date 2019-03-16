package com.example.addressbook.controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.addressbook.R;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.GroupModel;
import com.example.addressbook.views.viewholders.GroupViewHolder;

public class GroupAdapter extends BaseAdapter<GroupViewHolder, GroupModel> {
    public GroupAdapter(OnItemClickEvent<GroupModel> listener) {
        super(listener);
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(
                R.layout.group_entry, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        final GroupModel item = this.items.get(position);

        holder.id.setText(item.getIdStr());
        holder.title.setText(item.getTitle());
    }

    private static void manageAssociation(
            int requestMethod,
            RequestQueue requestQueue,
            int groupID, int contactID,
            ContactAdapter.ISuccessNoResponse callback,
            @Nullable Response.ErrorListener errorListener) {

        requestQueue.add(new StringRequest(requestMethod,
                AppConfig.getURL("/persons/" + contactID + "/groups/" + groupID),
                response -> callback.successCallback(),
                errorListener
        ));
    }

    public static void associateToContact(
            RequestQueue requestQueue,
            int groupID, int contactID,
            ContactAdapter.ISuccessNoResponse callback,
            @Nullable Response.ErrorListener errorListener) {

        manageAssociation(
                Request.Method.POST, requestQueue, groupID, contactID, callback, errorListener);
    }

    public static void deleteAssociation(
            RequestQueue requestQueue,
            int groupID, int contactID,
            ContactAdapter.ISuccessNoResponse callback,
            @Nullable Response.ErrorListener errorListener) {

        manageAssociation(
                Request.Method.DELETE, requestQueue, groupID, contactID, callback, errorListener);
    }
}
