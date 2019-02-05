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

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView JSONCallbackView;
    ImageView IconImage;
    EditText queryInput;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.JSONCallbackView = findViewById(R.id.query_results);
        this.IconImage = findViewById(R.id.image_icon);
        this.queryInput = findViewById(R.id.query_input);

        this.requestQueue = Volley.newRequestQueue(this);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this::onSubmitButtonClick);
    }

    // Send the input query to the download manager
    public void onSubmitButtonClick(View view) {
        String userQuery = this.queryInput.getText().toString();
        WeatherQueryManager.getDataByQuery(
                this.requestQueue,
                userQuery, this::onWeatherDataReceived, this::handleError);
    }

    public void handleError(VolleyError error) {
        this.handleError(error.getCause());
    }

    public void handleError(Throwable throwable) {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        Log.wtf("Error from weather retrieval", throwable);
    }

    public void handleImageResponse(Bitmap result) {
        this.IconImage.setImageBitmap(result);
    }

    public void onWeatherDataReceived(JSONObject data) {
        try {
            Log.d("HH", data.toString(4));
            JSONObject weatherDescriptions = data.getJSONArray("weather").getJSONObject(0);
            JSONObject weatherData = data.getJSONObject("main");

            WeatherQueryManager.downloadIcon(
                this.requestQueue,
                weatherDescriptions, this::handleImageResponse, this::handleError);

            this.JSONCallbackView.setText(String.format(
                    "%s\n%s°C (%s%%)",
                    weatherDescriptions.get("description"),
                    weatherData.get("temp"),
                    weatherData.get("humidity")));
        }
        catch (Exception e) {
            this.handleError(e);
        }
    }
}
