package com.example.TPNotemkocak.controllers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.TPNotemkocak.R;
import com.example.TPNotemkocak.models.CategoryModel;
import com.example.TPNotemkocak.views.viewholders.CategoryViewHolder;

public class CategoryAdapter extends BaseAdapter<CategoryViewHolder, CategoryModel> {
    public CategoryAdapter(OnItemClickEvent<CategoryModel> listener) {
        super(listener);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(
                R.layout.category_entry, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.title.setText(this.items.get(position).getTitle());
    }
}
