package com.example.addressbook.controllers.files;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;

import java.io.FileDescriptor;
import java.io.IOException;

public class ImageProcessor {
    private final static int DEFAULT_MAX_IMAGE_SIZE = 150000;  // 0.15MP
    private final int MAX_IMAGE_SIZE;

    public ImageProcessor(int maxImageSize) {
        this.MAX_IMAGE_SIZE = maxImageSize;
    }

    public ImageProcessor() {
        this(DEFAULT_MAX_IMAGE_SIZE);
    }

    public Bitmap compress(
            ContentResolver resolver, Uri source) throws IOException {

        // Get the source file's path
        ParcelFileDescriptor sourcefd = resolver.openFileDescriptor(source, "r");

        // Preprocess and copy the file
        return createScaledBitmap(sourcefd);
    }

    public @NonNull Bitmap
    createScaledBitmap(ParcelFileDescriptor sourcefd) {

        FileDescriptor fd = sourcefd.getFileDescriptor();

        BitmapFactory.Options bmfOptions = new BitmapFactory.Options();
        bmfOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, bmfOptions);

        int scale = 1;
        while((bmfOptions.outWidth * bmfOptions.outHeight) * (1 / Math.pow(scale, 2)) > MAX_IMAGE_SIZE) {
            ++scale;
        }

        Bitmap b;

        if(scale > 1){
            --scale;

            bmfOptions = new BitmapFactory.Options();
            bmfOptions.inSampleSize = scale;
            b = BitmapFactory.decodeFileDescriptor(fd, null, bmfOptions);

            int height = b.getHeight();
            int width = b.getWidth();

            double y = Math.sqrt(MAX_IMAGE_SIZE / (((double)width) / height));
            double x = (y/height) * width;

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, (int) y, true);

            b.recycle();
            b = scaledBitmap;

        } else {
            b = BitmapFactory.decodeFileDescriptor(fd);
        }

        return b;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmap(
            String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
}
