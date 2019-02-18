package com.example.todolist.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.controllers.DatabaseWrapper;
import com.example.todolist.models.TodoEntry;

@SuppressLint("ValidFragment")
public class TodoEntryFragment extends Fragment {
    public interface OnTodoEntryAction {
        public void onEntryDeleted(View view);
    }

    private final TodoEntry todoEntry;
    private final DatabaseWrapper databaseWrapper;

    private OnTodoEntryAction deleteCallback;
    private View actualView;

    public TodoEntryFragment(TodoEntry entry, DatabaseWrapper databaseWrapper) {
        super();
        this.todoEntry = entry;
        this.databaseWrapper = databaseWrapper;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        // Create a UI fragment from the layout file
        View view = inflater.inflate(
                R.layout.card_fragment, container, false);

        TextView todoText = view.findViewById(R.id.todo_text);
        TextView todoIdentifier = view.findViewById(R.id.todo_id);
        Button deleteButton = view.findViewById(R.id.delete_btn);

        todoText.setText(this.todoEntry.text);
        todoIdentifier.setText(this.todoEntry.getIdStr());
        deleteButton.setOnClickListener(this::OnDeleteClick);

        this.actualView = view;
        return view;
    }

    public void setOnDeleteListener(OnTodoEntryAction newCallback) {
        this.deleteCallback = newCallback;
    }

    public void OnDeleteClick(View view) {
        if (this.todoEntry.deleteEntry(this.databaseWrapper) != 1) {
            // If didn't delete only one (or any) entry, it's an unexpected error, tell it.
            Toast.makeText(
                    this.getContext(), R.string.failed_deleting_entry, Toast.LENGTH_SHORT
            ).show();
        }

        this.deleteCallback.onEntryDeleted(this.actualView);
    }
}
