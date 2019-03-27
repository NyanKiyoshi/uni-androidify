package com.example.addressbook.views.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.addressbook.R;

public class GroupViewHolder extends BaseViewHolder {
    // Text views
    public TextView id;
    public TextView title;

    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);

        // Retrieve text views
        this.id = itemView.findViewById(R.id.id);
        this.title = itemView.findViewById(R.id.title);
    }
}
