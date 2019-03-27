package com.example.TPNotemkocak.views;

import android.app.Activity;

import androidx.core.widget.ContentLoadingProgressBar;

import com.example.TPNotemkocak.listeners.BaseAddEditActivityListener;

public interface IDeferrableActivity {
    Activity getActivity();
    ContentLoadingProgressBar getLoadingBar();
    BaseAddEditActivityListener getListener();
}
