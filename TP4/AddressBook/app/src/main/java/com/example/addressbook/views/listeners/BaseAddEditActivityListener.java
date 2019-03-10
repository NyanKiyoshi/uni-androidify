package com.example.addressbook.views.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.addressbook.models.BaseModel;
import com.example.addressbook.models.ContactModel;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;



public abstract class BaseAddEditActivityListener<Model extends BaseModel> {
    static final int CREATE_ENTRY_REQUEST = 1;
    static final int UPDATE_ENTRY_REQUEST = 2;

    public interface CRUDEvents<Model> {
        void onEntryUpdated(Model newItem);
        void onEntryFailedUpdating();
        void onEntryStartUpdating(Model newItem);
        void onIntentReadyToStart(Intent intent, int requestCode);
    }

    private Class<Model> modelClass;
    private RequestQueue requestQueue;
    Context context;
    CRUDEvents<Model> listeners;

    public BaseAddEditActivityListener(
            Class<Model> modelClass,
            Context context,
            CRUDEvents<Model> CRUDEventsListener,
            RequestQueue requestQueue) {

        this.modelClass = modelClass;
        this.context = context;
        this.listeners = CRUDEventsListener;
        this.requestQueue = requestQueue;
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
                this.updateEntry(newEntry);
                break;
            default:
                break;
        }
    }

    private void onResponseHandled(Model newItem) {
        this.listeners.onEntryUpdated(newItem);
    }

    private void onServerResponse(JSONObject data) {
        try {
            this.onResponseHandled(
                    (Model)this.modelClass.newInstance().fromJSON(data));
        } catch (JSONException|IllegalAccessException|InstantiationException e) {
            this.onError(e);
        }
    }

    void sendRequest(String requestURL, int method, Model entry) {
        try {
            // Create the request object and attach it to the listeners
            Request<JSONObject> request = new JsonObjectRequest(
                    method,
                    requestURL,
                    entry.serialize(),
                    this::onServerResponse, this::onError);

            // Append the request to the queue
            this.requestQueue.add(request);
        } catch (JSONException e) {
            this.onError(e);
        }
    }

    void onError(Exception error) {
        // Log the full error
        Log.e(this.getClass().getName(),
                "got an error while getting entries", error);
        this.listeners.onEntryFailedUpdating();
    }

    abstract Model parseIntentResults(Intent data);
    abstract void updateEntry(Model newEntry);
    abstract void createNewEntry(Model newEntry);
    abstract public void startViewEntry(ContactModel item);
    abstract public void startCreateNewEntry();
    abstract public void startUpdateEntry(Model item);
}
