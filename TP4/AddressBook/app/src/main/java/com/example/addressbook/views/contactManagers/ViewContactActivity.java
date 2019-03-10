package com.example.addressbook.views.contactManagers;

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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.addressbook.R;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.listeners.BaseAddEditActivityListener;
import com.example.addressbook.views.listeners.ContactAddEditActivityListener;

public class ViewContactActivity
        extends BaseContactActivity
        implements BaseAddEditActivityListener.CRUDEvents<ContactModel> {

    private ContactAddEditActivityListener activityListener;
    private TextView textViewFirstname;
    private TextView textViewLastname;

    private ContactModel contactModel;

    private ContentLoadingProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        this.textViewFirstname = findViewById(R.id.edit_text_firstname);
        this.textViewLastname = findViewById(R.id.edit_text_lastname);

        // Get the activity's intent object
        final Intent intent = getIntent();
        setTitle("Viewing Contact");

        // Create the item model object
        this.contactModel = new ContactModel(
                intent.getIntExtra(EXTRA_ID, -1),
                intent.getStringExtra(EXTRA_FIRSTNAME),
                intent.getStringExtra(EXTRA_LASTNAME)
        );

        // Create a progress bar for HTTP requests
        this.loadingBar = new ContentLoadingProgressBar(this);

        // Create the CRUD activity listener
        this.activityListener = new ContactAddEditActivityListener(
                this, this, requestQueue);

        textViewFirstname.setText(this.contactModel.getFirstName());
        textViewLastname.setText(this.contactModel.getLastName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.view_entry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_entry) {
            this.activityListener.startUpdateEntry(this.contactModel);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEntryUpdated(ContactModel newItem) {
        this.contactModel = newItem;
        this.textViewFirstname.setText(newItem.getFirstName());
        this.textViewLastname.setText(newItem.getLastName());
        this.loadingBar.hide();
        Toast.makeText(this, R.string.contact_updated, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEntryFailedUpdating() {
        Toast.makeText(this, R.string.failed_to_create, Toast.LENGTH_SHORT).show();
        this.loadingBar.hide();
    }

    @Override
    public void onEntryStartUpdating(ContactModel newItem) {
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
    }
}
