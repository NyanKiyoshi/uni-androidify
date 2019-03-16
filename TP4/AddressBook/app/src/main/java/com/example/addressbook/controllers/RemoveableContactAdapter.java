package com.example.addressbook.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.R;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.viewholders.ContactViewHolder;

public class RemoveableContactAdapter extends ContactAdapter {
    public interface IOnClickEvent {
        void onRemoveClick(ContactModel item, int pos);
    }

    private RecyclerView.ViewHolder holder;
    private IOnClickEvent removeClickListener;

    public RemoveableContactAdapter(IHasContext parent, OnItemClickEvent<ContactModel> listener) {
        super(parent, listener);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return this.onCreateViewHolder(parent, viewType, R.layout.contact_removeable_entry);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        this.holder = holder;

        holder.removeBtn.setOnClickListener(this::onRemoveClick);
    }

    private void onRemoveClick(View view) {
        int pos = this.holder.getAdapterPosition();

        if (pos == RecyclerView.NO_POSITION || this.removeClickListener == null) {
            return;
        }

        this.removeClickListener.onRemoveClick(this.items.get(pos), pos);
    }

    public void setRemoveClickListener(IOnClickEvent removeClickListener) {
        this.removeClickListener = removeClickListener;
    }
}
