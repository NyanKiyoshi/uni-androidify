package com.example.addressbook.models;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactModel extends BaseModel {
    private int id;
    private String firstName;
    private String lastName;

    private @Nullable String picturePath;
    private @Nullable SharedPreferences sharedPreferences;

    public ContactModel() {

    }

    public ContactModel(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ContactModel fromJSON(JSONObject data) throws JSONException {
        this.id = data.getInt("id");
        this.firstName = data.getString("firstname");
        this.lastName = data.getString("lastname");

        return this;
    }

    public JSONObject serialize() throws JSONException {
        JSONObject data = new JSONObject();

        data.put("firstname", this.firstName);
        data.put("lastname", this.lastName);

        return data;
    }

    public int getId() {
        return this.id;
    }

    public void save() {
        if (this.sharedPreferences == null
                || this.picturePath == null
                || this.picturePath.isEmpty()
                || this.id < 0) {

            return;
        }

        final SharedPreferences.Editor prefEditor = this.sharedPreferences.edit();
        prefEditor.putString(this.getIdStr(), this.picturePath);
        prefEditor.apply();
    }

    public String getIdStr() {
        return String.valueOf(this.id);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public @Nullable
    String getPicturePath() {
        if (this.sharedPreferences != null) {
            this.picturePath = this.sharedPreferences.getString(this.getIdStr(), null);
        }
        return picturePath;
    }

    public void setPicturePath(@Nullable String picturePath) {
        this.picturePath = picturePath;
    }

    public void setSharedPreferences(@Nullable SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setID(int id) {
        this.id = id;
    }
}
