package com.hesham.sawarstudent.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


public class AddImageUtilities {
    private static final int GALLERY = 10;
    public static final int READ_EXTERNAL_STORAGE_REQUEST_PERMISSION = 5;
    public static final int CAMERA_REQUEST_PERMISSION = 6;

    public static void openGalleryOrCameraIntent(int galleryOrCamera, Activity activity, OnRequestImageIntentListener listener){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            listener.onRequestGallery(galleryIntent);
    }


    public static String getImagePath(Uri imageUri, Context context){
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(imageUri,filePath,null,null,null);
        String imagePath = "";
        if (cursor != null){
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return imagePath;
    }
}
