package com.example.alex.dvrone;

import android.content.Context;
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

public class testImgAdapter extends BaseAdapter {

    private File icons[];

    private String letters[];

    private Context context;

    private LayoutInflater inflater;

    public testImgAdapter(Context context,File icons[], String letters[]){
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View gridview = view;
        ImageView icon;
        TextView letter;

        if(view == null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            icon = new ImageView(context);
            letter = new TextView(context);

            gridview = inflater.inflate(R.layout.custom_layout,null);
        }


        icon = (ImageView) gridview.findViewById(R.id.imageview);
        letter = (TextView) gridview.findViewById(R.id.imgText);

        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(icons[i].getAbsolutePath()),300 ,300);
        icon.setImageBitmap(ThumbImage);
        letter.setText(letters[i]);

        return gridview;
    }
}
