package com.example.tp3weather;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class WeatherQueryManager {
    public static String getEndpointURL(String resourceEndpoint) {
        return String.format(
            "%s%s?units=%s&appid=%s",
            Settings.OPEN_WEATHER_BASE_URL,
            resourceEndpoint,
            Settings.OPEN_WEATHER_UNITS,
            Settings.OPEN_WEATHER_API_KEY);
    }

    public static AsyncTask<URL, Integer, DownloadManager.Result>
            getDataByQuery(String query, DownloadManager.JSONCallback listener) {

        String url = getEndpointURL("weather") + "&q=" + query;

        try {
            return new DownloadManager(listener).execute(new URL(url));
        }
        catch (MalformedURLException e) {
            return null;
        }

    }
}
