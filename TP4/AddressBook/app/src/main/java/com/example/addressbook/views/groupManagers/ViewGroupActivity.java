package com.example.addressbook.views.groupManagers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.addressbook.R;
import com.example.addressbook.controllers.ViewUtils;
import com.example.addressbook.models.GroupModel;
import com.example.addressbook.views.IDeferrableActivity;
import com.example.addressbook.views.listeners.BaseAddEditActivityListener;
import com.example.addressbook.views.listeners.GroupAddEditActivityListener;

public class ViewGroupActivity
        extends BaseGroupActivity
        implements BaseAddEditActivityListener.CRUDEvents<GroupModel>, IDeferrableActivity {

    private BaseAddEditActivityListener<GroupModel> activityListener;
    private TextView textViewTitle;

    private GroupModel groupModel;

    private ContentLoadingProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        this.textViewTitle = findViewById(R.id.edit_text_title);

        // Get the activity's intent object
        final Intent intent = getIntent();
        setTitle("Viewing Group");

        // Create the item model object
        this.groupModel = new GroupModel(
                intent.getIntExtra(EXTRA_ID, -1),
                intent.getStringExtra(EXTRA_TITLE)
        );

        // Create a progress bar for HTTP requests
        this.loadingBar = new ContentLoadingProgressBar(this);

        // Create the CRUD activity listener
        this.activityListener = new GroupAddEditActivityListener(
                this, this, requestQueue);

        this.textViewTitle.setText(this.groupModel.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.view_entry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_entry:
                this.activityListener.startUpdateEntry(this.groupModel);
                break;

            case R.id.delete_entry:
                ViewUtils.PromptDelete(this, this.groupModel);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void onEntryDeleted(Object response) {
        this.setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void refreshData() {
        // nop
    }

    @Override
    public void onEntryUpdated(GroupModel newItem) {
        this.groupModel = newItem;
        this.textViewTitle.setText(newItem.getTitle());
        this.loadingBar.hide();
        Toast.makeText(this, R.string.group_updated, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEntryFailedUpdating() {
        Toast.makeText(this, R.string.failed_to_update, Toast.LENGTH_SHORT).show();
        this.loadingBar.hide();
    }

    @Override
    public void onEntryStartUpdating(GroupModel newItem) {
        this.loadingBar.show();
    }

    @Override
    public void onIntentReadyToStart(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.activityListener.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public ContentLoadingProgressBar getLoadingBar() {
        return this.loadingBar;
    }

    @Override
    public BaseAddEditActivityListener getListener() {
        return this.activityListener;
    }
}
