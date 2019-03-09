package com.example.addressbook.models;

public class GroupModel {
    private int id;
    private String title;

    public GroupModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getIdStr() {
        return String.valueOf(this.id);
    }

    public String getTitle() {
        return this.title;
    }
}
