package com.example.addressbook.views.groupManagers;

import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.addressbook.R;
import com.example.addressbook.controllers.ContactAdapter;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.ContactModel;

public class SelectContact extends BaseGroupActivity {
    private ContactAdapter adapter;
    private ContentLoadingProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_contact);

        // Set the activity's title
        setTitle(R.string.select_contact);

        // Create the contact adapter
        this.adapter = new ContactAdapter(this::getBaseContext, this::onSelect);

        // Create a progress bar for HTTP requests
        this.loadingBar = new ContentLoadingProgressBar(this);
        this.loadingBar.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(new JsonArrayRequest(
                AppConfig.getURL("/persons"),
                response -> {
                    try {
                        this.adapter.addItems(
                                ContactModel.deserialize(ContactModel.class, response));
                    } catch (Exception e) {
                        this.onError(e);
                    }

                    this.loadingBar.hide();
                },
                this::onError
        ));

        // Set-up and bind the recycler view
        RecyclerView recyclerView = this.findViewById(R.id.listRecyclerView);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    void onError(Exception exc) {
        super.onError(exc);
        this.setResult(-1);
        this.finish();
    }

    private void onSelect(ContactModel item, int pos) {
        this.setResult(item.getId());
        this.finish();
    }
}
