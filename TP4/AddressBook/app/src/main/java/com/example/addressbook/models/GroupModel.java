package com.example.addressbook.models;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupModel extends BaseModel {
    private int id;
    private String title;

    public GroupModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public GroupModel fromJSON(JSONObject data) throws JSONException {
        this.id = data.getInt("id");
        this.title = data.getString("title");

        return this;
    }

    @Override
    public JSONObject serialize() throws JSONException {
        JSONObject data = new JSONObject();

        data.put("title", this.title);

        return data;
    }

    public int getId() {
        return this.id;
    }

    public String getIdStr() {
        return String.valueOf(this.id);
    }

    public String getTitle() {
        return this.title;
    }
}
