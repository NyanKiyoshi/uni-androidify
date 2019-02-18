package com.example.todolist.views;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.todolist.R;
import com.example.todolist.controllers.DatabaseWrapper;
import com.example.todolist.controllers.TodoEntryAdapter;
import com.example.todolist.models.TodoEntry;

public class MainActivity extends AppCompatActivity {

    private TodoEntryAdapter todoEntryAdapter;
    private EditText dialogEditText;
    private AlertDialog.Builder inputDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.todoEntryAdapter = new TodoEntryAdapter(new DatabaseWrapper(this));
        this.dialogEditText = new EditText(this);
        this.inputDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.new_entry_title)
                .setMessage(R.string.new_entry_help_text)
                .setView(dialogEditText)
                .setPositiveButton("Create", this::onCreateSubmitClick)
                .setNegativeButton("Cancel", this::onCreateCancelClick)
                .setOnDismissListener(this::onCreateDialogDismiss);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(this.todoEntryAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton createButton = findViewById(R.id.fab);
        createButton.setOnClickListener(this::onCreateOpenClick);
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

        // Add the item to the interface
        this.todoEntryAdapter.onItemCreated(new TodoEntry(todoText));

        // Reset the dialog text now that the user created it.
        this.dialogEditText.setText("");
    }

    public void onCreateCancelClick(DialogInterface dialog, int whichButton) {
        // nop
    }
}
