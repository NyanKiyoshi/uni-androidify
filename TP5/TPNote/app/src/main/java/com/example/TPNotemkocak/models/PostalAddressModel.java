package com.example.TPNotemkocak.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidParameterException;

public class PostalAddressModel extends AddressModel {
    private String address;

    public PostalAddressModel() {
    }

    public PostalAddressModel(int id, int personID, String type, String address)
            throws InvalidParameterException {

        super(id, personID, type);
        this.address = address;
    }

    @Override
    public BaseModel fromJSON(JSONObject data) throws JSONException {
        super.fromJSON(data);
        this.address = data.getString("address");
        return this;
    }

    @Override
    public JSONObject serialize() throws JSONException {
        return super.serialize()
                .put("address", this.address);
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
