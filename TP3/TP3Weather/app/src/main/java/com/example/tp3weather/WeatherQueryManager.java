package com.example.tp3weather;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tp3weather.requests.BitmapRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherQueryManager {
    private static final String BASE_ICON_URL = "http://openweathermap.org/img/w/";

    public static String getEndpointURL(String resourceEndpoint) {
        return String.format(
            "%s%s?units=%s&appid=%s",
            Settings.OPEN_WEATHER_BASE_URL,
            resourceEndpoint,
            Settings.OPEN_WEATHER_UNITS,
            Settings.OPEN_WEATHER_API_KEY);
    }

    public static void getDataByQuery(
            RequestQueue requestQueue,
            String query,
            Response.Listener<JSONObject> listener,
            @Nullable Response.ErrorListener errorListener) {

        String url = getEndpointURL("weather") + "&q=" + query;
        requestQueue.add(new JsonObjectRequest(url, null, listener, errorListener));
    }

    public static void downloadIcon(
            RequestQueue requestQueue,
            JSONObject data,
            Response.Listener<Bitmap> listener,
            @Nullable Response.ErrorListener errorListener) throws JSONException {

        String url = BASE_ICON_URL + data.getString("icon") + ".png";
        requestQueue.add(new BitmapRequest(url, listener, errorListener));
    }
}
