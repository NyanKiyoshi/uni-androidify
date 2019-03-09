package com.example.addressbook.controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.addressbook.R;
import com.example.addressbook.models.GroupModel;
import com.example.addressbook.views.viewholders.GroupViewHolder;

public class GroupAdapter extends BaseAdapter<GroupViewHolder, GroupModel> {
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
        final GroupModel item = this.items.get(position);

        holder.id.setText(item.getIdStr());
        holder.title.setText(item.getTitle());
    }
}
