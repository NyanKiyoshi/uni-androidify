package com.example.TPNotemkocak.controllers.http;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import java.io.UnsupportedEncodingException;

public class RawJsonRequest extends JsonRequest<String> {

    /**
     * Creates a new request.
     *
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonPayload A {@link String} to post with the request. Null indicates no
     *     parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public RawJsonRequest(
            int method,
            String url,
            @Nullable String jsonPayload,
            Response.Listener<String> listener,
            @Nullable Response.ErrorListener errorListener) {
        super(
                method,
                url,
                jsonPayload,
                listener,
                errorListener);
    }

    @Override
    @SuppressWarnings("DefaultCharset")
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            // Since minSdkVersion = 8, we can't call
            // new String(response.data, Charset.defaultCharset())
            // So suppress the warning instead.
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}