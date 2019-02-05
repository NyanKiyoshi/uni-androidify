package com.example.tp3weather;

import com.example.tp3weather.downloadManagers.BaseDownloadManager;
import com.example.tp3weather.downloadManagers.BitmapDownloadManager;
import com.example.tp3weather.downloadManagers.JSONDownloadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

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

    public static void getDataByQuery(String query, BaseDownloadManager.DownloadCallback listener) {
        String url = getEndpointURL("weather") + "&q=" + query;

        try {
            new JSONDownloadManager(listener).execute(new URL(url));
        }
        catch (MalformedURLException ignored) {
        }

    }

    public static void downloadIcon(JSONObject data, BaseDownloadManager.DownloadCallback listener)
            throws JSONException, MalformedURLException {

        String icon = data.getString("icon");
        String url = BASE_ICON_URL + icon + ".png";
        new BitmapDownloadManager(listener).execute(new URL(url));
    }
}
