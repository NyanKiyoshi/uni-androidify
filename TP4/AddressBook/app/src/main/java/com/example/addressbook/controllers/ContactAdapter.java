package com.example.addressbook.controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.R;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.ContactViewHolder;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    public final ArrayList<ContactModel> items = new ArrayList<>();

    public ContactAdapter() {
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
        final ContactModel item = this.items.get(position);

        holder.id.setText(item.getIdStr());
        holder.firstname.setText(item.getFirstName());
        holder.lastname.setText(item.getLastName());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void addItem(ContactModel contact) {
        this.items.add(contact);
        this.notifyItemInserted(this.items.size() - 1);
    }

    public void clear() {
        if (this.items.isEmpty()) {
            return;
        }

        int removedCount = this.items.size();
        this.items.clear();
        this.notifyItemRangeRemoved(0, removedCount);
    }
}
