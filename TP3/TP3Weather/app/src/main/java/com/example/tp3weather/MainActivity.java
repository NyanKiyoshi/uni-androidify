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

import com.example.tp3weather.downloadManagers.BaseDownloadManager;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView JSONCallbackView;
    ImageView IconImage;
    EditText queryInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.JSONCallbackView = findViewById(R.id.query_results);
        this.IconImage = findViewById(R.id.image_icon);
        this.queryInput = findViewById(R.id.query_input);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this::onSubmitButtonClick);
    }

    // Send the input query to the download manager
    public void onSubmitButtonClick(View view) {
        String userQuery = this.queryInput.getText().toString();
        WeatherQueryManager.getDataByQuery(
                userQuery, this::onWeatherDataReceived);
    }

    public void handleError(Exception exception) {
        int errorMessage;

        if (exception == null) {
            exception = new UnsupportedOperationException();
        }
        if (exception.getClass() == java.io.FileNotFoundException.class) {
            errorMessage = R.string.no_such_city;
        }
        else {
            errorMessage = R.string.please_check_connectivity;
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        Log.wtf("Error from weather retrieval", exception);
    }

    public void handleImageResponse(BaseDownloadManager.Result result) {
        this.IconImage.setImageBitmap((Bitmap)result.results);
    }

    public void onWeatherDataReceived(BaseDownloadManager.Result results) {
        Exception exception = results.exception;
        JSONObject data = (JSONObject)results.results;

        if (exception == null && data != null) {
            try {
                Log.d("HH", data.toString(4));
                JSONObject weatherDescriptions = data.getJSONArray("weather").getJSONObject(0);
                JSONObject weatherData = data.getJSONObject("main");

                WeatherQueryManager.downloadIcon(
                    weatherDescriptions, this::handleImageResponse);

                this.JSONCallbackView.setText(String.format(
                        "%s\n%s°C (%s%%)",
                        weatherDescriptions.get("description"),
                        weatherData.get("temp"),
                        weatherData.get("humidity")));

                return;
            }
            catch (Exception e) {
                exception = e;
            }
        }

        this.handleError(exception);
    }
}
