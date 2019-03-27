package com.example.TPNotemkocak.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.TPNotemkocak.R;
import com.example.TPNotemkocak.models.CategoryModel;
import com.example.TPNotemkocak.views.BaseChildActivity;

public class ViewCategoryActivity extends BaseChildActivity {

    public static final String EXTRA_ID =
            ".groupManagers.EXTRA_ID";
    public static final String EXTRA_TITLE =
            ".groupManagers.EXTRA_TITLE";

    private static final int REQUEST_SELECT_CONTACT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        TextView textViewTitle = findViewById(R.id.edit_text_title);

        // Get the activity's intent object
        final Intent intent = getIntent();
        setTitle("Viewing Category");

        // Create the item model object
        CategoryModel categoryModel = CategoryModel
                .getAvailableCategories()[intent.getIntExtra(EXTRA_ID, -1)];

        textViewTitle.setText(categoryModel.getTitle());
    }
}
