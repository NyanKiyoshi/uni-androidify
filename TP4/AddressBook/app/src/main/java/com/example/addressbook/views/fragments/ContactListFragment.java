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
import com.example.addressbook.controllers.ContactAdapter;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.listeners.BaseAddEditActivityListener;
import com.example.addressbook.views.listeners.ContactAddEditActivityListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactListFragment
        extends BaseRecyclerFragment
        implements BaseAddEditActivityListener.CRUDEvents<ContactModel> {

    private final String ENDPOINT = "/persons";
    private ContactAdapter contactAdapter;
    private ContactAddEditActivityListener activityListener;

    public ContactListFragment() {
        // Create the view adapter
        this.contactAdapter = new ContactAdapter(
                (item, pos) -> this.activityListener.startViewEntry(item));
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
        recyclerView.setAdapter(this.contactAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        // Create the activity's listener
        this.activityListener = new ContactAddEditActivityListener(
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
                requestURL, this::onContactListResponse, this::onError);

        // Append the request to the queue
        this.requestQueue.add(request);
    }

    @Override
    void refreshEntries() {
        this.contactAdapter.clear();
        this.pullToRefresh.setRefreshing(true);
        this.getEntries();
    }

    private void onContactListResponse(JSONArray data) {
        JSONObject contactEntryData;

        int dataLength = data.length();

        if (dataLength < 1) {
            return;
        }

        // This will contains the parsed entries
        ContactModel[] contactEntries = new ContactModel[dataLength];

        try {
            for (int i = 0; i < data.length(); ++i) {
                // Retrieve the next contact
                // and create a new contact object from it
                contactEntries[i] = new ContactModel().fromJSON(data.getJSONObject(i));
            }
        }
        catch (JSONException exc) {
            // On JSON error, dispatch it to the callback
            this.onError(exc);
            return;
        }

        // Append the contacts
        this.contactAdapter.addItems(contactEntries);

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
    public void onEntryUpdated(ContactModel newItem) {
        this.loadingBar.hide();
        this.contactAdapter.addItem(newItem);
        Toast.makeText(this.context, R.string.contact_created, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEntryFailedUpdating() {
        Toast.makeText(this.context, R.string.failed_to_create, Toast.LENGTH_SHORT).show();
        this.loadingBar.hide();
    }

    @Override
    public void onEntryStartUpdating(ContactModel newItem) {
        this.loadingBar.show();
    }
}
