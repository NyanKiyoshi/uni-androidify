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

import java.util.ArrayList;

public class AddEditNoteActivity extends BaseChildActivity {

    public static final String EXTRA_TITLE =
            ".noteManagers.EXTRA_TITLE";
    public static final String EXTRA_BODY =
            ".noteManagers.EXTRA_BODY";
    public static final String EXTRA_CATEGORY_IDS =
            ".noteManagers.EXTRA_CATEGORY_IDS";

    public final static int RESULT_UPDATED = 2;
    public final static int RESULT_CREATED = 3;

    private int entryID;
    private EditText editTextTitle;
    private EditText editTextBody;

    private NoteModel item;

    private RemovableAdapter<CategoryModel> categoriesAdapter = new RemovableAdapter<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        this.editTextTitle = findViewById(R.id.edit_text_title);
        this.editTextBody = findViewById(R.id.edit_text_body);

        // Get the passed data
        Intent intent = this.getIntent();
        this.entryID = intent.getIntExtra(ViewUtils.EXTRA_ID, -1);

        if (this.entryID > -1) {
            this.setTitle("Edit Note");

            String title = intent.getStringExtra(EXTRA_TITLE);
            String body = intent.getStringExtra(EXTRA_BODY);

            this.editTextTitle.setText(title);
            this.editTextBody.setText(body);

            this.item = new NoteModel(this.entryID, title, body);
            this.item.setCategories(intent.getIntegerArrayListExtra(EXTRA_CATEGORY_IDS));
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

        if (this.item == null) {
            return;
        }

        ArrayList<CategoryModel> categoriesToAdd = new ArrayList<>(this.item.categories.size());
        for (int id : this.item.categories) {
            categoriesToAdd.add(CategoryModel.getAvailableCategories()[id]);
        }
        this.categoriesAdapter.addItems(categoriesToAdd);
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
        data.putExtra(ViewUtils.EXTRA_ID, this.entryID);
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_BODY, body);

        ArrayList<Integer> selectedCategories = new ArrayList<>(this.categoriesAdapter.items.size());
        for (CategoryModel category : this.categoriesAdapter.items) {
            selectedCategories.add(category.getId());
        }

        // Add groups data
        data.putIntegerArrayListExtra(EXTRA_CATEGORY_IDS, selectedCategories);

        setResult(this.item == null ? RESULT_CREATED : RESULT_UPDATED, data);
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
                    booleans[i] = true;
                    break;
                }
            }

            ++i;
        }

        (new AlertDialog.Builder(this)
                .setMultiChoiceItems(sequences, booleans, (dialog, which, isChecked) -> {
                    CategoryModel selectedItem = availableCategories[which];
                    if (!isChecked) {
                        this.categoriesAdapter.removeItem(selectedItem);
                    } else {
                        this.categoriesAdapter.addItem(selectedItem);
                    }
                })
                .setPositiveButton(R.string.close, (dialog, which) -> {})).show();
    }
}
