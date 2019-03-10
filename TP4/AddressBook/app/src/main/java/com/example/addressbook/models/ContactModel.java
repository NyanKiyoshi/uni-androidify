package com.example.addressbook.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactModel extends BaseModel {
    private int id;
    private String firstName;
    private String lastName;

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

    public String getIdStr() {
        return String.valueOf(this.id);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
