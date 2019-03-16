package com.example.addressbook.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.addressbook.R;
import com.example.addressbook.controllers.ContactAdapter;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.listeners.ContactAddEditActivityListener;
import com.example.addressbook.views.viewholders.ContactViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContactListFragment
        extends BaseRecyclerFragment<ContactModel, ContactViewHolder>
        implements ContactAdapter.IHasContext {

    public ContactListFragment() {
        super(ContactModel.class);

        // Create the view adapter
        this.adapter = new ContactAdapter(
                this,
                (item, pos) -> this.activityListener.startUpdateEntry(item));
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Create the activity's listener
        this.activityListener = new ContactAddEditActivityListener(
                this.context, this, this.requestQueue);

        // Register an handler to the floating button
        final FloatingActionButton fab = view.findViewById(R.id.create_fab);
        fab.setOnClickListener((v) -> this.activityListener.startCreateNewEntry());

        // Finally, get the data and return the inflated view
        this.refreshData();

        return view;
    }

    @Override
    String getEndpoint() {
        return "/persons";
    }
}
