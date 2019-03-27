package com.example.TPNotemkocak.controllers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.TPNotemkocak.R;
import com.example.TPNotemkocak.models.GroupModel;
import com.example.TPNotemkocak.views.viewholders.GroupViewHolder;

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
        holder.title.setText(this.items.get(position).getTitle());
    }
}
