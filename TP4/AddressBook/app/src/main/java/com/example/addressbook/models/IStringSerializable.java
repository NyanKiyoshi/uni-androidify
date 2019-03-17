package com.example.addressbook.models;

import androidx.annotation.NonNull;

public interface IStringSerializable {
    @NonNull
    String toString();

    int getId();
}
