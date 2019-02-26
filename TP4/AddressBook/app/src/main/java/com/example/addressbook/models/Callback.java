package com.example.addressbook.models;

public interface Callback<T> {
    void onSuccess(T response);
    void onError(Exception error);
}
