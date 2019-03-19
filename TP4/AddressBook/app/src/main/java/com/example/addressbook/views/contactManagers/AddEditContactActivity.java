package com.example.addressbook.views.contactManagers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.addressbook.R;
import com.example.addressbook.controllers.GroupAssociations;
import com.example.addressbook.controllers.adapters.RemovableAdapter;
import com.example.addressbook.controllers.ViewUtils;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.models.GroupModel;
import com.example.addressbook.models.IStringSerializable;
import com.example.addressbook.views.IDeferrableActivity;
import com.example.addressbook.views.listeners.BaseAddEditActivityListener;
import com.example.addressbook.views.listeners.ContactAddEditActivityListener;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Set;
import java.util.UUID;

public class AddEditContactActivity
        extends BaseContactActivity
        implements IDeferrableActivity, BaseAddEditActivityListener.CRUDEvents<ContactModel>  {

    private final static int PICKED_PICTURE = 1;

    private EditText editTextFirstname;
    private EditText editTextLastname;

    private ImageView picturePreview;
    private ContentLoadingProgressBar loadingBar;
    private RequestQueue requestQueue;

    private Uri pictureURI;
    private ContactModel item;
    private ContactAddEditActivityListener listener;

    private RemovableAdapter groupsAdapter = new RemovableAdapter();
    private IStringSerializable[] groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        this.requestQueue = Volley.newRequestQueue(this);

        this.editTextFirstname = findViewById(R.id.edit_text_firstname);
        this.editTextLastname = findViewById(R.id.edit_text_lastname);

        this.loadingBar = new ContentLoadingProgressBar(this);
        this.listener = new ContactAddEditActivityListener(
                this.getApplicationContext(), this, Volley.newRequestQueue(this));

        // Get the picture preview box
        this.picturePreview = findViewById(R.id.picture_preview_box);

        // Set-up the 'select picture' button
        final AppCompatButton changePictureBtn = findViewById(R.id.change_picture);
        changePictureBtn.setOnClickListener(this::onChangePictureBtnPressed);

        final MaterialButton manageGroupsBtn = findViewById(R.id.manage_group_btn);
        manageGroupsBtn.setOnClickListener(this::selectGroup);

        // Get the activity's intent object
        final Intent intent = getIntent();

        // Get the entry ID
        final int entryID = intent.getIntExtra(EXTRA_ID, -1);

        if (entryID > -1) {
            setTitle("Edit Contact");

            String firstname = intent.getStringExtra(EXTRA_FIRSTNAME);
            String lastname = intent.getStringExtra(EXTRA_LASTNAME);

            editTextFirstname.setText(firstname);
            editTextLastname.setText(lastname);

            String picture = intent.getStringExtra(EXTRA_FILE_ABS_PATH);
            ViewUtils.SetImage(this.picturePreview, picture, R.drawable.ic_menu_gallery_gray);

            this.item = new ContactModel(entryID, firstname, lastname);
        } else {
            setTitle("Add Contact");
        }

        this.createRecyclerViews();
    }

    private void createRecyclerViews() {
        final RecyclerView groupView = this.findViewById(R.id.groupListRecyclerView);
        groupView.setAdapter(this.groupsAdapter);
        groupView.setLayoutManager(new LinearLayoutManager(this));

        if (this.item == null) {
            return;
        }

        this.requestQueue.add(new JsonArrayRequest(
                AppConfig.getURL("/persons/" + this.item.getId() + "/groups"),
                response -> {
                    try {
                        // Copy the base groups to protect them against edition
                        this.item.groups = GroupModel.deserialize(GroupModel.class, response);

                        // Add the groups to the adapter to put them on the view
                        this.groupsAdapter.clear();
                        this.groupsAdapter.addItems(this.item.groups);
                    } catch (Exception e) {
                        this.onError(e);
                    }
                },
                this::onError
        ));
    }

    private void saveEntry() {
        String firstName = editTextFirstname.getText().toString();
        String lastName = editTextLastname.getText().toString();

        if (firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            Toast.makeText(
                    this, R.string.please_fill_form, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_FIRSTNAME, firstName);
        data.putExtra(EXTRA_LASTNAME, lastName);
        try {
            data.putExtra(EXTRA_FILE_ABS_PATH, this.savePictureToStorage());
        } catch (IOException exc) {
            Log.wtf("Skipping copy of picture", exc);
        }

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        // Add groups data
        data.putExtras(
                GroupAssociations.applyGroups(this.groupsAdapter, this.item));

        setResult(RESULT_OK, data);
        finish();
    }

    public void onChangePictureBtnPressed(View view) {
        Intent intent = new Intent();

        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // Always show the chooser (if there are multiple options available)
        startActivityForResult(
                Intent.createChooser(intent, "Select Picture"), PICKED_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        if (requestCode == PICKED_PICTURE) {
            try {
                this.setPickedPicture(data.getData());
            } catch (IOException e) {
                Log.wtf("Failed to get the picture", e);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_contact_menu, menu);

        // If the user is editing an entry, show the delete option on the menu
        if (this.item != null) {
            menu.findItem(R.id.delete_entry).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveEntry();
                return true;
            case R.id.delete_entry:
                ViewUtils.PromptDelete(this, this.item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String savePictureToStorage() throws IOException {
        if (this.pictureURI == null) {
            return null;
        }

        // Get the copy destination
        File destinationFile = new File(
                this.getFilesDir().getAbsolutePath(), UUID.randomUUID().toString());
        FileChannel destination =
                new FileOutputStream(destinationFile).getChannel();

        // Get the source file's path
        ParcelFileDescriptor sourcefd =
                this.getContentResolver().openFileDescriptor(pictureURI, "r");
        FileChannel source =
                new FileInputStream(sourcefd.getFileDescriptor()).getChannel();

        // Copy the source file to our destination
        try {
            destination.transferFrom(source, 0, source.size());
        }
        catch (Exception exc) {
            source.close();
            destination.close();
            throw exc;
        }

        return destinationFile.getAbsolutePath();
    }

    private void setPickedPicture(Uri pictureURI) throws IOException {
        this.pictureURI = pictureURI;

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pictureURI);
        this.picturePreview.setImageBitmap(bitmap);
    }

    private void selectGroup(View view) {
        // If the groups are cached, just use them, don't request again
        if (this.groups != null) {
            this.openGroupSelection();
            return;
        }

        this.loadingBar.show();
        this.requestQueue.add(new JsonArrayRequest(
                AppConfig.getURL("/groups/"),
                response -> {
                    this.loadingBar.hide();
                    try {
                        this.groups = GroupModel.deserialize(GroupModel.class, response);
                        this.openGroupSelection();
                    } catch (Exception e) {
                        this.onError(e);
                    }
                },
                this::onError
        ));
    }

    private void openGroupSelection() {
        String[] sequences = new String[this.groups.length];
        boolean[] booleans = new boolean[this.groups.length];

        int i = 0;
        for (IStringSerializable group: this.groups) {
            sequences[i] = group.toString();

            for (IStringSerializable selectedGroup : this.groupsAdapter.items) {
                if (selectedGroup.getId() == group.getId()) {
                    this.groups[i] = selectedGroup;
                    booleans[i] = true;
                    break;
                }
            }

            ++i;
        }

        (new AlertDialog.Builder(this)
                .setMultiChoiceItems(sequences, booleans, (dialog, which, isChecked) -> {
                    IStringSerializable selectedItem = this.groups[which];
                    if (!isChecked) {
                        this.groupsAdapter.removeItem(selectedItem);
                    } else {
                        this.groupsAdapter.addItem(selectedItem);
                    }
                })
                .setPositiveButton(R.string.close, (dialog, which) -> {})).show();
    }

    private static void removeOrAdd(Set set, Object o) {
        if (!set.remove(o)) {
            set.add(o);
        }
    }

    private void onError(Exception exc) {
        Log.wtf("Failed to retrieve data", exc);
        Toast.makeText(this, exc.getMessage(), Toast.LENGTH_LONG).show();
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
        return this.listener;
    }

    @Override
    public void refreshData() {
        // nop
    }

    @Override
    public void onEntryUpdated(ContactModel newItem) {
        // nop: only the parents are handling those events,
        // to notify the user of the data update's success,
        // as this activity is destroyed once data is sent,
        // it doesn't wait for data to be received.
    }

    @Override
    public void onEntryFailedUpdating() {
        Toast.makeText(this, R.string.failed_to_update, Toast.LENGTH_SHORT).show();
        this.loadingBar.hide();
    }

    @Override
    public void onEntryStartUpdating(ContactModel newItem) {
        // nop: this event is not used,
        // this is received by the parent activity.
    }

    @Override
    public void onIntentReadyToStart(Intent intent, int requestCode) {
        // nop: this is not used, as this activity
        // does not start or wait for any activity.
    }
}
