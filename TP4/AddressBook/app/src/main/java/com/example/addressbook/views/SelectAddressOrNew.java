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

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.R;
import com.example.addressbook.controllers.ViewUtils;
import com.example.addressbook.controllers.adapters.RemovableAdapter;
import com.example.addressbook.models.AddressModel;

/**
 * Allows the user to select from a list of addresses
 * or to create a new value.
 */
public class SelectAddressOrNew<ModelCls extends AddressModel> extends RemovableAdapter<ModelCls> {
    private final Class<ModelCls> modelClass;
    private final ViewGroup view;
    private final Context context;
    private final @LayoutRes int layoutID;

    public SelectAddressOrNew(
            Class<ModelCls> modelClass,
            ViewGroup view,
            Context context,
            @IdRes int recyclerID, @IdRes int addButtonID, @LayoutRes int layoutID,
            ViewUtils.IOnClickEvent<ModelCls> removeClickListener) {

        super(removeClickListener);

        this.modelClass = modelClass;
        this.view = view;
        this.context = context;
        this.layoutID = layoutID;

        RecyclerView recyclerView = view.findViewById(recyclerID);
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        view.findViewById(addButtonID).setOnClickListener(this::onNewButtonClick);
    }

    public void createNew() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.create_new_address);
        View inflated = LayoutInflater.from(context).inflate(layoutID, view, false);
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
