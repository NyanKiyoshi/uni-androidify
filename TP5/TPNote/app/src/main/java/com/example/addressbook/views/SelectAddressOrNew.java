package com.example.addressbook.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.R;
import com.example.addressbook.controllers.ViewUtils;
import com.example.addressbook.controllers.adapters.RemovableAdapter;
import com.example.addressbook.models.AddressModel;
import com.example.addressbook.views.components.EntryListView;

/**
 * Allows the user to select from a list of addresses
 * or to create a new value.
 */
public class SelectAddressOrNew<ModelCls extends AddressModel> extends RemovableAdapter<ModelCls> {
    private final Class<ModelCls> modelClass;
    private final ViewGroup view;
    private final Context context;
    private final @Nullable String title;
    private final int inputType;

    public SelectAddressOrNew(
            Class<ModelCls> modelClass,
            ViewGroup view,
            Context context,
            EntryListView entryManager, int inputType,
            ViewUtils.IOnClickEvent<ModelCls> removeClickListener) {

        super(removeClickListener);

        this.modelClass = modelClass;
        this.view = view;
        this.context = context;
        this.title = entryManager.getTitle();
        this.inputType = inputType;

        RecyclerView recyclerView = entryManager.getRecyclerView();
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        entryManager.getAddButton().setOnClickListener(this::onNewButtonClick);
    }

    public void createNew() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(this.title);
        View inflated = LayoutInflater.from(context).inflate(R.layout.address_alert, view, false);
        builder.setView(inflated);

        final String[] selectedType = {null};

        ((Spinner)(inflated.findViewById(R.id.type)))
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {

                        selectedType[0] = (String) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        EditText editAddress = inflated.findViewById(R.id.address);
        editAddress.setHint(this.title);
        editAddress.setInputType(this.inputType);

        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            // Retrieve the input text
            String inputText = editAddress.getText().toString();

            // Check the input is not empty or show an error
            if (inputText.isEmpty()) {
                Toast.makeText(
                        this.context, R.string.please_fill_form, Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new item from the input
            try {
                ModelCls newItem = this.modelClass.newInstance();
                newItem.setAddress(inputText);
                newItem.setType(selectedType[0]);
                this.addItem(newItem);
            } catch (Exception e) {
                Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            // Finally, in all cases, dismiss the form
            dialog.dismiss();
        }).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void onNewButtonClick(View view) {
        this.createNew();
    }
}
