package com.example.addressbook.views.listeners;

import android.content.Context;
import android.content.Intent;

import com.example.addressbook.models.ContactModel;

import static android.app.Activity.RESULT_OK;



public abstract class BaseAddEditActivityListener<Model> {
    static final int CREATE_ENTRY_REQUEST = 1;
    static final int UPDATE_ENTRY_REQUEST = 2;

    public interface CRUDEvents<Model> {
        void onEntryUpdated(Model newItem);
        void onEntryStartUpdating(Model newItem);
        void onIntentReadyToStart(Intent intent, int requestCode);
    }

    Context context;
    CRUDEvents<Model> listeners;

    public BaseAddEditActivityListener(
            Context context,
            CRUDEvents<Model> CRUDEventsListener) {

        this.context = context;
        this.listeners = CRUDEventsListener;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        Model newEntry = this.parseIntentResults(data);

        switch (requestCode) {
            case CREATE_ENTRY_REQUEST:
                this.listeners.onEntryStartUpdating(newEntry);
                this.createNewEntry(newEntry);
                break;
            case UPDATE_ENTRY_REQUEST:
                this.listeners.onEntryStartUpdating(newEntry);
                this.updateNewEntry(newEntry);
                break;
            default:
                break;
        }
    }

    void onResponseHandled(Model newItem) {
        this.listeners.onEntryUpdated(newItem);
    }

    abstract Model parseIntentResults(Intent data);
    abstract void updateNewEntry(Model newEntry);
    abstract void createNewEntry(Model newEntry);
    abstract public void startViewEntry(ContactModel item);
    abstract public void startCreateNewEntry();
    abstract public void startUpdateEntry(Model item);
}
