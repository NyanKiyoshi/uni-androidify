package com.example.addressbook.controllers;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.addressbook.controllers.adapters.ContactAdapter;
import com.example.addressbook.controllers.adapters.RemovableAdapter;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.models.IStringSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GroupAssociations {
    public static final String EXTRA_GROUPS_TO_REMOVE =
            "com.example.addressbook.controllers.adapters.GroupAdapter.EXTRA_GROUPS_TO_REMOVE";
    public static final String EXTRA_GROUPS_TO_ADD =
            "com.example.addressbook.controllers.adapters.GroupAdapter.EXTRA_GROUPS_TO_ADD";

    private static void manageAssociation(
            int requestMethod,
            RequestQueue requestQueue,
            int groupID, int contactID,
            ContactAdapter.ISuccessNoResponse callback,
            @Nullable Response.ErrorListener errorListener) {

        requestQueue.add(new StringRequest(requestMethod,
                AppConfig.getURL("/persons/" + contactID + "/groups/" + groupID),
                response -> callback.successCallback(),
                errorListener
        ));
    }

    public static void associateToContact(
            RequestQueue requestQueue,
            int groupID, int contactID,
            ContactAdapter.ISuccessNoResponse callback,
            @Nullable Response.ErrorListener errorListener) {

        manageAssociation(
                Request.Method.POST, requestQueue, groupID, contactID, callback, errorListener);
    }

    public static void deleteAssociation(
            RequestQueue requestQueue,
            int groupID, int contactID,
            ContactAdapter.ISuccessNoResponse callback,
            @Nullable Response.ErrorListener errorListener) {

        manageAssociation(
                Request.Method.DELETE, requestQueue, groupID, contactID, callback, errorListener);
    }

    private static boolean isNotInGroup(
            IStringSerializable target, Iterable<? extends IStringSerializable> source) {

        for (IStringSerializable group : source) {
            if (target.getId() == group.getId()) {
                return false;
            }
        }
        return true;
    }

    private static void populateSelectedGroupsBundle(
            Bundle bundle, ArrayList<Integer> toAdd, ArrayList<Integer> toRemove) {

        bundle.putIntegerArrayList(EXTRA_GROUPS_TO_ADD, toAdd);
        bundle.putIntegerArrayList(EXTRA_GROUPS_TO_REMOVE, toRemove);
    }

    public static Bundle applyGroups(
            RemovableAdapter<IStringSerializable> adapter, ContactModel contact) {

        Bundle results = new Bundle();

        ArrayList<Integer> toRemove = new ArrayList<>();
        ArrayList<Integer> toAdd = new ArrayList<>();

        // If the groups were not initialized (should not happen, but could)
        if (contact == null || contact.groups == null) {
            populateSelectedGroupsBundle(results, toAdd, toRemove);
            return results;
        }

        // Cast the groups to an iterable (was already iterable but we need it...)
        List<IStringSerializable> existingGroups = Arrays.asList(contact.groups);

        // Find selected groups that were not already associated to the contact
        // And add them to the list of group to be added.
        for (IStringSerializable selectedGroup : adapter.items) {
            if (isNotInGroup(selectedGroup, existingGroups)) {
                toAdd.add(selectedGroup.getId());
            }
        }

        // Find groups that were associated to the contact but removed by the user
        // And add them to the list of group to be removed.
        for (IStringSerializable prevGroup : contact.groups) {
            if (isNotInGroup(prevGroup, adapter.items)) {
                toRemove.add(prevGroup.getId());
            }
        }

        populateSelectedGroupsBundle(results, toAdd, toRemove);
        return results;
    }
}
