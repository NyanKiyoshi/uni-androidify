package com.example.addressbook.views.listeners;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.contactManagers.AddEditContactActivity;
import com.example.addressbook.views.contactManagers.ViewContactActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactAddEditActivityListener
        extends BaseAddEditActivityListener<ContactModel> {

    final static String ENDPOINT = "/persons";

    public ContactAddEditActivityListener(
            Context context,
            CRUDEvents<ContactModel> CRUDEventsListener, RequestQueue requestQueue) {

        super(ContactModel.class, context, CRUDEventsListener, requestQueue);
    }

    @Override
    ContactModel parseIntentResults(Intent data) {
        int id = data.getIntExtra(ViewContactActivity.EXTRA_ID, -1);
        String firstname = data.getStringExtra(ViewContactActivity.EXTRA_FIRSTNAME);
        String lastname = data.getStringExtra(ViewContactActivity.EXTRA_LASTNAME);

        return new ContactModel(id, firstname, lastname);
    }

    @Override
    void updateEntry(ContactModel newEntry) {
        // Create the request URL
        String requestURL = AppConfig.getURL(ENDPOINT + "/" + newEntry.getId());

        // Send the update request
        this.sendRequest(requestURL, Request.Method.PUT, newEntry);
    }

    @Override
    void createNewEntry(ContactModel newEntry) {
        // Create the request URL
        String requestURL = AppConfig.getURL(ENDPOINT);

        // Send the update request
        this.sendRequest(requestURL, Request.Method.POST, newEntry);
    }

    @Override
    public void startViewEntry(ContactModel item) {
        Intent intent = new Intent(this.context, ViewContactActivity.class);
        intent.putExtra(ViewContactActivity.EXTRA_ID, item.getId());
        intent.putExtra(ViewContactActivity.EXTRA_FIRSTNAME, item.getFirstName());
        intent.putExtra(ViewContactActivity.EXTRA_LASTNAME, item.getLastName());
        this.listeners.onIntentReadyToStart(intent, 0);
    }

    @Override
    public void startCreateNewEntry() {
        Intent intent = new Intent(this.context, AddEditContactActivity.class);
        this.listeners.onIntentReadyToStart(intent, CREATE_ENTRY_REQUEST);
    }

    @Override
    public void startUpdateEntry(ContactModel item) {
        Intent intent = new Intent(this.context, AddEditContactActivity.class);
        intent.putExtra(ViewContactActivity.EXTRA_ID, item.getId());
        intent.putExtra(ViewContactActivity.EXTRA_FIRSTNAME, item.getFirstName());
        intent.putExtra(ViewContactActivity.EXTRA_LASTNAME, item.getLastName());
        this.listeners.onIntentReadyToStart(intent, UPDATE_ENTRY_REQUEST);
    }
}
