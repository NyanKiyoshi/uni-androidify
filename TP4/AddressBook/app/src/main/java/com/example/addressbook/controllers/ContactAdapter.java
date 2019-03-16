package com.example.addressbook.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.example.addressbook.R;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.viewholders.ContactViewHolder;

public class ContactAdapter extends BaseAdapter<ContactViewHolder, ContactModel> {
    public interface IHasContext {
        Context getContext();
    }

    private final IHasContext parent;

    public ContactAdapter(IHasContext parent, OnItemClickEvent<ContactModel> listener) {
        super(listener);
        this.parent = parent;
    }

    @NonNull
    public ContactViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType, @LayoutRes int layoutID) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(layoutID, parent, false);
        return new ContactViewHolder(v);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return this.onCreateViewHolder(parent, viewType, R.layout.contact_entry);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        final ContactModel item = this.items.get(position);

        if (holder.id != null) {
            holder.id.setText(item.getIdStr());
        }
        holder.firstname.setText(item.getFirstName());
        holder.lastname.setText(item.getLastName());

        item.setSharedPreferences(ViewUtils.GetSharedPrefs(this.parent.getContext()));
        ViewUtils.SetImage(
                holder.pictureBox,
                item.getPicturePath(),
                R.drawable.ic_menu_gallery_gray);
    }
}
