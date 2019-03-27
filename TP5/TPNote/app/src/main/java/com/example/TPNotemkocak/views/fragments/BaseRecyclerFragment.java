package com.example.TPNotemkocak.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TPNotemkocak.R;
import com.example.TPNotemkocak.controllers.adapters.BaseAdapter;
import com.example.TPNotemkocak.models.BaseModel;
import com.example.TPNotemkocak.views.components.EmptyRecyclerView;
import com.example.TPNotemkocak.views.viewholders.BaseViewHolder;

import static com.example.TPNotemkocak.controllers.ViewUtils.RESULT_DELETED;

public abstract
class BaseRecyclerFragment<Model extends BaseModel, VH extends BaseViewHolder>
        extends Fragment {

    private EmptyRecyclerView recyclerView;
    BaseAdapter<VH, Model> adapter;

    public Context context;

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

        // Set-up and bind the recycler view
        this.recyclerView = view.findViewById(R.id.listRecyclerView);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        this.recyclerView.setEmptyView(view.findViewById(R.id.empty_recycler));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_DELETED) {
            // TODO: delete it :)
        }
    }
}
