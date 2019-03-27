package com.example.TPNotemkocak.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.TPNotemkocak.R;
import com.example.TPNotemkocak.controllers.ViewUtils;
import com.example.TPNotemkocak.controllers.adapters.NoteAdapter;
import com.example.TPNotemkocak.models.NoteModel;
import com.example.TPNotemkocak.views.activities.AddEditNoteActivity;
import com.example.TPNotemkocak.views.viewholders.NoteViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.TPNotemkocak.controllers.ViewUtils.RESULT_DELETED;

public class NoteListFragment
        extends BaseRecyclerFragment<NoteModel, NoteViewHolder>
        implements NoteAdapter.IHasContext {

    public NoteListFragment() {
        // Create the view adapter
        this.adapter = new NoteAdapter(this, this::onSelectItem);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Register an handler to the floating button
        final FloatingActionButton fab = view.findViewById(R.id.create_fab);
        fab.setOnClickListener(this::onAddNewItem);

        return view;
    }

    public NoteModel getEntry(int id) {
        for (NoteModel note : this.adapter.items) {
            if (note.getId() == id) {
                return note;
            }
        }

        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        int itemID = data.getIntExtra(ViewUtils.EXTRA_ID, -1);

        if (resultCode == RESULT_DELETED) {
            this.adapter.removeItem(itemID);
            return;
        }

        String newTitle = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
        String newBody = data.getStringExtra(AddEditNoteActivity.EXTRA_BODY);

        if (resultCode == AddEditNoteActivity.RESULT_CREATED) {
            this.adapter.addItem(new NoteModel(newTitle, newBody));
        } else if (resultCode == AddEditNoteActivity.RESULT_UPDATED) {
            NoteModel entry = this.getEntry(itemID);
            entry.setTitle(newTitle);
            entry.setBody(newBody);
            this.adapter.changedItem(entry);
        }
    }

    public void onSelectItem(NoteModel item, int pos) {
        Intent intent = new Intent(this.context, AddEditNoteActivity.class);
        intent.putExtra(ViewUtils.EXTRA_ID, item.getId());
        intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, item.getTitle());
        intent.putExtra(AddEditNoteActivity.EXTRA_BODY, item.getBody());
        this.startActivityForResult(intent, 1);
    }

    public void onAddNewItem(View view) {
        Intent intent = new Intent(this.context, AddEditNoteActivity.class);
        this.startActivityForResult(intent, 1);
    }
}
