package com.example.alex.dvrone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;

public class GalleryActivityImage extends AppCompatActivity {

File file;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.file = PhotoFragment.getClickedFile();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fullsized_image);



        ImageView imgv = findViewById(R.id.imageView2);

        imgv.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
    }
}

