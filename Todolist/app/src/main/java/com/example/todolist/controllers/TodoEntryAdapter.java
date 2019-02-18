package com.example.todolist.controllers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.models.TodoEntry;

import java.util.ArrayList;
import java.util.List;

public class TodoEntryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final DatabaseWrapper database;
    private List<TodoEntry> items;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView text;
        Button deleteButton;

        public interface OnTodoEntryAction {
            void onEntryDeleted(int position);
        }

        private OnTodoEntryAction deleteCallback;

        ViewHolder(View itemView) {
            super(itemView);

            this.id = itemView.findViewById(R.id.todo_id);
            this.text = itemView.findViewById(R.id.todo_text);
            this.deleteButton = itemView.findViewById(R.id.delete_btn);

            this.deleteButton.setOnClickListener(this::OnDeleteClick);
        }

        void OnDeleteClick(View view) {
            this.deleteCallback.onEntryDeleted(this.getAdapterPosition());
        }
    }

    public TodoEntryAdapter(DatabaseWrapper databaseWrapper) {
        super();

        this.database = databaseWrapper;

        this.items = new ArrayList<>();
        this.items.addAll(TodoEntry.getEntries(databaseWrapper));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(
                        R.layout.card_fragment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder baseHolder, int position) {
        final ViewHolder holder = (ViewHolder) baseHolder;
        final TodoEntry item = items.get(position);

        holder.id.setText(item.getIdStr());
        holder.text.setText(item.text);

        holder.deleteCallback = this::onItemDeleted;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    private void onItemDeleted(int position) {
        final TodoEntry item = this.items.remove(position);
        item.deleteEntry(this.database);
        notifyItemRemoved(position);
    }

    public void onItemCreated(TodoEntry item) {
        item.createEntry(this.database);
        this.items.add(0, item);
        notifyItemInserted(0);
    }
}
