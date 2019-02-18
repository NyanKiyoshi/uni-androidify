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

import com.example.todolist.R;
import com.example.todolist.models.TodoEntry;

@SuppressLint("ValidFragment")
public class TodoEntryFragment extends Fragment {
    public interface OnTodoEntryAction {
        public void onEntryDeleted(View view);
    }

    TodoEntry todoEntry;
    OnTodoEntryAction deleteCallback;
    View actualView;

    public TodoEntryFragment(TodoEntry entry) {
        super();
        this.todoEntry = entry;
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
        Button deleteButton = view.findViewById(R.id.delete_btn);

        todoText.setText(String.format(
                "%d: %s", this.todoEntry.id, this.todoEntry.text));
        deleteButton.setOnClickListener(this::OnDeleteClick);

        this.actualView = view;
        return view;
    }

    public void setOnDeleteListener(OnTodoEntryAction newCallback) {
        this.deleteCallback = newCallback;
    }

    public void OnDeleteClick(View view) {
        this.deleteCallback.onEntryDeleted(this.actualView);
    }
}
