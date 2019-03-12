package com.example.addressbook.views;

import android.app.Activity;

import androidx.core.widget.ContentLoadingProgressBar;

import com.example.addressbook.views.listeners.BaseAddEditActivityListener;

public interface IDeferrableActivity {
    Activity getActivity();
    ContentLoadingProgressBar getLoadingBar();
    BaseAddEditActivityListener getListener();
}
