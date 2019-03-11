package com.example.addressbook.models;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseModel {
    public BaseModel() {

    }

    public abstract BaseModel fromJSON(JSONObject data) throws JSONException;
    public abstract JSONObject serialize() throws JSONException;

    public abstract int getId();
}
