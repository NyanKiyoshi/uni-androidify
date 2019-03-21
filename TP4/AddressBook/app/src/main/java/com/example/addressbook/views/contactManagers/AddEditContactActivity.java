package com.example.addressbook.views.contactManagers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.addressbook.R;
import com.example.addressbook.controllers.adapters.ContactAdapter;
import com.example.addressbook.controllers.files.FileOperation;
import com.example.addressbook.controllers.files.ImageProcessor;
import com.example.addressbook.controllers.files.RandomFile;
import com.example.addressbook.controllers.http.ContactAssociations;
import com.example.addressbook.controllers.http.GroupAssociations;
import com.example.addressbook.controllers.adapters.RemovableAdapter;
import com.example.addressbook.controllers.ViewUtils;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.BaseModel;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.models.GroupModel;
import com.example.addressbook.models.IStringSerializable;
import com.example.addressbook.models.MailAddressModel;
import com.example.addressbook.models.PhoneNumberModel;
import com.example.addressbook.models.PostalAddressModel;
import com.example.addressbook.views.IDeferrableActivity;
import com.example.addressbook.views.SelectAddressOrNew;
import com.example.addressbook.listeners.BaseAddEditActivityListener;
import com.example.addressbook.listeners.ContactAddEditActivityListener;
import com.example.addressbook.views.components.EntryListView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class AddEditContactActivity
        extends BaseContactActivity
        implements IDeferrableActivity, BaseAddEditActivityListener.CRUDEvents<ContactModel>  {

    private final static int PICKED_PICTURE = 1;
    private final ImageProcessor imageProcessor = new ImageProcessor();
    private final Handler handler = new Handler();

    private @Nullable Bitmap selectedPicture;

    private EditText editTextFirstname;
    private EditText editTextLastname;

    private ImageView picturePreview;
    private ContentLoadingProgressBar loadingBar;
    private RequestQueue requestQueue;

    private ContactModel item;
    private ContactAddEditActivityListener listener;

    private RemovableAdapter<BaseModel> groupsAdapter = new RemovableAdapter<>();
    private BaseModel[] groups;

    private SelectAddressOrNew<PostalAddressModel> postalAdapter;
    private ArrayList<Integer> removedPostals = new ArrayList<>();

    private SelectAddressOrNew<PhoneNumberModel> phonesAdapter;
    private ArrayList<Integer> removedPhones = new ArrayList<>();

    private SelectAddressOrNew<MailAddressModel> emailsAdapter;
    private ArrayList<Integer> removedEmails = new ArrayList<>();

    private boolean isPictureDeleted;

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
        final ImageView changePictureBtn = findViewById(R.id.change_picture);
        changePictureBtn.setOnClickListener(this::onChangePictureBtnPressed);

        // Set-up the 'delete picture' button
        final ImageView deletePictureBtn = findViewById(R.id.delete_picture);
        deletePictureBtn.setOnClickListener(this::onDeletePictureBtnPressed);

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

            this.updatePreviewPicture(intent.getStringExtra(EXTRA_FILE_ABS_PATH));
            this.item = new ContactModel(entryID, firstname, lastname);
        } else {
            setTitle("Add Contact");
        }

        this.createRecyclerViews();
    }

    private void updatePreviewPicture(String picture) {
        this.handler.post(() ->
                ContactAdapter.setImage(picture, this.picturePreview, false));
    }

    private void createRecyclerViews() {
        final ViewGroup viewGroup = this.findViewById(android.R.id.content);

        final EntryListView groupView = this.findViewById(R.id.group_manager);
        groupView.getRecyclerView().setAdapter(this.groupsAdapter);
        groupView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        groupView.getAddButton().setOnClickListener(this::selectGroup);

        this.postalAdapter = new SelectAddressOrNew<>(
                PostalAddressModel.class,
                viewGroup, this, this.findViewById(R.id.postal_manager),
                InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS,
                (removedItem, pos) -> {
                    // Only append to the item to deletion list if it is stored on the server
                    if (removedItem.getId() > 0) {
                        this.removedPostals.add(removedItem.getId());
                    }
                    this.postalAdapter.removeItem(pos);
                });

        this.phonesAdapter = new SelectAddressOrNew<>(
                PhoneNumberModel.class,
                viewGroup, this, this.findViewById(R.id.phone_manager),
                InputType.TYPE_CLASS_PHONE,
                (removedItem, pos) -> {
                    // Only append to the item to deletion list if it is stored on the server
                    if (removedItem.getId() > 0) {
                        this.removedPhones.add(removedItem.getId());
                    }
                    this.phonesAdapter.removeItem(pos);
                });

        this.emailsAdapter = new SelectAddressOrNew<>(
                MailAddressModel.class,
                viewGroup, this, this.findViewById(R.id.mail_manager),
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                (removedItem, pos) -> {
                    // Only append to the item to deletion list if it is stored on the server
                    if (removedItem.getId() > 0) {
                        this.removedEmails.add(removedItem.getId());
                    }
                    this.emailsAdapter.removeItem(pos);
                });

        if (this.item == null) {
            return;
        }

        this.requestQueue.add(new JsonArrayRequest(
                GroupAssociations.getPersonGroupURL(this.item.getId()),
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

        this.requestQueue.add(new JsonArrayRequest(
                ContactAssociations.getPostalURL(this.item.getId()),
                response -> {
                    try {
                        // Add the postals to the adapter to put them on the view
                        this.postalAdapter.clear();
                        this.postalAdapter.addItems(PostalAddressModel.deserialize(
                                PostalAddressModel.class, response));
                    } catch (Exception e) {
                        this.onError(e);
                    }
                },
                this::onError
        ));

        this.requestQueue.add(new JsonArrayRequest(
                ContactAssociations.getPhoneURL(this.item.getId()),
                response -> {
                    try {
                        // Add the phone numbers to the adapter to put them on the view
                        this.phonesAdapter.clear();
                        this.phonesAdapter.addItems(PhoneNumberModel.deserialize(
                                PhoneNumberModel.class, response));
                    } catch (Exception e) {
                        this.onError(e);
                    }
                },
                this::onError
        ));

        this.requestQueue.add(new JsonArrayRequest(
                ContactAssociations.getMailURL(this.item.getId()),
                response -> {
                    try {
                        // Add the phone numbers to the adapter to put them on the view
                        this.emailsAdapter.clear();
                        this.emailsAdapter.addItems(MailAddressModel.deserialize(
                                MailAddressModel.class, response));
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
        data.putExtra(EXTRA_IS_PICTURE_DELETED, this.isPictureDeleted);

        if (this.selectedPicture != null) {
            String pictureCopyPath = RandomFile.sfromBase(this.getFilesDir());
            data.putExtra(EXTRA_FILE_ABS_PATH, pictureCopyPath);
            this.handler.post(() -> {
                try {
                    FileOperation.saveBitmap(selectedPicture, pictureCopyPath);
                } catch (IOException exc) {
                    this.onError(exc);
                }
            });
        }

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        // Add groups data
        data.putExtras(
                GroupAssociations.applyGroups(this.groupsAdapter, this.item));

        try {
            ContactAssociations.applyPostalAddresses(this.postalAdapter, this.removedPostals, data);
            ContactAssociations.applyPhoneNumbers(this.phonesAdapter, this.removedPhones, data);
            ContactAssociations.applyEmails(this.emailsAdapter, this.removedEmails, data);
        } catch (JSONException e) {
            Log.wtf("Failed to commit data from contact", e);
            Toast.makeText(this, R.string.failed_to_add_postals, Toast.LENGTH_LONG).show();
        }

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

    public void onDeletePictureBtnPressed(View view) {
        this.isPictureDeleted = true;
        this.updatePreviewPicture(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        if (requestCode == PICKED_PICTURE) {
            this.setPickedPicture(data.getData());
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

    private void setPickedPicture(Uri pictureURI) {
        this.handler.post(() -> {
            try {
                this.selectedPicture = this.imageProcessor.compress(
                        this.getContentResolver(), pictureURI);
                this.picturePreview.setImageBitmap(this.selectedPicture);
            } catch (IOException e) {
                Log.wtf("Failed to get the picture", e);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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

            for (BaseModel selectedGroup : this.groupsAdapter.items) {
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
                    BaseModel selectedItem = this.groups[which];
                    if (!isChecked) {
                        this.groupsAdapter.removeItem(selectedItem);
                    } else {
                        this.groupsAdapter.addItem(selectedItem);
                    }
                })
                .setPositiveButton(R.string.close, (dialog, which) -> {})).show();
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
    public void onEntryFailedUpdating(Exception exc) {
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
