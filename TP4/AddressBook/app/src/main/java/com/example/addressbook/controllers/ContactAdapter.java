package com.example.addressbook.controllers;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.addressbook.R;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.fragments.BaseRecyclerFragment;
import com.example.addressbook.views.viewholders.ContactViewHolder;

public class ContactAdapter extends BaseAdapter<ContactViewHolder, ContactModel> {
    private final BaseRecyclerFragment parent;

    public ContactAdapter(BaseRecyclerFragment parent, OnItemClickEvent<ContactModel> listener) {
        super(listener);
        this.parent = parent;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(
                R.layout.contact_entry, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        final ContactModel item = this.items.get(position);

        holder.id.setText(item.getIdStr());
        holder.firstname.setText(item.getFirstName());
        holder.lastname.setText(item.getLastName());

        item.setSharedPreferences(ViewUtils.GetSharedPrefs(this.parent.getContext()));
        ViewUtils.SetImage(
                holder.pictureBox,
                item.getPicturePath(),
                R.drawable.ic_menu_gallery_gray);
    }
}
