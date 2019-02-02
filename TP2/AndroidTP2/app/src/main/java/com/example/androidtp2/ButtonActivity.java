package com.example.androidtp2;

import android.os.Bundle;
import android.widget.Button;

public class ButtonActivity extends BaseViewManager {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button catButton = findViewById(R.id.toggle_cat_button);
        Button loremButton = findViewById(R.id.toggle_lorem_ipsum_button);

        catButton.setOnClickListener(new FragmentToggleListener(
                this.fragmentManager, this.cuteCatFragment));

        loremButton.setOnClickListener(new FragmentToggleListener(
                this.fragmentManager, this.textFragment));
    }
}
