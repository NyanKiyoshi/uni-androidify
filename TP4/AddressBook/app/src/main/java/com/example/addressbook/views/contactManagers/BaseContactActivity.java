package com.example.addressbook.views.contactManagers;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.addressbook.R;

public abstract class BaseContactActivity extends AppCompatActivity {

    public static final String EXTRA_ID =
            "com.example.addressbook.views.contactManagers.EXTRA_ID";
    public static final String EXTRA_FIRSTNAME =
            "com.example.addressbook.views.contactManagers.EXTRA_FIRSTNAME";
    public static final String EXTRA_LASTNAME =
            "com.example.addressbook.views.contactManagers.EXTRA_LASTNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
