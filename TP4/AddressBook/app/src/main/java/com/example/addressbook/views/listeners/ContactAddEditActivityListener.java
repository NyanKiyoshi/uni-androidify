package com.example.addressbook.views.listeners;

import android.content.Context;
import android.content.Intent;

import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.contactManagers.AddEditContactActivity;
import com.example.addressbook.views.contactManagers.ViewContactActivity;

public class ContactAddEditActivityListener
        extends BaseAddEditActivityListener<ContactModel> {

    public ContactAddEditActivityListener(
            Context context,
            CRUDEvents<ContactModel> CRUDEventsListener) {

        super(context, CRUDEventsListener);
    }

    @Override
    ContactModel parseIntentResults(Intent data) {
        int id = data.getIntExtra(ViewContactActivity.EXTRA_ID, -1);
        String firstname = data.getStringExtra(ViewContactActivity.EXTRA_FIRSTNAME);
        String lastname = data.getStringExtra(ViewContactActivity.EXTRA_LASTNAME);

        return new ContactModel(id, firstname, lastname);
    }

    @Override
    void updateNewEntry(ContactModel newEntry) {
        this.listeners.onEntryUpdated(newEntry);
        // TODO: http request here
    }

    @Override
    void createNewEntry(ContactModel newEntry) {
        this.listeners.onEntryUpdated(newEntry);
        // TODO: http request here
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
