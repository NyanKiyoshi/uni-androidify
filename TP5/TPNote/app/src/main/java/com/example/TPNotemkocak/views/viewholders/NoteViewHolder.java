package com.example.TPNotemkocak.views.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.TPNotemkocak.R;

public class NoteViewHolder extends BaseViewHolder {
    // Text views
    public TextView title;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        // Retrieve the text views
        this.title = itemView.findViewById(R.id.title);
    }
}
