package com.example.addressbook.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.addressbook.R;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.ContactViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    public final String ENDPOINT = "/persons";

    private final RequestQueue requestQueue;
    private final ArrayList<ContactModel> items = new ArrayList<>();
    private ContactModel item;

    private final Context context;

    public ContactAdapter(Context context, RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
        this.context = context;
        this.getEntries();
    }

    public ContactAdapter(Context context) {
        this(context, Volley.newRequestQueue(context));
    }

    private void getEntries() {
        // Create the request URL
        String requestURL = AppConfig.getURL(ENDPOINT);

        // Create the request object and attach it to the listeners
        Request request = new JsonArrayRequest(
                requestURL, this::onResponse, this::onError);

        // Append the request to the queue
        this.requestQueue.add(request);
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

    private void onResponse(JSONArray data) {
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
                this.items.add(contactEntry);
            }
        }
        catch (JSONException exc) {
            // On JSON error, dispatch it to the callback
            this.onError(exc);
        }
    }

    private void onError(Exception error) {
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
            this.items.add(new ContactModel(
                    i, "first" + i, "last"+i
            ));
        }
    }
}
