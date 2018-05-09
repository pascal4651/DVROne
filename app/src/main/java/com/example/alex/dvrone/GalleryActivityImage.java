package com.example.alex.dvrone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

public class GalleryActivityImage extends AppCompatActivity implements View.OnTouchListener {

    private File file;
    Button deleteButton, infoButton;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullsized_image);

        this.file = PhotoFragment.getClickedFile();

        ImageView imgv = findViewById(R.id.imageView2);
        imgv.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.GONE);

        findViewById(R.id.constraintLayout).setOnTouchListener(this);

        setTitle(file.getName());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout.postDelayed(new Runnable() {
                    public void run() {
                        linearLayout.setVisibility(View.GONE);
                    }
                }, 5000);
                break;
        }
        return false;
    }

    public void onDelete(View view){
        file.delete();
        super.onBackPressed();
    }
}

