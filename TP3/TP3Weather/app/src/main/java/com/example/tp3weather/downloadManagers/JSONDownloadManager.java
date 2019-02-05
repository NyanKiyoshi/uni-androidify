package com.example.tp3weather.downloadManagers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class JSONDownloadManager extends BaseDownloadManager {
    public JSONDownloadManager(DownloadCallback listener) {
        super(listener);
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    protected Object processDownloaded(URLConnection urlConnection, InputStream inputStream
    ) throws Exception {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        return new JSONObject(jsonText);
    }
}
