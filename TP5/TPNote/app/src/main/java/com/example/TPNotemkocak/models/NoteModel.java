package com.example.TPNotemkocak.models;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NoteModel extends BaseModel {
    private static int nextID = 0;

    private int id;
    private String title;

    private String body;

    public @Nullable ArrayList<Integer> categories;

    public NoteModel(int id, String title, String body) {
        this.title = title;
        this.body = body;
        this.id = id;
    }

    public NoteModel(String title, String body) {
        this(nextID++, title, body);
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public ArrayList<Integer> getCategories() {
        return categories;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCategories(@Nullable ArrayList<Integer> categories) {
        this.categories = categories;
    }
}
