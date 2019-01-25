package com.example.androidtp1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.security.acl.Group;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "GLOBAL_DATA";

    private Context context;
    private SharedPreferences sharedPreferences;
    private ViewGroup formContainer;

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void SaveFormData() {
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        EditText childInputField = null;

        for( int i = 0; i < formContainer.getChildCount(); ++i ) {
            if (formContainer.getChildAt(i) instanceof EditText) {
                childInputField = (EditText) formContainer.getChildAt(i);
                prefEditor.putString(
                        "" + childInputField.getId(),
                        childInputField.getText().toString());
            }
        }

        prefEditor.apply();
    }

    private void RestoreSavedData() {
        int fieldIdentifier;
        EditText textField;
        Object savedTextData;

        Map<String, ?> savedData = sharedPreferences.getAll();
        for (String identifier : savedData.keySet()) {
            try {
                fieldIdentifier = Integer.parseInt(identifier);
                textField = findViewById(fieldIdentifier);
                savedTextData = savedData.get(identifier);

                if (textField == null || savedTextData == null) {
                    continue;
                }

                textField.setText(savedTextData.toString());
            }
            catch (NumberFormatException ignored) {}
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        sharedPreferences = getSharedPreferences(context);
        formContainer = findViewById(R.id.form_container);

        final Intent userDetailsView = new Intent(this, UserDetailsActivity.class);
        final Button cancelButton = findViewById(R.id.cancel_button);
        final Button submitButton = findViewById(R.id.submit_button);

        RestoreSavedData();

        // Set the cancel button to close the activity
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // Set the submit button to save the input and open the summary view
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SaveFormData();
                startActivity(userDetailsView);
            }
        });
    }
}
