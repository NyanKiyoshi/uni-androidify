package com.example.addressbook.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.addressbook.R;

public abstract class BaseRecyclerFragment extends Fragment {
    public Context context;
    public RequestQueue requestQueue;
    public SwipeRefreshLayout pullToRefresh;
    public ContentLoadingProgressBar loadingBar;

    abstract void refreshEntries();
    abstract void getEntries();

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
        this.pullToRefresh.setOnRefreshListener(this::refreshEntries);

        // Create a loading component from the context
        this.loadingBar = new ContentLoadingProgressBar(this.context);

        return view;
    }
}
