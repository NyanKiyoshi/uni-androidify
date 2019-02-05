package com.example.tp3weather.downloadManagers;

import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public abstract class BaseDownloadManager
        extends AsyncTask<URL, Void, BaseDownloadManager.Result> {

    public static class Result {
        public final Object results;
        public final Exception exception;

        public Result(Object results) {
            this.results = results;
            this.exception = null;
        }

        public Result(Exception exception) {
            this.results = null;
            this.exception = exception;
        }
    }

    public interface DownloadCallback {
        public void onDownloadDone(Result results);
    }

    private final DownloadCallback listener;

    public BaseDownloadManager(DownloadCallback listener) {
        this.listener = listener;
    }

    protected abstract Object processDownloaded(
            URLConnection urlConnection, InputStream inputStream
    ) throws Exception;

    public Result downloadURL(URL url) throws Exception {
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();

        try {
            return new Result(this.processDownloaded(urlConnection, inputStream));
        }
        finally {
            inputStream.close();
        }
    }

    @Override
    protected Result doInBackground(URL... urls) {
        try {
            return downloadURL(urls[0]);
        }
        // Exception will return null
        catch (Exception e) {
            return new Result(e);
        }
    }

    protected void onPostExecute(Result result) {
        this.listener.onDownloadDone(result);
    }
}
