package com.example.addressbook.controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
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

    public ContactAdapter(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
        this.getEntries();
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

    }
}
