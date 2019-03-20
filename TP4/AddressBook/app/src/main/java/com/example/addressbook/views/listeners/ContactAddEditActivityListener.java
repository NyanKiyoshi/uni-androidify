package com.example.addressbook.views.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.addressbook.controllers.ContactAssociations;
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

    private void commit(@NonNull ContactModel entry) {
        this.commitGroups(entry);
        this.commitPostals(entry);
        this.commitPhone(entry);
        this.commitMails(entry);
    }

    private void commitGroups(@NonNull ContactModel entry) {

        if (entry.newGroups != null) {
            for (Integer newGroupID : entry.newGroups) {
                GroupAssociations.associateToContact(
                        this.requestQueue, newGroupID, entry.getId(), () -> {}, null);
            }
        }

        if (entry.removedGroups != null) {
            for (Integer remGroupID : entry.removedGroups) {
                GroupAssociations.deleteAssociation(
                        this.requestQueue, remGroupID, entry.getId(), () -> {}, null);
            }
        }
    }

    private void commitPostals(@NonNull ContactModel entry) {

        if (entry.newPostalPayloads != null) {
            for (String payload : entry.newPostalPayloads) {
                ContactAssociations.createPostal(
                        this.requestQueue, payload, entry.getId(), null, null);
            }
        }

        if (entry.removedPostalsIDs != null) {
            for (Integer remGroupID : entry.removedPostalsIDs) {
                ContactAssociations.deletePostal(
                        this.requestQueue, remGroupID, entry.getId(), null, null);
            }
        }
    }

    private void commitPhone(@NonNull ContactModel entry) {

        if (entry.newPhonesPayloads != null) {
            for (String payload : entry.newPhonesPayloads) {
                ContactAssociations.createPhone(
                        this.requestQueue, payload, entry.getId(), null, null);
            }
        }

        if (entry.removedPhonesIDs != null) {
            for (Integer remGroupID : entry.removedPhonesIDs) {
                ContactAssociations.deletePhone(
                        this.requestQueue, remGroupID, entry.getId(), null, null);
            }
        }
    }

    private void commitMails(@NonNull ContactModel entry) {

        if (entry.newMailsPayloads != null) {
            for (String payload : entry.newMailsPayloads) {
                ContactAssociations.createMail(
                        this.requestQueue, payload, entry.getId(), null, null);
            }
        }

        if (entry.removedMailIDs != null) {
            for (Integer remGroupID : entry.removedMailIDs) {
                ContactAssociations.deleteMail(
                        this.requestQueue, remGroupID, entry.getId(), null, null);
            }
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
            newEntry.setPicturePath(null);
        }

        if (picture != null) {
            newEntry.setPicturePath(picture);
        }

        // Groups
        newEntry.newGroups =
                data.getIntegerArrayListExtra(GroupAssociations.EXTRA_GROUPS_TO_ADD);
        newEntry.removedGroups =
                data.getIntegerArrayListExtra(GroupAssociations.EXTRA_GROUPS_TO_REMOVE);

        // Postals
        newEntry.newPostalPayloads =
                data.getStringArrayListExtra(ContactAssociations.EXTRA_POSTAL_TO_ADD);
        newEntry.removedPostalsIDs =
                data.getIntegerArrayListExtra(ContactAssociations.EXTRA_POSTAL_TO_REMOVE);

        // Phones
        newEntry.newPhonesPayloads =
                data.getStringArrayListExtra(ContactAssociations.EXTRA_NUMBER_TO_ADD);
        newEntry.removedPhonesIDs =
                data.getIntegerArrayListExtra(ContactAssociations.EXTRA_NUMBER_TO_REMOVE);

        // Mails
        newEntry.newMailsPayloads =
                data.getStringArrayListExtra(ContactAssociations.EXTRA_MAIL_TO_ADD);
        newEntry.removedMailIDs =
                data.getIntegerArrayListExtra(ContactAssociations.EXTRA_MAIL_TO_REMOVE);

        return newEntry;
    }

    @Override
    void updateEntry(ContactModel newEntry, @Nullable Response.Listener<JSONObject> callback) {
        super.updateEntry(newEntry, response -> {
            // Commit all
            this.commit(newEntry);

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

            // Commit all
            this.commit(newEntry);

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
