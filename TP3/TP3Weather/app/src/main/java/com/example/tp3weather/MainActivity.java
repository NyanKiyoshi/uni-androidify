package com.example.tp3weather;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity implements DownloadManager.DownloadCallback {
    TextView JSONCallbackView;
    ImageView IconImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.JSONCallbackView = findViewById(R.id.query_results);
        this.IconImage = findViewById(R.id.image_icon);

        final EditText queryInput = findViewById(R.id.query_input);
        Button submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the input query to the download manager
                WeatherQueryManager.getDataByQuery(
                        queryInput.getText().toString(), (MainActivity)v.getContext());
            }
        });
    }

    public void handleError(Exception exception) {
        int errorMessage;

        if (exception.getClass() == java.io.FileNotFoundException.class) {
            errorMessage = R.string.no_such_city;
        }
        else {
            errorMessage = R.string.please_check_connectivity;
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        Log.wtf("Error from weather retrieval", exception);
    }

    public void handleJSONResponse(JSONObject data) throws Exception {
        Log.d("HH", data.toString(4));
        JSONObject weatherDescriptions = data.getJSONArray("weather").getJSONObject(0);
        JSONObject weatherData = data.getJSONObject("main");

        WeatherQueryManager.downloadIcon(weatherDescriptions, this);

        this.JSONCallbackView.setText(String.format(
                "%s\n%s°C (%s%%)",
                weatherDescriptions.get("description"),
                weatherData.get("temp"),
                weatherData.get("humidity")));
    }

    public void handleImageResponse(Bitmap bitmap) {
        this.IconImage.setImageBitmap(bitmap);
    }

    @Override
    public void onDownloadDone(DownloadManager.Result results) {
        Exception exception = results.exception;

        if (exception == null) {
            try {
                if (results.body != null) {
                    this.handleJSONResponse(results.body);
                }
                else if (results.bitmap != null) {
                    this.handleImageResponse(results.bitmap);
                }

                return;
            }
            catch (Exception e) {
                exception = e;
            }
        }

        this.handleError(exception);
    }
}
