package com.example.addressbook.views.groupManagers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.addressbook.R;
import com.example.addressbook.controllers.GroupAdapter;
import com.example.addressbook.controllers.RemoveableContactAdapter;
import com.example.addressbook.controllers.ViewUtils;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.models.GroupModel;
import com.example.addressbook.views.IDeferrableActivity;
import com.example.addressbook.views.listeners.BaseAddEditActivityListener;
import com.example.addressbook.views.listeners.GroupAddEditActivityListener;
import com.google.android.material.button.MaterialButton;

public class ViewGroupActivity
        extends BaseGroupActivity
        implements BaseAddEditActivityListener.CRUDEvents<GroupModel>, IDeferrableActivity {

    private static final int REQUEST_SELECT_CONTACT = 2;

    private BaseAddEditActivityListener<GroupModel> activityListener;
    private TextView textViewTitle;

    private GroupModel groupModel;

    private ContentLoadingProgressBar loadingBar;
    private RemoveableContactAdapter adapter;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        this.requestQueue = Volley.newRequestQueue(this);
        this.textViewTitle = findViewById(R.id.edit_text_title);

        // Get the activity's intent object
        final Intent intent = getIntent();
        setTitle("Viewing Group");

        // Create the item model object
        this.groupModel = new GroupModel(
                intent.getIntExtra(EXTRA_ID, -1),
                intent.getStringExtra(EXTRA_TITLE)
        );

        // Create a progress bar for HTTP requests
        this.loadingBar = new ContentLoadingProgressBar(this);

        // Create the CRUD activity listener
        this.activityListener = new GroupAddEditActivityListener(
                this, this, requestQueue);

        this.textViewTitle.setText(this.groupModel.getTitle());

        this.adapter = new RemoveableContactAdapter(this::getActivity, null);
        this.adapter.setRemoveClickListener((item, pos) -> {
            GroupAdapter.deleteAssociation(
                    this.requestQueue,
                    this.groupModel.getId(), item.getId(),
                    this::refreshData, this::onError);
            this.adapter.removeItem(pos);
        });

        // Set-up and bind the recycler view
        RecyclerView recyclerView = this.findViewById(R.id.listRecyclerView);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MaterialButton addContactBtn = this.findViewById(R.id.add_contact_btn);
        addContactBtn.setOnClickListener(v -> openContactSelection());

        this.refreshData();
    }

    private void openContactSelection() {
        Intent intent = new Intent(this, SelectContact.class);
        this.startActivityForResult(intent, REQUEST_SELECT_CONTACT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.view_entry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_entry:
                this.activityListener.startUpdateEntry(this.groupModel);
                break;

            case R.id.delete_entry:
                ViewUtils.PromptDelete(this, this.groupModel);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void refreshData() {
        this.requestQueue.add(new JsonArrayRequest(
                AppConfig.getURL("/groups/" + this.groupModel.getId() + "/persons"),
                response -> {
                    try {
                        this.adapter.clear();
                        this.adapter.addItems(
                                ContactModel.deserialize(ContactModel.class, response));
                    } catch (Exception e) {
                        this.onError(e);
                    }
                },
                this::onError
        ));
    }

    @Override
    public void onEntryUpdated(GroupModel newItem) {
        this.groupModel = newItem;
        this.textViewTitle.setText(newItem.getTitle());
        this.loadingBar.hide();
        Toast.makeText(this, R.string.group_updated, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEntryFailedUpdating() {
        Toast.makeText(this, R.string.failed_to_update, Toast.LENGTH_SHORT).show();
        this.loadingBar.hide();
    }

    @Override
    public void onEntryStartUpdating(GroupModel newItem) {
        this.loadingBar.show();
    }

    @Override
    public void onIntentReadyToStart(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.activityListener.onActivityResult(requestCode, resultCode, data);

        if (resultCode < 0) {
            return;
        }

        if (requestCode == REQUEST_SELECT_CONTACT) {
            GroupAdapter.associateToContact(
                    this.requestQueue,
                    this.groupModel.getId(), resultCode,
                    this::refreshData, this::onAssociateError);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public ContentLoadingProgressBar getLoadingBar() {
        return this.loadingBar;
    }

    @Override
    public BaseAddEditActivityListener getListener() {
        return this.activityListener;
    }

    private void onAssociateError(Exception exc) {
        if (VolleyError.class.isAssignableFrom(exc.getClass())) {
            VolleyError volleyError = (VolleyError) exc;

            if (volleyError.networkResponse != null) {
                if (volleyError.networkResponse.statusCode == 400) {
                    Toast.makeText(
                            this, R.string.already_in_group, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        this.onError(exc);
    }
}
