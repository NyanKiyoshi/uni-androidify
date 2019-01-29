package com.example.androidtp2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.androidtp2.fragments.CuteCatFragment;
import com.example.androidtp2.fragments.LoremIpsumFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    LinearLayout fragmentContainer;

    LoremIpsumFragment loremIpsumFragment = new LoremIpsumFragment();
    CuteCatFragment cuteCatFragment = new CuteCatFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fragmentContainer = findViewById(R.id.fragment_container);
        this.fragmentManager = getSupportFragmentManager();

        Button catButton = findViewById(R.id.toggle_cat_button);
        Button loremButton = findViewById(R.id.toggle_lorem_ipsum_button);

        catButton.setOnClickListener(new FragmentToggleListener(
                this.fragmentManager, this.cuteCatFragment));

        loremButton.setOnClickListener(new FragmentToggleListener(
                this.fragmentManager, this.loremIpsumFragment));
    }
}
