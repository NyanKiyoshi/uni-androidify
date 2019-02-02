package com.example.androidtp2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

class FragmentToggleListener implements View.OnClickListener {
    private FragmentManager fragmentManager;
    private Fragment fragment;

    FragmentToggleListener(
            FragmentManager fragmentManager, Fragment fragment) {
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
    }

    @Override
    public void onClick(View v) {

        FragmentTransaction transaction =
                this.fragmentManager.beginTransaction();

        if (fragment.isAdded()) {
            transaction.remove(fragment);
        }
        else {
            transaction.replace(R.id.fragment_container, fragment);
        }

        transaction.commit();
    }
}
