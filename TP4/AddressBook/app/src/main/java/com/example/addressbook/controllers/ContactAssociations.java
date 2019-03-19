package com.example.addressbook.controllers;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.addressbook.controllers.http.RawJsonRequest;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.BaseModel;
import com.example.addressbook.views.SelectAddressOrNew;

import org.json.JSONException;

import java.util.ArrayList;

public final class ContactAssociations {
    public static final String EXTRA_POSTAL_TO_REMOVE =
            "com.example.addressbook.controllers.ContactAssociations.EXTRA_POSTAL_TO_REMOVE";
    public static final String EXTRA_POSTAL_TO_ADD =
            "com.example.addressbook.controllers.ContactAssociations.EXTRA_POSTAL_TO_ADD";

    private static String getPostalURL(int contactID) {
        return AppConfig.getURL("/persons/" + contactID + "/postalAddresses");
    }

    public static void applyPostalAddresses(
            SelectAddressOrNew adapter, ArrayList<Integer> toDelete, Intent intent)
            throws JSONException {

        ArrayList<String> payloads = new ArrayList<>();

        BaseModel item;
        for (Object o: adapter.items) {
            item = (BaseModel) o;
            if (item.getId() < 1) {
                payloads.add(item.serialize().toString());
            }
        }
        intent.putExtra(EXTRA_POSTAL_TO_ADD, payloads);
        intent.putStringArrayListExtra(EXTRA_POSTAL_TO_ADD, payloads);
        intent.putIntegerArrayListExtra(EXTRA_POSTAL_TO_REMOVE, toDelete);
    }

    public static void createPostal(
            RequestQueue requestQueue,
            String payload, int contactID,
            Response.Listener<String> listener,
            @Nullable Response.ErrorListener errorListener) {

        requestQueue.add(new RawJsonRequest(
                Request.Method.POST, getPostalURL(contactID),
                payload,
                listener, errorListener));
    }

    public static void deletePostal(
            RequestQueue requestQueue,
            int postalID, int contactID,
            Response.Listener<String> listener,
            @Nullable Response.ErrorListener errorListener) {

        requestQueue.add(new StringRequest(
                Request.Method.DELETE, getPostalURL(contactID) + "/" + postalID,
                listener, errorListener));
    }
}
