package com.example.TPNotemkocak.views.fragments;

import com.example.TPNotemkocak.controllers.adapters.CategoryAdapter;
import com.example.TPNotemkocak.models.CategoryModel;
import com.example.TPNotemkocak.views.viewholders.CategoryViewHolder;

public class CategoryListFragment extends BaseRecyclerFragment<CategoryModel, CategoryViewHolder> {

    public CategoryListFragment() {
        // Create the view adapter
        this.adapter = new CategoryAdapter(this::onSelectItem);
    }

    public void onSelectItem(CategoryModel item, int pos) {

    }
}
