package com.example.addressbook.views.contactManagers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.addressbook.R;

public class ViewContactActivity extends BaseContactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        TextView textViewFirstname = findViewById(R.id.edit_text_firstname);
        TextView textViewLastname = findViewById(R.id.edit_text_lastname);

        // Get the activity's intent object
        final Intent intent = getIntent();
        setTitle("Viewing Contact");

        textViewFirstname.setText(intent.getStringExtra(EXTRA_FIRSTNAME));
        textViewLastname.setText(intent.getStringExtra(EXTRA_LASTNAME));
    }
}
