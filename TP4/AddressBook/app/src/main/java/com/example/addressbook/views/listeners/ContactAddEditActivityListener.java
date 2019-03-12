package com.example.addressbook.views.listeners;

import android.content.Context;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.contactManagers.AddEditContactActivity;
import com.example.addressbook.views.contactManagers.BaseContactActivity;

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

        return new ContactModel(id, firstname, lastname);
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
