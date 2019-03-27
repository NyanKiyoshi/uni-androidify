package com.example.TPNotemkocak.models;

public final class AppConfig {
    private static final String
            API_BASE_URL = "http://35.180.46.12:5000";
    public static final boolean IS_TESTING = true;

    public static String getURL(String endpoint) {
        return API_BASE_URL + endpoint;
    }
}
