package com.example.TPNotemkocak.views.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.TPNotemkocak.R;

public class ContactViewHolder extends BaseViewHolder {
    // Text views
    public TextView id;
    public TextView firstname;
    public TextView lastname;

    public ImageView pictureBox;
    public ImageView removeBtn;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);

        // Retrieve text views
        this.id = itemView.findViewById(R.id.id);
        this.firstname = itemView.findViewById(R.id.firstname);
        this.lastname = itemView.findViewById(R.id.lastname);

        // Retrieve the picture box
        this.pictureBox = itemView.findViewById(R.id.picture);

        // Retrieve buttons
        this.removeBtn = itemView.findViewById(R.id.remove_btn);
    }
}
