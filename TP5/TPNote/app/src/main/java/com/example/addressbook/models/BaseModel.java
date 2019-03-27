package com.example.addressbook.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseModel implements IStringSerializable {
    public BaseModel() {

    }

    public abstract BaseModel fromJSON(JSONObject data) throws JSONException;
    public abstract JSONObject serialize() throws JSONException;

    public abstract int getId();

    public static <Model extends BaseModel>
    Model[] deserialize(Class<Model> modelClass, JSONArray data)
            throws InstantiationException, IllegalAccessException, JSONException {

        int dataLength = data.length();

        // This will contains the parsed entries
        BaseModel[] entries = new BaseModel[dataLength];

        // Load the entries
        for (int i = 0; i < data.length(); ++i) {
            // Retrieve the next group data
            // and create a new group object from it.
            entries[i] = modelClass.newInstance().fromJSON(data.getJSONObject(i));
        }

        return (Model[])entries;
    }
}
