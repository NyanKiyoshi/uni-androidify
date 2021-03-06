package com.example.addressbook.controllers.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.example.addressbook.R;
import com.example.addressbook.controllers.ViewUtils;
import com.example.addressbook.controllers.files.ImageProcessor;
import com.example.addressbook.models.ContactModel;
import com.example.addressbook.views.viewholders.ContactViewHolder;

import static com.example.addressbook.models.Drawables.DefaultContactPic;
import static com.example.addressbook.models.Drawables.DefaultContactPicDark;
import static com.example.addressbook.models.Drawables.ResolutionThumbnail;

public class ContactAdapter extends BaseAdapter<ContactViewHolder, ContactModel> {
    private final Handler handler = new Handler();

    public interface IHasContext {
        Context getContext();
    }

    private final IHasContext parent;

    public ContactAdapter(IHasContext parent, OnItemClickEvent<ContactModel> listener) {
        super(listener);
        this.parent = parent;
    }

    private static void setDefaultImage(ImageView destView) {
        destView.setImageResource(DefaultContactPic);
    }

    private static void setDefaultImageDark(ImageView destView) {
        destView.setImageResource(DefaultContactPicDark);
    }

    public static void setImage(String path, ImageView destView, boolean resize) {
        if (path == null) {
            setDefaultImage(destView);
            return;
        }

        Bitmap bitmap = resize
                ? ImageProcessor.decodeSampledBitmap(
                path, ResolutionThumbnail, ResolutionThumbnail)
                : BitmapFactory.decodeFile(path);

        destView.setImageBitmap(bitmap);
    }

    public static void setImage(String path, ImageView destView) {
        setImage(path, destView, true);
    }

    @NonNull
    public ContactViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType, @LayoutRes int layoutID) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(layoutID, parent, false);
        return new ContactViewHolder(v);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return this.onCreateViewHolder(parent, viewType, R.layout.contact_entry);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        final ContactModel item = this.items.get(position);

        if (holder.id != null) {
            holder.id.setText(item.getIdStr());
        }
        holder.firstname.setText(item.getFirstName());
        holder.lastname.setText(item.getLastName());

        item.setSharedPreferences(ViewUtils.GetSharedPrefs(this.parent.getContext()));
        String picturePath = item.getPicturePath();

        if (picturePath != null) {
            this.handler.post(() -> setImage(picturePath, holder.pictureBox));
        } else {
            setDefaultImageDark(holder.pictureBox);
        }
    }
}
