package com.example.androidtp2.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidtp2.BaseViewManager;
import com.example.androidtp2.R;

public class EditorFragment extends Fragment {
    private EditText textInputField;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        // Create a UI fragment from the layout file
        View view = inflater.inflate(R.layout.editor_fragment, container, false);
        Button submitButton = view.findViewById(R.id.editor_submit_button);
        this.textInputField = view.findViewById(R.id.text_editor_input_field);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextFragment textFragment = ((BaseViewManager) getActivity()).getTextFragment();
                textFragment.set_currentTextToShow(textInputField.getText().toString());
            }
        });

        return view;
    }
}
