package com.example.addressbook.views.groupManagers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.R;
import com.example.addressbook.controllers.ContactAdapter;
import com.example.addressbook.controllers.RemoveableContactAdapter;
import com.example.addressbook.models.ContactModel;

public class AddEditGroupActivity
        extends BaseGroupActivity
        implements ContactAdapter.IHasContext {

    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_group);

        this.editTextTitle = findViewById(R.id.edit_text_title);

        // Get the activity's intent object
        final Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Group");
            this.editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
        } else {
            setTitle("Add Group");
        }

        // Set-up and bind the recycler view
        RemoveableContactAdapter adapter = new RemoveableContactAdapter(this, null);
        RecyclerView recyclerView = this.findViewById(R.id.listRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter.setRemoveClickListener((item, pos) -> {
            adapter.removeItem(pos);
        });
        adapter.addItem(new ContactModel(1, "abc", "def"));
    }

    public Context getContext() {
        return this.getBaseContext();
    }

    private void saveEntry() {
        String title = this.editTextTitle.getText().toString();

        if (title.trim().isEmpty()) {
            Toast.makeText(
                    this, R.string.please_fill_form, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);

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
