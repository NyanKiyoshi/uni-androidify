package com.example.addressbook.views.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.viewholders.ContactViewHolder;

public class ContactSelectionFragment
        extends BaseRecyclerFragment<ContactModel, ContactViewHolder> {

    ContactSelectionFragment(Class<ContactModel> contactModelClass) {
        super(contactModelClass);
    }

    @Override
    String getEndpoint() {
        return null;
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        this.fab.setVisibility(View.GONE);

        return view;
    }
}
