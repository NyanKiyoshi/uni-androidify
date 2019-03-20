package com.example.addressbook.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.addressbook.R;
import com.example.addressbook.controllers.adapters.BaseAdapter;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.BaseModel;
import com.example.addressbook.listeners.BaseAddEditActivityListener;
import com.example.addressbook.views.viewholders.BaseViewHolder;

import org.json.JSONArray;
import org.json.JSONException;

import static com.example.addressbook.controllers.ViewUtils.RESULT_DELETED;

public abstract
class BaseRecyclerFragment<Model extends BaseModel, VH extends BaseViewHolder>
        extends Fragment
        implements BaseAddEditActivityListener.CRUDEvents<Model> {

    private Class<Model> modelClass;

    RequestQueue requestQueue;
    SwipeRefreshLayout pullToRefresh;
    ContentLoadingProgressBar loadingBar;
    BaseAdapter<VH, Model> adapter;
    BaseAddEditActivityListener<Model> activityListener;

    public Context context;

    abstract String getEndpoint();

    BaseRecyclerFragment(Class<Model> modelClass) {
        this.modelClass = modelClass;
    }

    private void onError(Exception error) {
        // Stop loading
        this.pullToRefresh.setRefreshing(false);

        // Log the full error
        Log.e(this.getClass().getName(),
                "got an error while getting entries", error);

        // Show a simple error to the user
        if (this.context != null) {
            Toast.makeText(
                    this.context,
                    "Failed to get entries.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        // Create a UI fragment from the layout file
        View view = inflater.inflate(
                R.layout.list_fragment, container,false);

        // Get the view context
        this.context = view.getContext();

        // Create a request queue for the view
        this.requestQueue = Volley.newRequestQueue(this.context);

        // Listen for swipe to refresh data on demand
        this.pullToRefresh = view.findViewById(R.id.pullToRefresh);
        this.pullToRefresh.setOnRefreshListener(this::refreshData);

        // Create a loading component from the context
        this.loadingBar = new ContentLoadingProgressBar(this.context);

        // Set-up and bind the recycler view
        RecyclerView recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        return view;
    }

    @Override
    public void refreshData() {
        this.adapter.clear();
        this.pullToRefresh.setRefreshing(true);
        this.getEntries();
    }

    void getEntries() {
        // Create the request URL
        String requestURL = AppConfig.getURL(this.getEndpoint());

        // Create the request object and attach it to the listeners
        Request<JSONArray> request = new JsonArrayRequest(
                requestURL, this::onListResponse, this::onError);

        // Append the request to the queue
        this.requestQueue.add(request);
    }

    void onListResponse(JSONArray data) {
        try {
            // Append the entries
            this.adapter.addItems(
                    BaseModel.deserialize(this.modelClass, data));
        }
        catch (JSONException | IllegalAccessException | java.lang.InstantiationException exc) {
            // On JSON error, dispatch it to the callback
            this.onError(exc);
        }

        // Stop loading
        this.pullToRefresh.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.activityListener.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_DELETED) {
            this.refreshData();
        }
    }

    @Override
    public void onIntentReadyToStart(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onEntryUpdated(Model newItem) {
        this.loadingBar.hide();
        Toast.makeText(this.context, R.string.entry_saved, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEntryFailedUpdating() {
        Toast.makeText(this.context, R.string.failed_to_create, Toast.LENGTH_SHORT).show();
        this.loadingBar.hide();
    }

    @Override
    public void onEntryStartUpdating(Model newItem) {
        this.loadingBar.show();
    }
}
