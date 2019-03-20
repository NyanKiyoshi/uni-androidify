package com.example.addressbook.controllers;

import com.android.volley.VolleyError;

public class ErrorUtils {
    public enum EError {
        UNKNOWN,
        DUPLICATE,
    }

    public static EError parseError(Exception error) {

        if (VolleyError.class.isAssignableFrom(error.getClass())) {
            VolleyError volleyError = (VolleyError) error;

            if (volleyError.networkResponse != null) {
                switch (volleyError.networkResponse.statusCode) {
                    case 400:
                        return EError.DUPLICATE;
                    default:
                        return EError.UNKNOWN;
                }
            }
        }

        return EError.UNKNOWN;
    }
}
