package com.example.addressbook.views.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.addressbook.controllers.ViewUtils;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.contactManagers.AddEditContactActivity;
import com.example.addressbook.views.contactManagers.BaseContactActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactAddEditActivityListener
        extends BaseAddEditActivityListener<ContactModel> {

    public ContactAddEditActivityListener(
            Context context,
            CRUDEvents<ContactModel> CRUDEventsListener, RequestQueue requestQueue) {

        super(ContactModel.class, context, CRUDEventsListener, requestQueue);
    }

    @Override
    String getEndpoint() {
        return "/persons";
    }

    private void populateIntent(Intent intent, ContactModel item) {
        intent.putExtra(BaseContactActivity.EXTRA_ID, item.getId());
        intent.putExtra(BaseContactActivity.EXTRA_FIRSTNAME, item.getFirstName());
        intent.putExtra(BaseContactActivity.EXTRA_LASTNAME, item.getLastName());
    }

    @Override
    ContactModel parseIntentResults(Intent data) {
        int id = data.getIntExtra(BaseContactActivity.EXTRA_ID, -1);
        String firstname = data.getStringExtra(BaseContactActivity.EXTRA_FIRSTNAME);
        String lastname = data.getStringExtra(BaseContactActivity.EXTRA_LASTNAME);
        String picture = data.getStringExtra(BaseContactActivity.EXTRA_FILE_ABS_PATH);

        ContactModel newEntry = new ContactModel(id, firstname, lastname);
        newEntry.setSharedPreferences(ViewUtils.GetSharedPrefs(this.context));
        newEntry.setPicturePath(picture);

        return newEntry;
    }

    @Override
    void updateEntry(ContactModel newEntry, @Nullable Response.Listener<JSONObject> callback) {
        super.updateEntry(newEntry, response -> newEntry.save());
    }

    @Override
    void createNewEntry(ContactModel newEntry, @Nullable Response.Listener<JSONObject> callback) {
        super.createNewEntry(newEntry, response -> {
            // Retrieve the created ID and set it to the object
            try {
                newEntry.setID(response.getInt("id"));
            } catch (JSONException exc) {
                Log.wtf("Failed to get ID of contact", exc);
            }

            // Save the entry
            newEntry.save();
        });
    }

    @Override
    public void startViewEntry(ContactModel item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void startCreateNewEntry() {
        Intent intent = new Intent(this.context, AddEditContactActivity.class);
        this.listeners.onIntentReadyToStart(intent, CREATE_ENTRY_REQUEST);
    }

    @Override
    public void startUpdateEntry(ContactModel item) {
        Intent intent = new Intent(this.context, AddEditContactActivity.class);
        this.populateIntent(intent, item);
        this.listeners.onIntentReadyToStart(intent, UPDATE_ENTRY_REQUEST);
    }
}
