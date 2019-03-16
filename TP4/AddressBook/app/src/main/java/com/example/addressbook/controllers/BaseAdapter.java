package com.example.addressbook.controllers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.views.viewholders.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class BaseAdapter<VH extends BaseViewHolder, Model>
        extends RecyclerView.Adapter<VH> {

    public interface OnItemClickEvent<Model> {
        void onItemClick(Model item, int position);
    }

    private OnItemClickEvent<Model> listener;
    public final ArrayList<Model> items = new ArrayList<>();

    public BaseAdapter(OnItemClickEvent<Model> listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void addItem(Model modelItem) {
        this.items.add(modelItem);
        this.notifyItemInserted(this.items.size());
    }
    public void removeItem(int pos) {
        this.items.remove(pos);
        this.notifyItemRemoved(pos);
    }

    public void addItems(Model[] modelItems) {
        if (modelItems.length == 0) {
            return;
        }

        int startpos = this.items.size();
        this.items.addAll(Arrays.asList(modelItems));
        this.notifyItemRangeInserted(startpos, modelItems.length);
    }

    public void clear() {
        if (this.items.isEmpty()) {
            return;
        }

        int removedCount = this.items.size();
        this.items.clear();
        this.notifyItemRangeRemoved(0, removedCount);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        this.setOnClickListener(holder, this.listener);
    }

    private void setOnClickListener(@NonNull VH holder, OnItemClickEvent<Model> listener) {
        holder.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(items.get(position), position);
            }
        });
    }
}
