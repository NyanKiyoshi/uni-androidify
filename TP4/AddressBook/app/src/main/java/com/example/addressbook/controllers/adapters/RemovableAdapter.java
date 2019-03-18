package com.example.addressbook.controllers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.addressbook.R;
import com.example.addressbook.controllers.ViewUtils;
import com.example.addressbook.models.IStringSerializable;
import com.example.addressbook.views.viewholders.RemovableViewHolder;

public class RemovableAdapter<Cls extends IStringSerializable>
        extends BaseAdapter<RemovableViewHolder, Cls>
        implements ViewUtils.IRemoveClickListener<Cls> {

    private ViewUtils.IOnClickEvent<Cls> removeClickListener;

    public RemovableAdapter(ViewUtils.IOnClickEvent<Cls> removeClickListener) {
        super(null);
        this.removeClickListener = removeClickListener;
    }

    public RemovableAdapter() {
        this(null);
        this.removeClickListener = (item, pos) -> this.removeItem(pos);
    }

    @NonNull
    @Override
    public RemovableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(
                R.layout.removable_entry, parent, false);
        return new RemovableViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RemovableViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.text.setText(this.items.get(position).toString());
        holder.removeBtn.setOnClickListener(ViewUtils.wrapRecyclerRemoveItem(this, holder));
    }

    @Override
    public ViewUtils.IOnClickEvent<Cls> getRemoveCallback() {
        return this.removeClickListener;
    }

    @Override
    public Cls getItem(int pos) {
        return this.items.get(pos);
    }
}
