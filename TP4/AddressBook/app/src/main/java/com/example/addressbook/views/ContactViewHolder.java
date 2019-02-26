package com.example.addressbook.views;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    // Text views
    public TextView id;
    public TextView firstname;
    public TextView lastname;

    // Buttons
    public Button deleteButton;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);

        // Retrieve text views
        this.id = itemView.findViewById(R.id.id);
        this.firstname = itemView.findViewById(R.id.firstname);
        this.lastname = itemView.findViewById(R.id.lastname);

        // Retrieve buttons
        this.deleteButton = itemView.findViewById(R.id.delete_btn);

        // Set listeners
        this.deleteButton.setOnClickListener(this::OnDeleteClick);
    }

    void OnDeleteClick(View view) {
        throw new UnsupportedOperationException();
    }
}
