package com.example.tp3weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements DownloadManager.JSONCallback {
    TextView JSONCallbackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final MainActivity listener = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.JSONCallbackView = findViewById(R.id.query_results);

        final EditText queryInput = findViewById(R.id.query_input);
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherQueryManager.getDataByQuery(
                        queryInput.getText().toString(), listener);
            }
        });
    }

    public void handleError(Exception exception) {
        String errorMessage;

        if (exception.getClass() == java.io.FileNotFoundException.class) {
            errorMessage = "No such city";
        }
        else {
            errorMessage = "Please check your connectivity.";
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        Log.wtf("Error from weather retrieval", exception);
    }

    @Override
    public void onJSONDataReceived(DownloadManager.Result results) {
        Exception exception = results.exception;

        if (exception == null) {
            try {
                this.JSONCallbackView.setText(results.body.toString(4));
                return;
            } catch (Exception e) {
                exception = e;
            }
        }

        this.handleError(exception);
    }
}
