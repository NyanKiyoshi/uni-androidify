package com.example.addressbook.views.listeners;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.GroupModel;
import com.example.addressbook.views.groupManagers.AddEditGroupActivity;
import com.example.addressbook.views.groupManagers.ViewGroupActivity;

public class GroupAddEditActivityListener
        extends BaseAddEditActivityListener<GroupModel> {

    final static String ENDPOINT = "/groups";

    public GroupAddEditActivityListener(
            Context context,
            CRUDEvents<GroupModel> CRUDEventsListener, RequestQueue requestQueue) {

        super(GroupModel.class, context, CRUDEventsListener, requestQueue);
    }

    @Override
    GroupModel parseIntentResults(Intent data) {
        int id = data.getIntExtra(ViewGroupActivity.EXTRA_ID, -1);
        String title = data.getStringExtra(ViewGroupActivity.EXTRA_TITLE);

        return new GroupModel(id, title);
    }

    @Override
    void updateEntry(GroupModel newEntry) {
        // Create the request URL
        String requestURL = AppConfig.getURL(ENDPOINT + "/" + newEntry.getId());

        // Send the update request
        this.sendRequest(requestURL, Request.Method.PUT, newEntry);
    }

    @Override
    void createNewEntry(GroupModel newEntry) {
        // Create the request URL
        String requestURL = AppConfig.getURL(ENDPOINT);

        // Send the update request
        this.sendRequest(requestURL, Request.Method.POST, newEntry);
    }

    @Override
    public void startViewEntry(GroupModel item) {
        Intent intent = new Intent(this.context, ViewGroupActivity.class);
        intent.putExtra(ViewGroupActivity.EXTRA_ID, item.getId());
        intent.putExtra(ViewGroupActivity.EXTRA_TITLE, item.getTitle());
        this.listeners.onIntentReadyToStart(intent, 0);
    }

    @Override
    public void startCreateNewEntry() {
        Intent intent = new Intent(this.context, AddEditGroupActivity.class);
        this.listeners.onIntentReadyToStart(intent, CREATE_ENTRY_REQUEST);
    }

    @Override
    public void startUpdateEntry(GroupModel item) {
        Intent intent = new Intent(this.context, AddEditGroupActivity.class);
        intent.putExtra(ViewGroupActivity.EXTRA_ID, item.getId());
        intent.putExtra(ViewGroupActivity.EXTRA_TITLE, item.getTitle());
        this.listeners.onIntentReadyToStart(intent, UPDATE_ENTRY_REQUEST);
    }
}
