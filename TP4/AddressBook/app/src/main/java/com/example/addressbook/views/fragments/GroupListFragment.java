package com.example.addressbook.views.fragments;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GroupListFragment extends BaseRecyclerFragment {
    private final String ENDPOINT = "/groups";
    private GroupAdapter adapter;

    public GroupListFragment() {
        // Create the view adapter
        this.adapter = new GroupAdapter(this::onItemClick);
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
                // Retrieve the next group
                entryData = data.getJSONObject(i);

                // Create a new group object from the entry data
                entries[i] = new GroupModel(
                        entryData.getInt("id"),
                        entryData.getString("title"));
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

        // If the app is not in testing mode, stop here.
        if (!AppConfig.IS_TESTING) {
            return;
        }

        // Otherwise, if the app is in testing mode, create dummy data.
        for (int i = 0; i < 15; ++i) {
            this.adapter.items.add(new GroupModel(
                    i, "Group" + i
            ));
        }
    }

    private void onItemClick(GroupModel item, int position) {
        // TODO: implement me
    }
}
