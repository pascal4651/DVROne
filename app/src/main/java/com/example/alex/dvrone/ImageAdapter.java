package com.example.alex.dvrone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class ImageAdapter extends BaseAdapter {


    private Context context;
    private Context mContext;
    File[] mThumbIds;


    public ImageAdapter(Context c, File[] filer) {
        mContext = c;
        mThumbIds = filer;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView textView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            textView = new TextView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            textView.setLayoutParams(new GridView.LayoutParams(300,300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
            textView = new TextView(mContext);
        }

        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mThumbIds[position].getAbsolutePath()),50 ,50);
        imageView.setImageBitmap(ThumbImage);
        textView.setText(mThumbIds[position].getName());
        return imageView;
    }
}

// references to our images




