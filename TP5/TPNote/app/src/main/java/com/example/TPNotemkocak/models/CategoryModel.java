package com.example.TPNotemkocak.models;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class CategoryModel extends BaseModel {
    private static int nextPos = 0;
    private final static CategoryModel[] availableCategories =
            new CategoryModel[] {
                    new CategoryModel("Études"),
                    new CategoryModel("Loisirs"),
                    new CategoryModel("Important"),
                    new CategoryModel("Urgent"),
            };

    private int id;
    private String title;

    public CategoryModel() {
        this.id = nextPos++;
    }

    public CategoryModel(String title) {
        this();
        this.title = title;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }

    public static CategoryModel[] getAvailableCategories() {
        return availableCategories;
    }
}
