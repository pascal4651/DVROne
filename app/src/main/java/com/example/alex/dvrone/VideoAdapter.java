package com.example.alex.dvrone;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class VideoAdapter extends BaseAdapter {

    private File icons[];
    private String letters[];
    private Context context;
    private LayoutInflater inflater;

    public VideoAdapter(Context context, File icons[], String letters[]) {
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

        if (view == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //icon = new ImageView(context);
            //letter = new TextView(context);
            gridview = inflater.inflate(R.layout.custom_layout, null);
        }
        icon = gridview.findViewById(R.id.imageview);
        letter = gridview.findViewById(R.id.imgText);

        if(icon.getTag() != null) {
            ((ImageGetter) icon.getTag()).cancel(true);
        }
        ImageGetter task = new ImageGetter(icon) ;
        task.execute(icons[i]);
        icon.setTag(task);

        //Bitmap ThumbImage = ThumbnailUtils.createVideoThumbnail(icons[i].getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
        //icon.setImageBitmap(Bitmap.createScaledBitmap(ThumbImage, 300, 300, false));
        letter.setText(letters[i]);
        return gridview;
    }
}
