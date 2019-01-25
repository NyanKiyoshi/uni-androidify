package com.example.androidtp1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity {
    private Context context;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        sharedPreferences = MainActivity.getSharedPreferences(context);

        setContentView(R.layout.activity_user_details);

        final TextView summaryTextView = findViewById(R.id.results_text_box);
        final StringBuilder summary = new StringBuilder();

        Map<String, ?> savedData = sharedPreferences.getAll();
        for (Object value : savedData.values()) {
            summary.append(value.toString()).append("\n");
        }

        summaryTextView.setText(summary);
    }
}
