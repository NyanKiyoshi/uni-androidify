package com.example.TPNotemkocak.views.fragments;

import android.content.Intent;

import com.example.TPNotemkocak.controllers.adapters.CategoryAdapter;
import com.example.TPNotemkocak.models.CategoryModel;
import com.example.TPNotemkocak.views.activities.ViewCategoryActivity;
import com.example.TPNotemkocak.views.viewholders.CategoryViewHolder;

public class CategoryListFragment extends BaseRecyclerFragment<CategoryModel, CategoryViewHolder> {

    public CategoryListFragment() {
        // Create the view adapter
        this.adapter = new CategoryAdapter(this::onSelectItem);
        this.adapter.addItems(CategoryModel.getAvailableCategories());
    }

    public void onSelectItem(CategoryModel item, int pos) {
        Intent intent = new Intent(this.context, ViewCategoryActivity.class);
        intent.putExtra(ViewCategoryActivity.EXTRA_ID, item.getId());
        this.startActivityForResult(intent, 1);
    }
}
