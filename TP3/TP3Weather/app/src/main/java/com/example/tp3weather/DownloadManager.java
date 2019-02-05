package com.example.tp3weather;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class DownloadManager extends AsyncTask<URL, Integer, DownloadManager.Result> {

    public class Result {
        final JSONObject body;
        final Exception exception;

        public Result(JSONObject body, Exception exception) {
            this.body = body;
            this.exception = exception;
        }
    }

    public interface JSONCallback {
        public void onJSONDataReceived(Result results);
    }

    private final JSONCallback listener;

    public DownloadManager(JSONCallback listener) {
        this.listener = listener;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(URL url) throws IOException, JSONException {
        InputStream is = url.openStream();
        try {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }

    @Override
    protected Result doInBackground(URL... urls) {
        try {
            return new Result(readJsonFromUrl(urls[0]), null);
        }
        // Exception will return null
        catch (IOException | JSONException e) {
            return new Result(null, e);
        }
    }

    protected void onPostExecute(Result result) {
        this.listener.onJSONDataReceived(result);
    }
}
