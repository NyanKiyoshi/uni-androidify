package com.example.addressbook.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.addressbook.R;
import com.example.addressbook.controllers.GroupAdapter;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.GroupModel;
import com.example.addressbook.views.listeners.BaseAddEditActivityListener;
import com.example.addressbook.views.listeners.GroupAddEditActivityListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GroupListFragment
        extends BaseRecyclerFragment
        implements BaseAddEditActivityListener.CRUDEvents<GroupModel> {

    private final String ENDPOINT = "/groups";
    private GroupAdapter adapter;
    private BaseAddEditActivityListener<GroupModel> activityListener;

    public GroupListFragment() {
        // Create the view adapter
        this.adapter = new GroupAdapter(
                (item, pos) -> this.activityListener.startViewEntry(item)
        );
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Set-up and bind the recycler view
        RecyclerView recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

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

    void getEntries() {
        // Create the request URL
        String requestURL = AppConfig.getURL(ENDPOINT);

        // Create the request object and attach it to the listeners
        Request request = new JsonArrayRequest(
                requestURL, this::onListResponse, this::onError);

        // Append the request to the queue
        this.requestQueue.add(request);
    }

    @Override
    void refreshEntries() {
        this.adapter.clear();
        this.pullToRefresh.setRefreshing(true);
        this.getEntries();
    }

    private void onListResponse(JSONArray data) {
        JSONObject entryData;

        int dataLength = data.length();

        if (dataLength < 1) {
            return;
        }

        // This will contains the parsed entries
        GroupModel[] entries = new GroupModel[dataLength];

        try {
            for (int i = 0; i < data.length(); ++i) {
                // Retrieve the next group data
                // and create a new group object from it.
                entries[i] = new GroupModel().fromJSON(data.getJSONObject(i));
            }
        }
        catch (JSONException exc) {
            // On JSON error, dispatch it to the callback
            this.onError(exc);
            return;
        }

        // Append the entries
        this.adapter.addItems(entries);

        // Stop loading
        this.pullToRefresh.setRefreshing(false);
    }

    private void onError(Exception error) {
        // Stop loading
        this.pullToRefresh.setRefreshing(false);

        // Log the full error
        Log.e(this.getClass().getName(),
                "got an error while getting entries", error);

        // Show a simple error to the user
        if (this.context != null) {
            Toast.makeText(
                    this.context,
                    "Failed to get entries.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.activityListener.onActivityResult(requestCode, resultCode, data);
        this.refreshEntries();
    }

    @Override
    public void onIntentReadyToStart(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onEntryUpdated(GroupModel newItem) {
        this.loadingBar.hide();
        Toast.makeText(this.context, R.string.group_created, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEntryFailedUpdating() {
        Toast.makeText(this.context, R.string.failed_to_create, Toast.LENGTH_SHORT).show();
        this.loadingBar.hide();
    }

    @Override
    public void onEntryStartUpdating(GroupModel newItem) {
        this.loadingBar.show();
    }
}
