package com.example.TPNotemkocak.views.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.TPNotemkocak.R;

public class RemovableViewHolder extends BaseViewHolder {
    public TextView text;
    public ImageView removeBtn;

    public RemovableViewHolder(@NonNull View itemView) {
        super(itemView);

        // Retrieve text views
        this.text = itemView.findViewById(R.id.text);

        // Retrieve buttons
        this.removeBtn = itemView.findViewById(R.id.remove_btn);
    }
}
