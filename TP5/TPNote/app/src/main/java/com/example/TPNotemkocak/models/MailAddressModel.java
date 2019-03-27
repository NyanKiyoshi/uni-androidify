package com.example.TPNotemkocak.models;

import java.security.InvalidParameterException;

public class MailAddressModel extends PostalAddressModel {

    public MailAddressModel() {
    }

    public MailAddressModel(int id, int personID, String type, String address)
            throws InvalidParameterException {

        super(id, personID, type, address);
    }
}
