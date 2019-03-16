package com.example.addressbook.views.groupManagers;

import android.util.Log;
import android.widget.Toast;

import com.example.addressbook.views.BaseChildActivity;

public abstract class BaseGroupActivity extends BaseChildActivity {

    public static final String EXTRA_ID =
            "com.example.addressbook.views.groupManagers.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.addressbook.views.groupManagers.EXTRA_TITLE";

    void onError(Exception exc) {
        Log.wtf("Failed to retrieve data", exc);
        Toast.makeText(this, exc.getMessage(), Toast.LENGTH_LONG).show();
    }

}
