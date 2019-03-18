package com.example.addressbook.controllers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.R;
import com.example.addressbook.models.IStringSerializable;
import com.example.addressbook.views.viewholders.RemovableViewHolder;

public class RemovableAdapter extends BaseAdapter<RemovableViewHolder, IStringSerializable> {
    public interface IOnClickEvent {
        void onRemoveClick(IStringSerializable item, int pos);
    }

    private RecyclerView.ViewHolder holder;
    private IOnClickEvent removeClickListener;

    public RemovableAdapter(IOnClickEvent removeClickListener) {
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

        this.holder = holder;
        final IStringSerializable item = this.items.get(position);

        holder.text.setText(item.toString());
        holder.removeBtn.setOnClickListener(this::onRemoveClick);
    }

    private void onRemoveClick(View view) {
        int pos = this.holder.getAdapterPosition();

        if (pos == RecyclerView.NO_POSITION || this.removeClickListener == null) {
            return;
        }

        this.removeClickListener.onRemoveClick(this.items.get(pos), pos);
    }
}
