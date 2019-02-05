package com.example.tp3weather.requests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

public class BitmapRequest extends Request<Bitmap> {
    private final Response.Listener<Bitmap> listener;

    public BitmapRequest(
            String url,
            Response.Listener<Bitmap> listener,
            Response.ErrorListener errorListener) {

        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(response.data, 0, response.data.length);
        return Response.success(
            bitmap, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(Bitmap response) {
        listener.onResponse(response);
    }
}
