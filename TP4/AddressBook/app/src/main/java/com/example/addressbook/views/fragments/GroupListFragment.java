package com.example.addressbook.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.addressbook.R;
import com.example.addressbook.controllers.GroupAdapter;
import com.example.addressbook.models.GroupModel;
import com.example.addressbook.views.listeners.GroupAddEditActivityListener;
import com.example.addressbook.views.viewholders.GroupViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupListFragment extends BaseRecyclerFragment<GroupModel, GroupViewHolder> {

    public GroupListFragment() {
        super(GroupModel.class);

        // Create the view adapter
        this.adapter = new GroupAdapter(
                (item, pos) -> this.activityListener.startViewEntry(item));
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Create the activity's listener
        this.activityListener = new GroupAddEditActivityListener(
                this.context, this, this.requestQueue);

        // Register an handler to the floating button
        final FloatingActionButton fab = view.findViewById(R.id.create_fab);
        fab.setOnClickListener((v) -> this.activityListener.startCreateNewEntry());

        // Finally, get the data and return the inflated view
        this.refreshEntries();

        return view;
    }

    @Override
    String getEndpoint() {
        return "/groups";
    }
}
