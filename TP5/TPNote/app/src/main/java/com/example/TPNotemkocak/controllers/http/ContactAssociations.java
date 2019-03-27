package com.example.TPNotemkocak.controllers.http;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.TPNotemkocak.models.AppConfig;
import com.example.TPNotemkocak.models.BaseModel;
import com.example.TPNotemkocak.views.SelectAddressOrNew;

import org.json.JSONException;

import java.util.ArrayList;

public final class ContactAssociations {
    public static final String EXTRA_POSTAL_TO_REMOVE =
            "com.example.TPNotemkocak.controllers.http.ContactAssociations.EXTRA_POSTAL_TO_REMOVE";
    public static final String EXTRA_POSTAL_TO_ADD =
            "com.example.TPNotemkocak.controllers.http.ContactAssociations.EXTRA_POSTAL_TO_ADD";

    public static final String EXTRA_NUMBER_TO_REMOVE =
            "com.example.TPNotemkocak.controllers.http.ContactAssociations.EXTRA_NUMBER_TO_REMOVE";
    public static final String EXTRA_NUMBER_TO_ADD =
            "com.example.TPNotemkocak.controllers.http.ContactAssociations.EXTRA_NUMBER_TO_ADD";

    public static final String EXTRA_MAIL_TO_REMOVE =
            "com.example.TPNotemkocak.controllers.http.ContactAssociations.EXTRA_MAIL_TO_REMOVE";
    public static final String EXTRA_MAIL_TO_ADD =
            "com.example.TPNotemkocak.controllers.http.ContactAssociations.EXTRA_MAIL_TO_ADD";

    public static String getPostalURL(int contactID) {
        return AppConfig.getURL("/persons/" + contactID + "/postalAddresses");
    }

    public static String getPhoneURL(int contactID) {
        return AppConfig.getURL("/persons/" + contactID + "/phones");
    }

    public static String getMailURL(int contactID) {
        return AppConfig.getURL("/persons/" + contactID + "/mailAddresses");
    }

    private static void applyAddresses(
            SelectAddressOrNew adapter, ArrayList<Integer> toDelete,
            Intent intent,
            String extraAdd, String extraRemove)
            throws JSONException {

        ArrayList<String> payloads = new ArrayList<>();

        BaseModel item;
        for (Object o: adapter.items) {
            item = (BaseModel) o;
            if (item.getId() < 1) {
                payloads.add(item.serialize().toString());
            }
        }
        intent.putStringArrayListExtra(extraAdd, payloads);
        intent.putIntegerArrayListExtra(extraRemove, toDelete);
    }

    public static void applyPostalAddresses(
            SelectAddressOrNew adapter, ArrayList<Integer> toDelete, Intent intent)
            throws JSONException {

        applyAddresses(adapter, toDelete, intent, EXTRA_POSTAL_TO_ADD, EXTRA_POSTAL_TO_REMOVE);
    }

    public static void applyPhoneNumbers(
            SelectAddressOrNew adapter, ArrayList<Integer> toDelete, Intent intent)
            throws JSONException {

        applyAddresses(adapter, toDelete, intent, EXTRA_NUMBER_TO_ADD, EXTRA_NUMBER_TO_REMOVE);
    }

    public static void applyEmails(
            SelectAddressOrNew adapter, ArrayList<Integer> toDelete, Intent intent)
            throws JSONException {

        applyAddresses(adapter, toDelete, intent, EXTRA_MAIL_TO_ADD, EXTRA_MAIL_TO_REMOVE);
    }

    private static void postRequest(
            RequestQueue requestQueue,
            String url, @Nullable String payload,
            Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {

        requestQueue.add(new RawJsonRequest(
                payload == null ? Request.Method.DELETE : Request.Method.POST,
                url, payload, listener, errorListener));
    }

    public static void createPostal(
            RequestQueue requestQueue,
            String payload, int contactID,
            Response.Listener<String> listener,
            @Nullable Response.ErrorListener errorListener) {

        postRequest(
                requestQueue, getPostalURL(contactID), payload,
                listener, errorListener);
    }

    public static void deletePostal(
            RequestQueue requestQueue,
            int postalID, int contactID,
            Response.Listener<String> listener,
            @Nullable Response.ErrorListener errorListener) {

        postRequest(
                requestQueue, getPostalURL(contactID) + "/" + postalID, null,
                listener, errorListener);
    }

    public static void createPhone(
            RequestQueue requestQueue,
            String payload, int contactID,
            Response.Listener<String> listener,
            @Nullable Response.ErrorListener errorListener) {

        postRequest(
                requestQueue, getPhoneURL(contactID), payload,
                listener, errorListener);
    }

    public static void deletePhone(
            RequestQueue requestQueue,
            int phoneID, int contactID,
            Response.Listener<String> listener,
            @Nullable Response.ErrorListener errorListener) {

        postRequest(
                requestQueue, getPhoneURL(contactID) + "/" + phoneID, null,
                listener, errorListener);
    }

    public static void createMail(
            RequestQueue requestQueue,
            String payload, int contactID,
            Response.Listener<String> listener,
            @Nullable Response.ErrorListener errorListener) {

        postRequest(
                requestQueue, getMailURL(contactID), payload,
                listener, errorListener);
    }

    public static void deleteMail(
            RequestQueue requestQueue,
            int mailID, int contactID,
            Response.Listener<String> listener,
            @Nullable Response.ErrorListener errorListener) {

        postRequest(
                requestQueue, getMailURL(contactID) + "/" + mailID, null,
                listener, errorListener);
    }
}
