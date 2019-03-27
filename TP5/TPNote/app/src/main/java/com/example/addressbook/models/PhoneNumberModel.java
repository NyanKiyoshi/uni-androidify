package com.example.addressbook.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidParameterException;

public class PhoneNumberModel extends AddressModel {
    private String address;

    public PhoneNumberModel() {
    }

    public PhoneNumberModel(int id, int personID, String type, String address)
            throws InvalidParameterException {

        super(id, personID, type);
        this.address = address;
    }

    @Override
    public BaseModel fromJSON(JSONObject data) throws JSONException {
        super.fromJSON(data);
        this.address = data.getString("number");
        return this;
    }

    @Override
    public JSONObject serialize() throws JSONException {
        return super.serialize()
                .put("number", this.address);
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public void setAddress(String text) {
        this.address = text;
    }
}
