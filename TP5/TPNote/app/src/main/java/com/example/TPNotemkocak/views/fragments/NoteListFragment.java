package com.example.TPNotemkocak.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.TPNotemkocak.R;
import com.example.TPNotemkocak.controllers.adapters.NoteAdapter;
import com.example.TPNotemkocak.models.NoteModel;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_DELETED) {
            // TODO
        }
    }

    public void onSelectItem(NoteModel item, int pos) {

    }

    public void onAddNewItem(View view) {

    }
}
