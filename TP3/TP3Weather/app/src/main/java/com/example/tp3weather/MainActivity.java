package com.example.tp3weather;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
    private static final String SAVED_CITY_KEY = "last_city";

    TextView JSONCallbackView;
    ImageView IconImage;
    EditText queryInput;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.JSONCallbackView = findViewById(R.id.query_results);
        this.IconImage = findViewById(R.id.image_icon);
        this.queryInput = findViewById(R.id.query_input);

        this.requestQueue = Volley.newRequestQueue(this);
        this.progressDialog = new ProgressDialog(this);
        this.sharedPreferences = this.getSharedPreferences(
                this.getApplication().getPackageName(), MODE_PRIVATE);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this::onSubmitButtonClick);

        // Set the default input text to the latest saved input
        this.queryInput.setText(this.getSavedCity());
    }

    public String getSavedCity() {
        return this.sharedPreferences.getString(SAVED_CITY_KEY, "");
    }

    public void saveCity(String city) {
        final SharedPreferences.Editor prefEditor = this.sharedPreferences.edit();
        prefEditor.putString(SAVED_CITY_KEY, city);
        prefEditor.apply();
    }

    // Send the input query to the download manager
    public void onSubmitButtonClick(View view) {
        this.progressDialog.show();

        String userQuery = this.queryInput.getText().toString();
        WeatherQueryManager.getDataByQuery(
                this.requestQueue,
                userQuery, this::onWeatherDataReceived, this::handleError);

        // Save the query into the last used city
        this.saveCity(userQuery);
    }

    public void handleError(VolleyError error) {
        this.progressDialog.dismiss();

        if (error.networkResponse == null) {
            Toast.makeText(this, R.string.please_check_connectivity, Toast.LENGTH_SHORT).show();
        }
        else {
            switch (error.networkResponse.statusCode) {
                case 404:
                    Toast.makeText(this, R.string.no_such_city, Toast.LENGTH_SHORT).show();
                    break;
                case 401:
                    Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    this.handleException(error);
            }
        }
    }

    public void handleException(Exception exception) {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        Log.wtf("Error from weather retrieval", exception);
    }

    public void handleImageResponse(Bitmap result) {
        this.IconImage.setImageBitmap(result);
    }

    public void onWeatherDataReceived(JSONObject data) {
        this.progressDialog.dismiss();

        try {
            Log.d("Received weather data", data.toString(4));
            JSONObject weatherDescriptions = data.getJSONArray("weather").getJSONObject(0);
            JSONObject weatherData = data.getJSONObject("main");

            WeatherQueryManager.downloadIcon(
                this.requestQueue,
                weatherDescriptions, this::handleImageResponse, this::handleError);

            this.JSONCallbackView.setText(String.format(
                    "%s - %s\n%s°C (%s%% - %s kph)",
                    data.get("name"),
                    weatherDescriptions.get("description"),
                    weatherData.get("temp"),
                    weatherData.get("humidity"),
                    data.getJSONObject("wind").getDouble("speed")));
        }
        catch (Exception e) {
            this.handleException(e);
        }
    }
}
