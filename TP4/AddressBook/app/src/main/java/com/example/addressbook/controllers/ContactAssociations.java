package com.example.addressbook.controllers;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.addressbook.controllers.adapters.ContactAdapter;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.PostalAddressModel;

import org.json.JSONException;
import org.json.JSONObject;

public final class ContactAssociations {
    public static final String EXTRA_POSTAL_TO_REMOVE =
            "com.example.addressbook.controllers.ContactAssociations.EXTRA_POSTAL_TO_REMOVE";
    public static final String EXTRA_POSTAL_TO_ADD =
            "com.example.addressbook.controllers.ContactAssociations.EXTRA_POSTAL_TO_ADD";

    private static String getPostalURL(int contactID) {
        return AppConfig.getURL("/persons/" + contactID + "/postalAddresses");
    }

    public static void createPostal(
            RequestQueue requestQueue,
            PostalAddressModel address, int contactID,
            Response.Listener<JSONObject> listener,
            @Nullable Response.ErrorListener errorListener) throws JSONException {

        requestQueue.add(new JsonObjectRequest(
                Request.Method.POST, getPostalURL(contactID),
                address.serialize(),
                listener, errorListener));
    }

    public static void deletePostal(
            RequestQueue requestQueue,
            int postalID, int contactID,
            ContactAdapter.ISuccessNoResponse callback,
            @Nullable Response.ErrorListener errorListener) {

        requestQueue.add(new StringRequest(
                Request.Method.DELETE, getPostalURL(contactID) + "/" + postalID,
                response -> callback.successCallback(), errorListener));
    }
}
