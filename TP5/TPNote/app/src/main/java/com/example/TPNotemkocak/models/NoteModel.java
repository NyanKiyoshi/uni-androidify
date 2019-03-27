package com.example.TPNotemkocak.models;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoteModel extends BaseModel {
    private String title;
    private String body;

    public @Nullable BaseModel[] categories;
    public @Nullable ArrayList<Integer> newCategories;
    public @Nullable ArrayList<Integer> removedCategories;

    public NoteModel(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
