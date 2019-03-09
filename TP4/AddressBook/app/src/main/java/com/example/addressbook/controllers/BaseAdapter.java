package com.example.addressbook.controllers;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, Model>
        extends RecyclerView.Adapter<VH> {

    public final ArrayList<Model> items = new ArrayList<>();

    @Override
    public int getItemCount() {
        return this.items.size();
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
}
