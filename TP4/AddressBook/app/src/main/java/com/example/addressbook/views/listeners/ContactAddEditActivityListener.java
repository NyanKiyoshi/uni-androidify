package com.example.addressbook.views.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.addressbook.controllers.GroupAssociations;
import com.example.addressbook.controllers.ViewUtils;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.contactManagers.AddEditContactActivity;
import com.example.addressbook.views.contactManagers.BaseContactActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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
        item.setSharedPreferences(ViewUtils.GetSharedPrefs(this.context));

        intent.putExtra(BaseContactActivity.EXTRA_ID, item.getId());
        intent.putExtra(BaseContactActivity.EXTRA_FIRSTNAME, item.getFirstName());
        intent.putExtra(BaseContactActivity.EXTRA_LASTNAME, item.getLastName());
        intent.putExtra(BaseContactActivity.EXTRA_FILE_ABS_PATH, item.getPicturePath());
    }

    private void commitGroups(@NonNull ContactModel entry) {

        for (Integer newGroupID : entry.newGroups){
            GroupAssociations.associateToContact(
                    this.requestQueue, newGroupID, entry.getId(), () -> {}, null);
        }

        for (Integer remGroupID : entry.removedGroups){
            GroupAssociations.deleteAssociation(
                    this.requestQueue, remGroupID, entry.getId(), () -> {}, null);
        }
    }

    @Override
    ContactModel parseIntentResults(Intent data) {
        // Main data
        int id = data.getIntExtra(BaseContactActivity.EXTRA_ID, -1);
        String firstname = data.getStringExtra(BaseContactActivity.EXTRA_FIRSTNAME);
        String lastname = data.getStringExtra(BaseContactActivity.EXTRA_LASTNAME);
        String picture = data.getStringExtra(BaseContactActivity.EXTRA_FILE_ABS_PATH);
        boolean isDeletedPicture = data.getBooleanExtra(
                BaseContactActivity.EXTRA_IS_PICTURE_DELETED, false);

        ContactModel newEntry = new ContactModel(id, firstname, lastname);

        // Picture
        newEntry.setSharedPreferences(ViewUtils.GetSharedPrefs(this.context));
        if (isDeletedPicture) {
            String oldPicturePath = newEntry.getPicturePath();

            if (oldPicturePath != null) {
                File oldPicture = new File(oldPicturePath);
                oldPicture.delete();
            }
        }
        newEntry.setPicturePath(picture);

        // Groups
        newEntry.newGroups =
                data.getIntegerArrayListExtra(GroupAssociations.EXTRA_GROUPS_TO_ADD);
        newEntry.removedGroups =
                data.getIntegerArrayListExtra(GroupAssociations.EXTRA_GROUPS_TO_REMOVE);

        return newEntry;
    }

    @Override
    void updateEntry(ContactModel newEntry, @Nullable Response.Listener<JSONObject> callback) {
        super.updateEntry(newEntry, response -> {
            // Commit groups
            this.commitGroups(newEntry);

            newEntry.save();
        });
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

            // Commit groups
            this.commitGroups(newEntry);

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
