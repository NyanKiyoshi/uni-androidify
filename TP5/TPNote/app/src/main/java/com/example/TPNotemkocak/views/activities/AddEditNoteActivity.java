package com.example.TPNotemkocak.views.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.TPNotemkocak.R;
import com.example.TPNotemkocak.controllers.adapters.RemovableAdapter;
import com.example.TPNotemkocak.controllers.ViewUtils;
import com.example.TPNotemkocak.models.NoteModel;
import com.example.TPNotemkocak.models.CategoryModel;
import com.example.TPNotemkocak.views.BaseChildActivity;
import com.example.TPNotemkocak.views.components.EntryListView;

public class AddEditNoteActivity extends BaseChildActivity {

    public static final String EXTRA_IS_EDIT =
            ".noteManagers.EXTRA_ID";
    public static final String EXTRA_TITLE =
            ".noteManagers.EXTRA_TITLE";
    public static final String EXTRA_BODY =
            ".noteManagers.EXTRA_BODY";

    private EditText editTextTitle;
    private EditText editTextBody;

    private NoteModel item;
    private boolean isEditing;

    private RemovableAdapter<CategoryModel> categoriesAdapter = new RemovableAdapter<>();
    private CategoryModel[] selectedCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        this.editTextTitle = findViewById(R.id.edit_text_title);
        this.editTextBody = findViewById(R.id.edit_text_body);

        // Get the passed data
        Intent intent = this.getIntent();
        this.isEditing = intent.getBooleanExtra(EXTRA_IS_EDIT, false);

        if (this.isEditing) {
            this.setTitle("Edit Note");

            String title = intent.getStringExtra(EXTRA_TITLE);
            String body = intent.getStringExtra(EXTRA_BODY);

            this.editTextTitle.setText(title);
            this.editTextBody.setText(body);

            this.item = new NoteModel(title, body);
        } else {
            this.setTitle("Add Note");
        }

        this.createRecyclerViews();
    }

    private void createRecyclerViews() {
        final EntryListView groupView = this.findViewById(R.id.category_manager);
        groupView.getRecyclerView().setAdapter(this.categoriesAdapter);
        groupView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        groupView.getAddButton().setOnClickListener((v) -> this.openGroupSelection());
    }

    private void saveEntry() {
        String title = this.editTextTitle.getText().toString();
        String body = this.editTextBody.getText().toString();

        if (title.trim().isEmpty() || body.trim().isEmpty()) {
            Toast.makeText(
                    this, R.string.please_fill_form, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_BODY, body);

        // Add groups data
//        data.putExtras(
//                GroupAssociations.applyGroups(this.categoriesAdapter, this.item));

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_note_menu, menu);

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

    private void openGroupSelection() {
        CategoryModel[] availableCategories = CategoryModel.getAvailableCategories();
        String[] sequences = new String[availableCategories.length];
        boolean[] booleans = new boolean[availableCategories.length];

        int i = 0;
        for (CategoryModel category : availableCategories) {
            sequences[i] = category.toString();

            for (CategoryModel selectedCat : this.categoriesAdapter.items) {
                if (selectedCat.getId() == category.getId()) {
                    this.selectedCategories[i] = selectedCat;
                    booleans[i] = true;
                    break;
                }
            }

            ++i;
        }

        (new AlertDialog.Builder(this)
                .setMultiChoiceItems(sequences, booleans, (dialog, which, isChecked) -> {
                    CategoryModel selectedItem = this.selectedCategories[which];
                    if (!isChecked) {
                        this.categoriesAdapter.removeItem(selectedItem);
                    } else {
                        this.categoriesAdapter.addItem(selectedItem);
                    }
                })
                .setPositiveButton(R.string.close, (dialog, which) -> {})).show();
    }
}
