package com.example.addressbook.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.addressbook.R;
import com.example.addressbook.controllers.BaseAdapter;
import com.example.addressbook.models.AppConfig;
import com.example.addressbook.models.BaseModel;
import com.example.addressbook.views.listeners.BaseAddEditActivityListener;
import com.example.addressbook.views.viewholders.BaseViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.addressbook.controllers.ViewUtils.RESULT_DELETED;

public abstract
class RefreshableRecyclerFragment<Model extends BaseModel, VH extends BaseViewHolder>
        extends BaseRecyclerFragment<Model, VH> {

    SwipeRefreshLayout pullToRefresh;
    ContentLoadingProgressBar loadingBar;

    public RefreshableRecyclerFragment(Class<Model> modelClass) {
        super(modelClass);
    }

    abstract String getEndpoint();

    @Override
    void onError(Exception error) {
        // Stop loading
        this.pullToRefresh.setRefreshing(false);

        super.onError(error);
    }

    @Override
    @Nullable
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState,
            @LayoutRes int layoutToInflate) {

        View view = super.onCreateView(
                inflater, container, savedInstanceState, layoutToInflate);

        // Listen for swipe to refresh data on demand
        this.pullToRefresh = view.findViewById(R.id.pullToRefresh);
        this.pullToRefresh.setOnRefreshListener(this::refreshData);

        // Create a loading component from the context
        this.loadingBar = new ContentLoadingProgressBar(this.context);

        return view;
    }

    @Override
    public void refreshData() {
        this.pullToRefresh.setRefreshing(true);
        super.refreshData();
    }

    @Override
    void onListResponse(JSONArray data) {
        super.onListResponse(data);

        // Stop loading
        this.pullToRefresh.setRefreshing(false);
    }

    @Override
    public void onEntryUpdated(Model newItem) {
        super.onEntryUpdated(newItem);
        this.loadingBar.hide();
    }

    @Override
    public void onEntryFailedUpdating() {
        this.loadingBar.hide();
    }

    @Override
    public void onEntryStartUpdating(Model newItem) {
        this.loadingBar.show();
    }
}
