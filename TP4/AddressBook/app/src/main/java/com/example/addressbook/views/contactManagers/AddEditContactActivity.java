package com.example.addressbook.views.contactManagers;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.addressbook.R;

public class AddEditContactActivity extends BaseContactActivity {

    private EditText editTextFirstname;
    private EditText editTextLastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        this.editTextFirstname = findViewById(R.id.edit_text_firstname);
        this.editTextLastname = findViewById(R.id.edit_text_lastname);

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

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
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
}
