package com.example.androidtp2.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidtp2.R;

public class TextFragment extends Fragment {
    private String _currentTextToShow = "";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        // Create a UI fragment from the layout file
        View view = inflater.inflate(R.layout.text_fragment, container, false);

        // Set text to the one set by the other fragment
        TextView textView = view.findViewById(R.id.text_view);
        String textToSet = this.get_currentTextToShow();
        textView.setText(textToSet.isEmpty() ? "¯\\_(ツ)_/¯" : textToSet);

        return view;
    }

    public String get_currentTextToShow() {
        return this._currentTextToShow;
    }

    public void set_currentTextToShow(String currentTextToShow) {
        this._currentTextToShow = currentTextToShow;
    }
}
