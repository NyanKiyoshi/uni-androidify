package com.example.addressbook.models;

public class ContactModel {
    private int id;
    private String firstName;
    private String lastName;

    public ContactModel(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
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
