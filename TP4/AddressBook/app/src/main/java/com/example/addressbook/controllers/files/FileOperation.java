package com.example.addressbook.controllers.files;

import android.graphics.Bitmap;

import java.io.FileOutputStream;
import java.io.IOException;

public final class FileOperation {
    public static void saveBitmap(Bitmap bitmap, String dest) throws IOException {
        FileOutputStream fos = new FileOutputStream(dest);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
        fos.close();
    }
}
