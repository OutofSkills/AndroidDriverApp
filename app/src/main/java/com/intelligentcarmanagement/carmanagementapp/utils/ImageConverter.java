package com.intelligentcarmanagement.carmanagementapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageConverter {
    public static byte[] convertUrlToBytes(String url){
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            return baf.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }

    public static Bitmap convertBytesToBitmap(byte[] byteImage)
    {
        return BitmapFactory.decodeByteArray(byteImage , 0, byteImage .length);
    }

    public static byte[] convertBase64ToBytes(String base64Image)
    {
        return Base64.decode(base64Image, Base64.DEFAULT);
    }

    public static String convertBytesToBase64(byte[] imageBytes)
    {
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static byte[] convertBitmapToBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
}
