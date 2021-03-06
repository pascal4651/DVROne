package com.example.alex.dvrone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageAdapter extends BaseAdapter {


    private File icons[];

    private String letters[];

    private Context context;

    private LayoutInflater inflater;

    public ImageAdapter(Context context, File icons[], String letters[]) {
        this.context = context;
        this.letters = letters;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return letters.length;
    }

    @Override
    public Object getItem(int i) {
        return letters[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private Bitmap decodeImage(File f) {
        Bitmap b = null;
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            float sc = 0.0f;
            int scale = 1;
            //if image height is greater than width
            if (o.outHeight > o.outWidth) {
                sc = o.outHeight / 300;
                scale = Math.round(sc);
            }
            //if image width is greater than height
            else {
                sc = o.outWidth / 300;
                scale = Math.round(sc);
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (IOException e) {
        }
        return b;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View gridview = view;
        ImageView icon;
        TextView letter;

        if (view == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            icon = new ImageView(context);
            letter = new TextView(context);

            gridview = inflater.inflate(R.layout.custom_layout, null);
        }


        icon = (ImageView) gridview.findViewById(R.id.imageview);
        letter = (TextView) gridview.findViewById(R.id.imgText);

        if(icon.getTag() != null) {
            ((ImageGetter) icon.getTag()).cancel(true);
        }
        ImageGetter task = new ImageGetter(icon) ;
        task.execute(icons[i]);
        icon.setTag(task);

        //Bitmap ThumbImage = decodeImage(icons[i]);
        //icon.setImageBitmap(ThumbImage);
        letter.setText(letters[i]);

        return gridview;
    }
}




