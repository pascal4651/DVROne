package com.example.alex.dvrone;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

public class GalleryActivityImage extends AppCompatActivity implements View.OnTouchListener {

    private File file;
    private LinearLayout layout;
    private ImageView imgv;
    private Button buttonNext, buttonPrevious;
    private float initialXPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullsized_image);

        this.file = PhotoFragment.getClickedFile();
        buttonNext = findViewById(R.id.buttonRight);
        buttonPrevious = findViewById(R.id.buttonLeft);

        imgv = findViewById(R.id.imageView2);
        imgv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        imgv.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

        layout = findViewById(R.id.baseLayout);
        layout.setVisibility(View.GONE);

        findViewById(R.id.constraintLayout).setOnTouchListener(this);

        setTitle(file.getName());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialXPoint = motionEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float deltaX = initialXPoint - motionEvent.getX();
                if (deltaX > 200) {
                    buttonNext.performClick();
                } else if(deltaX < -200){
                    buttonPrevious.performClick();
                } else{
                    layout.setVisibility(View.VISIBLE);
                    layout.postDelayed(new Runnable() {
                        public void run() {
                            layout.setVisibility(View.GONE);
                        }
                    }, 3000);
                }
                break;
        }
        return true;
    }

    public void onDelete(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm delete")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setMessage("Are you sure you want to delete this file ?")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteFile();
                            }
                        })
                .setNegativeButton("Cancel", null).create();
        builder.show();
    }

    public void deleteFile(){
        file.delete();
        super.onBackPressed();
    }

    public void onInfo(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Details")
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setMessage(Html.fromHtml("<b>FULL PATH: </b>" + "<small><i>" +  file.getAbsolutePath() + "</i></small>"
                        + "<br><br><b>TYPE: </b><i>photo</i><br><br><b>SIZE: </b><i>" + StorageManager.bytesToHuman(file.length()) + "</i>"
                        + "<br><br><b>RESOLUTION: </b><i>" + getResolutionToString(file) + "</i>"))
                .setPositiveButton("Close", null).create();
        builder.show();
    }

    public String getResolutionToString(File imagePath){
        BitmapFactory.Options bitMapOption = new BitmapFactory.Options();
        bitMapOption.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath.getAbsolutePath(), bitMapOption);
        return bitMapOption.outWidth + "x" + bitMapOption.outHeight;
    }

    public void onNextImage(View view){
        file = PhotoFragment.getNextFile();
        imgv.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        setTitle(file.getName());
    }

    public void onPreviousImage(View view){
        file = PhotoFragment.getPreviousFile();
        imgv.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        setTitle(file.getName());
    }
}

