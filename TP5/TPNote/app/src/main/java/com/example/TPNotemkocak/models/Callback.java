package com.example.TPNotemkocak.models;

public interface Callback<T> {
    void onSuccess(T response);
    void onError(Exception error);
}
