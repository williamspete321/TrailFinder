package com.example.android.trailfinder.utilities;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;

public class DrawableUtils {

    public static Drawable loadImageFromUrl(String url) {
        try {
            InputStream inputStream = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(inputStream, "trail_image");
            return drawable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
