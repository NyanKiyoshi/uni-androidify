package com.example.TPNotemkocak.views.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.TPNotemkocak.R;

public class CategoryViewHolder extends BaseViewHolder {
    // Text views
    public TextView title;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        // Retrieve text views
        this.title = itemView.findViewById(R.id.title);
    }
}
