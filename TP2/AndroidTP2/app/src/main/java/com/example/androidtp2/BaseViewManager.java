package com.example.androidtp2;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.androidtp2.fragments.CuteCatFragment;
import com.example.androidtp2.fragments.EditorFragment;
import com.example.androidtp2.fragments.TextFragment;

public class BaseViewManager extends AppCompatActivity {

    protected FragmentManager fragmentManager;
    protected LinearLayout fragmentContainer;

    protected TextFragment textFragment = new TextFragment();
    protected CuteCatFragment cuteCatFragment = new CuteCatFragment();

    protected EditorFragment editorFragment = new EditorFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragmentContainer = findViewById(R.id.fragment_container);
        this.fragmentManager = getSupportFragmentManager();
    }

    public TextFragment getTextFragment() {
        return textFragment;
    }

    public CuteCatFragment getCuteCatFragment() {
        return cuteCatFragment;
    }

    public EditorFragment getEditorFragment() {
        return editorFragment;
    }
}
