package com.example.addressbook.views.contactManagers;

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

import com.example.addressbook.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

public class AddEditContactActivity extends BaseContactActivity {
    private final static int PICKED_PICTURE = 1;

    private EditText editTextFirstname;
    private EditText editTextLastname;

    private ImageView picturePreview;

    private Uri pictureURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        this.editTextFirstname = findViewById(R.id.edit_text_firstname);
        this.editTextLastname = findViewById(R.id.edit_text_lastname);

        // Get the picture preview box
        this.picturePreview = findViewById(R.id.picture_preview_box);

        // Set-up the 'select picture' button
        final AppCompatButton changePictureBtn = findViewById(R.id.change_picture);
        changePictureBtn.setOnClickListener(this::onChangePictureBtnPressed);

        // Get the activity's intent object
        final Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Contact");
            editTextFirstname.setText(intent.getStringExtra(EXTRA_FIRSTNAME));
            editTextLastname.setText(intent.getStringExtra(EXTRA_LASTNAME));
        } else {
            setTitle("Add Contact");
        }
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
            Log.d("WWWOOO", Integer.toString(requestCode));
            try {
                this.setPickedPicture(data.getData());
            } catch (IOException e) {
                Log.wtf("Failed to get the picture", e);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveEntry();
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
}
