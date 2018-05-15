package com.example.alex.dvrone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;

public class ImageGetter extends AsyncTask<File, Void, Bitmap> {
    private ImageView iv;
    public ImageGetter(ImageView v) {
        iv = v;
    }
    @Override
    protected Bitmap doInBackground(File... params) {
        return decodeFile(params[0]);
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        iv.setImageBitmap(result);
    }

    private Bitmap decodeFile(File f) {
        String extension = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));
        if(extension.equals(".mp4")){
            Bitmap ThumbImage = ThumbnailUtils.createVideoThumbnail(f.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
            return Bitmap.createScaledBitmap(ThumbImage, 300, 300, false);
        } else{
            return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()), 300, 300, false);
        }
    }
}
