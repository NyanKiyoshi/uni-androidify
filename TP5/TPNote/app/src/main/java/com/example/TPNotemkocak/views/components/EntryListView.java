package com.example.TPNotemkocak.views.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TPNotemkocak.R;

public class EntryListView extends LinearLayout {
    private EmptyRecyclerView recyclerView;
    private View addButton;
    private String title;

    public EntryListView(Context context) {
        super(context);
        this.init(context, null, 0);
    }

    public EntryListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs, 0);
    }

    public EntryListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.removable_entry_list_view, this, true);

        this.recyclerView = view.findViewById(R.id.itemListRecyclerView);
        this.recyclerView.setEmptyView(view.findViewById(R.id.empty_recycler));
        this.addButton = view.findViewById(R.id.add_btn);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(
                    attrs, R.styleable.EntryListView, defStyleAttr, 0);

            this.title = a.getString(R.styleable.EntryListView_alert_title);
            ((TextView)this.findViewById(R.id.title)).setText(
                    a.getString(R.styleable.EntryListView_title));

            a.recycle();
        }
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    public View getAddButton() {
        return this.addButton;
    }

    public @Nullable
    String getTitle() {
        return title;
    }
}
