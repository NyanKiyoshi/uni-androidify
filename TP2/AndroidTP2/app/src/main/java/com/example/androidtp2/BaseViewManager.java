package com.example.androidtp2;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.androidtp2.fragments.CuteCatFragment;
import com.example.androidtp2.fragments.LoremIpsumFragment;

public class BaseViewManager extends AppCompatActivity {

    FragmentManager fragmentManager;
    LinearLayout fragmentContainer;

    LoremIpsumFragment loremIpsumFragment = new LoremIpsumFragment();
    CuteCatFragment cuteCatFragment = new CuteCatFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragmentContainer = findViewById(R.id.fragment_container);
        this.fragmentManager = getSupportFragmentManager();
    }
}
