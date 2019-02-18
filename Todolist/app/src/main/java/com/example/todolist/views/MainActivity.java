package com.example.todolist.views;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.todolist.R;
import com.example.todolist.controllers.DatabaseWrapper;
import com.example.todolist.models.TodoEntry;

public class MainActivity extends AppCompatActivity {

    private EditText dialogEditText;
    private AlertDialog.Builder inputDialog;
    private DatabaseWrapper database;
    private LinearLayout todoEntriesContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.dialogEditText = new EditText(this);
        this.inputDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.new_entry_title)
                .setMessage(R.string.new_entry_help_text)
                .setView(dialogEditText)
                .setPositiveButton("Create", this::onCreateSubmitClick)
                .setNegativeButton("Cancel", this::onCreateCancelClick)
                .setOnDismissListener(this::onCreateDialogDismiss);
        this.database = new DatabaseWrapper(this);
        this.todoEntriesContainer = this.findViewById(R.id.todo_entries_container);

        FloatingActionButton createButton = findViewById(R.id.fab);
        createButton.setOnClickListener(this::onCreateOpenClick);

        for (TodoEntry entry : TodoEntry.getEntries(database)) {
            this.addEntryToInterface(entry);
        }
    }

    public void addEntryToInterface(TodoEntry entry) {
        TodoEntryFragment todoEntryFragment = new TodoEntryFragment(entry);
        todoEntryFragment.setOnDeleteListener(this::onTodoEntryDeleted);

        View view = todoEntryFragment.onCreateView(
                getLayoutInflater(), this.todoEntriesContainer, null);
        this.todoEntriesContainer.addView(view, 0);
    }

    public void onTodoEntryDeleted(View view) {
        this.todoEntriesContainer.removeView(view);
    }

    public void onCreateOpenClick(View v) {
        this.inputDialog.show();
    }

    public void onCreateDialogDismiss(DialogInterface dialog) {
        ((ViewGroup) this.dialogEditText.getParent()).removeView(this.dialogEditText);
    }

    public void onCreateSubmitClick(DialogInterface dialog, int whichButton) {
        String todoText = this.dialogEditText.getText().toString();

        if (todoText.isEmpty()) {
            // Ignore empty
            Log.i("Todo Dialog", "Aborted, todo text is empty");
            return;
        }

        TodoEntry newEntry = new TodoEntry(todoText);
        newEntry.createEntry(this.database);
        addEntryToInterface(newEntry);
    }

    public void onCreateCancelClick(DialogInterface dialog, int whichButton) {
        // nop
    }
}
