package com.example.addressbook.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.addressbook.R;
import com.example.addressbook.controllers.ContactAdapter;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.ContactModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactListFragment extends Fragment {
    private final String ENDPOINT = "/persons";

    private Context context;
    private RequestQueue requestQueue;
    private ContactAdapter contactAdapter;
    private SwipeRefreshLayout pullToRefresh;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        // Create a UI fragment from the layout file
        View view = inflater.inflate(
                R.layout.contact_list_fragment, container,false);

        // Get the view context
        this.context = view.getContext();

        // Create a request queue for the view
        this.requestQueue = Volley.newRequestQueue(this.context);

        // Create the view adapter
        this.contactAdapter = new ContactAdapter();

        // Listen for swipe to refresh data on demand
        this.pullToRefresh = view.findViewById(R.id.pullToRefresh);
        this.pullToRefresh.setOnRefreshListener(this::refreshEntries);

        // Set-up and bind the recycler view
        RecyclerView recyclerView = view.findViewById(R.id.contactListRecyclerView);
        recyclerView.setAdapter(this.contactAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        // Finally, get the data and return the inflated view
        this.refreshEntries();
        return view;
    }

    private void getEntries() {
        // Create the request URL
        String requestURL = AppConfig.getURL(ENDPOINT);

        // Create the request object and attach it to the listeners
        Request request = new JsonArrayRequest(
                requestURL, this::onContactListResponse, this::onError);

        // Append the request to the queue
        this.requestQueue.add(request);
    }

    private void refreshEntries() {
        this.contactAdapter.clear();
        this.pullToRefresh.setRefreshing(true);
        this.getEntries();
    }

    private void onContactListResponse(JSONArray data) {
        JSONObject contactEntryData;
        ContactModel contactEntry;

        try {
            for (int i = 0; i < data.length(); ++i) {
                // Retrieve the next contact
                contactEntryData = data.getJSONObject(i);

                // Create a new contact object from the entry data
                contactEntry = new ContactModel(
                        contactEntryData.getInt("id"),
                        contactEntryData.getString("firstname"),
                        contactEntryData.getString("lastname"));

                // Append the contact
                this.contactAdapter.addItem(contactEntry);
            }

            // Stop loading
            this.pullToRefresh.setRefreshing(false);
        }
        catch (JSONException exc) {
            // On JSON error, dispatch it to the callback
            this.onError(exc);
        }
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
            this.contactAdapter.items.add(new ContactModel(
                    i, "first" + i, "last"+i
            ));
        }
    }
}
