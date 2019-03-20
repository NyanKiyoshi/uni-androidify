package com.example.addressbook.models;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidParameterException;

public abstract class AddressModel extends BaseModel {
    public static final String[] TYPES = {"work", "home"};

    private int id;
    private int personID;

    private String type;

    public AddressModel() {
    }

    public AddressModel(int id, int personID, String type)
            throws InvalidParameterException {

        this.id = id;
        this.personID = personID;
        this.setType(type);
    }

    @Override
    public BaseModel fromJSON(JSONObject data) throws JSONException {
        this.id = data.getInt("id");
        this.personID = data.getInt("PersonId");
        this.type = data.getString("type");
        return this;
    }

    @Override
    public JSONObject serialize() throws JSONException {
        JSONObject data = new JSONObject();

        data.put("id", this.id);
        data.put("PersonId", this.personID);
        data.put("type", this.type);

        return data;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public void setType(String type) {
        boolean isValid = false;
        for (String t : TYPES) {
            if (t.equals(type)) {
                isValid = true;
            }
        }

        if (!isValid) {
            throw new InvalidParameterException("Invalid type: " + type);
        }

        this.type = type;
    }

    public abstract String getAddress();
    public abstract void setAddress(String text);

    @NonNull
    @Override
    public String toString() {
        return String.format("%s (%s)", this.getAddress(), this.type);
    }
}
