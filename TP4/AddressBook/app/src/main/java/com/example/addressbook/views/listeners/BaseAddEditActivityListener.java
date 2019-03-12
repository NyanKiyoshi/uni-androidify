package com.example.addressbook.views.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;



public abstract class BaseAddEditActivityListener<Model extends BaseModel> {
    static final int CREATE_ENTRY_REQUEST = 1;
    static final int UPDATE_ENTRY_REQUEST = 2;

    final String endpoint;

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

        this.endpoint = this.getEndpoint();

        this.modelClass = modelClass;
        this.context = context;
        this.listeners = CRUDEventsListener;
        this.requestQueue = requestQueue;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        Model newEntry = this.parseIntentResults(data);

        switch (requestCode) {
            case CREATE_ENTRY_REQUEST:
                this.listeners.onEntryStartUpdating(newEntry);
                this.createNewEntry(newEntry, null);
                break;
            case UPDATE_ENTRY_REQUEST:
                this.listeners.onEntryStartUpdating(newEntry);
                this.updateEntry(newEntry, null);
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

    void sendRequest(
            String requestURL, int method, JSONObject body,
            @Nullable Response.Listener<JSONObject> callback) {

        // Create the request object and attach it to the listeners
        Request<JSONObject> request = new JsonObjectRequest(
                method,
                requestURL,
                body,
                (JSONObject resp) -> {
                    if (callback != null) {
                        callback.onResponse(resp);
                    }
                    this.onServerResponse(resp);
                }, this::onError);

        // Append the request to the queue
        this.requestQueue.add(request);
    }

    void sendRequest(
            String requestURL, int method, Model entry,
            @Nullable Response.Listener<JSONObject> callback) {

        try {
            this.sendRequest(requestURL, method, entry.serialize(), callback);
        } catch (JSONException e) {
            this.onError(e);
        }
    }

    void sendRequest(String requestURL, int method, Model entry) {
        this.sendRequest(requestURL, method, entry, null);
    }

    void onError(Exception error) {
        // Log the full error
        Log.e(this.getClass().getName(),
                "got an error while getting entries", error);
        this.listeners.onEntryFailedUpdating();
    }

    void updateEntry(Model newEntry, @Nullable Response.Listener<JSONObject> callback) {
        // Create the request URL
        String requestURL = AppConfig.getURL(this.endpoint + "/" + newEntry.getId());

        // Send the update request
        this.sendRequest(requestURL, Request.Method.PUT, newEntry, callback);
    }

    void createNewEntry(Model newEntry, @Nullable Response.Listener<JSONObject> callback) {
        // Create the request URL
        String requestURL = AppConfig.getURL(this.endpoint);

        // Send the update request
        this.sendRequest(requestURL, Request.Method.POST, newEntry, callback);
    }

    void deleteEntry(Model newEntry, Response.Listener<String> listener) {
        // Create the request URL
        String requestURL = AppConfig.getURL(this.endpoint + "/" + newEntry.getId());

        Request request = new StringRequest(
                Request.Method.DELETE,
                requestURL,
                listener, this::onError);

        // Append the request to the queue
        this.requestQueue.add(request);
    }

    abstract String getEndpoint();
    abstract Model parseIntentResults(Intent data);
    abstract public void startViewEntry(Model item);
    abstract public void startCreateNewEntry();
    abstract public void startUpdateEntry(Model item);

    public void startDeleteEntry(Model item, Response.Listener<String> callback) {
        this.deleteEntry(item, callback);
    }
}
