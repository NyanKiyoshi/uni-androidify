package com.example.TPNotemkocak.controllers.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.TPNotemkocak.R;
import com.example.TPNotemkocak.controllers.ViewUtils;
import com.example.TPNotemkocak.models.ContactModel;
import com.example.TPNotemkocak.views.viewholders.ContactViewHolder;

public class RemovableContactAdapter
        extends ContactAdapter
        implements ViewUtils.IRemoveClickListener<ContactModel> {

    private ViewUtils.IOnClickEvent<ContactModel> removeClickListener;

    public RemovableContactAdapter(IHasContext parent, OnItemClickEvent<ContactModel> listener) {
        super(parent, listener);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return this.onCreateViewHolder(parent, viewType, R.layout.contact_removable_entry);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.removeBtn.setOnClickListener(ViewUtils.wrapRecyclerRemoveItem(this, holder));
    }

    public void setRemoveClickListener(ViewUtils.IOnClickEvent<ContactModel> removeClickListener) {
        this.removeClickListener = removeClickListener;
    }

    @Override
    public ViewUtils.IOnClickEvent<ContactModel> getRemoveCallback() {
        return this.removeClickListener;
    }

    @Override
    public ContactModel getItem(int pos) {
        return this.items.get(pos);
    }
}
