package com.example.alex.dvrone;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

public class GalleryActivityVideo extends AppCompatActivity implements View.OnTouchListener {

    private File file;
    LinearLayout linearLayout;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_video);

        this.file = VideoFragment.getClickedFile();
        videoView = findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle next click here
                file = VideoFragment.getNextFile();
                startVideoPreview();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle previous click here
                file = VideoFragment.getPreviousFile();
                startVideoPreview();
            }
        });
        videoView.setMediaController(mediaController);
        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.GONE);

        findViewById(R.id.constraintLayout).setOnTouchListener(this);
        startVideoPreview();
    }

    public void startVideoPreview(){
        videoView.setVideoPath(file.getAbsolutePath());
        setTitle(file.getName());
        videoView.start();
        new Thread(){
            @Override
            public void run(){
                try{
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoView.pause();
                        }
                    });
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }.start();
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
                }, 3000);
                break;
        }
        return false;
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
                        + "<br><br><b>TYPE: </b><i>video</i><br><br><b>SIZE: </b><i>" + StorageManager.bytesToHuman(file.length()) + "</i>"
                        + "<br><br><b>RESOLUTION: </b><i>" + getResolutionToString(file) + "</i>"))
                .setPositiveButton("Close", null).create();
        builder.show();
    }

    public String getResolutionToString(File imagePath){
        MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
        mRetriever.setDataSource(imagePath.getAbsolutePath());
        Bitmap frame = mRetriever.getFrameAtTime();
        return frame.getWidth() + "x" + frame.getHeight();
    }
}
