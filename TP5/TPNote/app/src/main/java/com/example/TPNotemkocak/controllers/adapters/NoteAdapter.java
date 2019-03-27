package com.example.TPNotemkocak.controllers.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.example.TPNotemkocak.R;
import com.example.TPNotemkocak.models.NoteModel;
import com.example.TPNotemkocak.views.viewholders.NoteViewHolder;

public class NoteAdapter extends BaseAdapter<NoteViewHolder, NoteModel> {
    private final Handler handler = new Handler();

    public interface IHasContext {
        Context getContext();
    }

    private final IHasContext parent;

    public NoteAdapter(IHasContext parent, OnItemClickEvent<NoteModel> listener) {
        super(listener);
        this.parent = parent;
    }

    @NonNull
    public NoteViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType, @LayoutRes int layoutID) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(layoutID, parent, false);
        return new NoteViewHolder(v);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return this.onCreateViewHolder(parent, viewType, R.layout.note_entry);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        final NoteModel item = this.items.get(position);
        holder.title.setText(item.getTitle());
    }
}
