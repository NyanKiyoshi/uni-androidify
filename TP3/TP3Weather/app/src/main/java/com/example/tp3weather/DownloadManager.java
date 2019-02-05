package com.example.tp3weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class DownloadManager extends AsyncTask<URL, Integer, DownloadManager.Result> {

    public static class Result {
        public JSONObject body = null;
        public Bitmap bitmap = null;
        public Exception exception = null;

        public Result(JSONObject body) {
            this.body = body;
        }

        public Result(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public Result(Exception exception) {
            this.exception = exception;
        }
    }

    public interface DownloadCallback {
        public void onDownloadDone(Result results);
    }

    private final DownloadCallback listener;

    public DownloadManager(DownloadCallback listener) {
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

    public static Result downloadJSON(InputStream inputStream) throws IOException, JSONException {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        return new Result(new JSONObject(jsonText));
    }

    public static Result downloadImage(InputStream inputStream) {
        return new Result(BitmapFactory.decodeStream(inputStream));
    }

    public static Result downloadURL(URL url) throws IOException, JSONException {
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        String contentType = urlConnection.getContentType().split(";")[0];
        try {
            switch (contentType) {
                case "application/json":
                    return downloadJSON(inputStream);
                case "image/x-icon":
                case "image/png":
                    return downloadImage(inputStream);
                default:
                    return new Result(
                            new UnsupportedOperationException(
                                    urlConnection.getContentType()));
            }
        } finally {
            inputStream.close();
        }
    }

    @Override
    protected Result doInBackground(URL... urls) {
        try {
            return downloadURL(urls[0]);
        }
        // Exception will return null
        catch (IOException | JSONException e) {
            return new Result(e);
        }
    }

    protected void onPostExecute(Result result) {
        this.listener.onDownloadDone(result);
    }
}
