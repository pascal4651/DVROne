package com.example.alex.dvrone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private Camera camera;
    private MediaRecorder mediaRecorder;
    private Button recordButton, photoButton;
    private SurfaceHolder holder;
    private boolean isRecording;
    private Thread timeThread;
    private TextView stopWatchText;
    private int degrees;
    private StopWatch sw;
    private int CAMERA_ID = 0;
    private boolean FULL_SCREEN = true;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private SharedPreferences sp;
    private int recordTimer;
    private int videoLengthSeconds;
    private boolean useChargerConnection;
    private boolean isExternalStorage;
    private int camProfile;
    private int rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        1);
            }
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},1);
            }

        }



        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);
        recordButton = findViewById(R.id.buttonRecord);
        photoButton = findViewById(R.id.buttonPhoto);
        stopWatchText = findViewById(R.id.textViewStopWatch);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sw = new StopWatch();
        isRecording = false;
        degrees = 0;
        holder = surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            // stop preview before making changes
                try {
                    camera.stopPreview();
                } catch (Exception e){
                    // ignore: tried to stop a non-existent preview
                }
                setPreviewSize(FULL_SCREEN);
                setCameraDisplayOrientation(CAMERA_ID);
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            }
        });

        timeThread = new Thread(){
            @Override
            public void run(){
                try{
                    while(true){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf;
                                if(degrees == 0 || degrees == 180){
                                    sdf = new SimpleDateFormat("HH:mm:ss");
                                }
                                else{
                                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                }
                                String dateString = sdf.format(date);
                                getSupportActionBar().setTitle(dateString);
                                if(isRecording){
                                    if(videoLengthSeconds > 0){
                                        recordTimer++;
                                        if(recordTimer >= videoLengthSeconds){
                                            recordButton.performClick();
                                            recordButton.performClick();
                                            recordTimer = 0;
                                        }
                                    }
                                    stopWatchText.setText("REC: " + sw.toString());
                                    CheckChargerConnetction();
                                }
                            }
                        });
                    }
                }
                catch(InterruptedException e){
                }
            }
        };
        timeThread.start();
    }

    public void CheckChargerConnetction(){
        if(useChargerConnection){
            if(!isChargerConnected(this)){
                Toast toast = Toast.makeText(this, "Charger is disconnected", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                recordButton.performClick();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;

        switch (item.getItemId())
        {
            case R.id.gallery:
                intent = new Intent(this, GalleryActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.exit:
                finish();
                System.exit(0);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCameraHardware(this);
        recordTimer = 0;
        FULL_SCREEN = sp.getBoolean("screenKey", true);
        useChargerConnection = sp.getBoolean("powerKey", true);
        String videoQuality = sp.getString( "videoQuality", "High");
        setCamcorderProfile(videoQuality);

        String storage = sp.getString("storageKey", "");
        isExternalStorage = storage.equals("gallery") ? false : true;

        videoLengthSeconds = 60 * Integer.parseInt(sp.getString("length", "0"));
        String camString = sp.getString("cameraKey", null);
        if(camString != null){
            if (Camera.getNumberOfCameras() > 1 && Integer.parseInt(camString) == 1) {
                CAMERA_ID = 1;
            }else{
                CAMERA_ID = 0;
            }
        }
        camera = Camera.open(CAMERA_ID);
        camera.startPreview();
    }

    public void setCamcorderProfile(String videoQuality){
        switch (videoQuality){
            case "Low":
                camProfile = CamcorderProfile.QUALITY_LOW;
                break;
            case "HD1080":
                camProfile = CamcorderProfile.QUALITY_1080P;
                break;
            case "HD720":
                camProfile = CamcorderProfile.QUALITY_720P;
                break;
            case "ED480":
                camProfile = CamcorderProfile.QUALITY_480P;
                break;
            default: camProfile = CamcorderProfile.QUALITY_HIGH;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        if (camera != null)
            camera.release();
        camera = null;
        isRecording = false;
        recordTimer = 0;
        sw.stop();
        stopWatchText.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        timeThread.interrupt();
    }

    public void onClickPicture(View view) {
        if(isRecording){
            recordButton.performClick();
        }
        final File photoFile = getOutputMediaFile(1);
        Toast toast = Toast.makeText(this, photoFile.toString(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    FileOutputStream fos = new FileOutputStream(photoFile);
                    fos.write(data);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        camera.startPreview();
    }

    public void onClickRecord(View view){
        if(isRecording){
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                releaseMediaRecorder();
            }
            sw.stop();
            recordTimer = 0;
            stopWatchText.setVisibility(View.INVISIBLE);
            isRecording = false;
            recordButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.rec), null, null);
            Toast toast = Toast.makeText(this, "PAUSED", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        else{
            if(useChargerConnection){
                if(!isChargerConnected(this)){
                    Toast toast = Toast.makeText(this, "Charger is disconnected", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
            }
            if (prepareVideoRecorder()) {
                mediaRecorder.start();
                isRecording = true;
                sw.start();
                stopWatchText.setVisibility(View.VISIBLE);
                recordButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.paus), null, null);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                releaseMediaRecorder();
            }
        }
    }

    private boolean prepareVideoRecorder() {

        File videoFile = getOutputMediaFile(2);
        Toast toast = Toast.makeText(this, videoFile.toString(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        camera.unlock();

        mediaRecorder = new MediaRecorder();

        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOrientationHint(rotate);
        mediaRecorder.setProfile(CamcorderProfile.get(camProfile));

        mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            camera.lock();
        }
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            finish();
            System.exit(0);
            return false;
        }
    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE){
            if(isExternalStorage){
                File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "DVROne/Photo");
                if (! mediaStorageDir.exists()){
                    if (! mediaStorageDir.mkdirs()){
                        Log.d("DVROne", "failed to create directory");
                        return null;
                    }
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
            }
            else{
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                mediaFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "IMG_"+ timeStamp + ".jpg");
            }
        } else if(type == MEDIA_TYPE_VIDEO) {
            if(isExternalStorage){
                File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "DVROne/Video");
                if (! mediaStorageDir.exists()){
                    if (! mediaStorageDir.mkdirs()){
                        Log.d("DVROne", "failed to create directory");
                        return null;
                    }
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mp4");
            }
            else{
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                mediaFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + "VID_"+ timeStamp + ".mp4");
            }
        } else {
            return null;
        }

        // initiate media scan and put the new things into the path array to
        // make the scanner aware of the location and the files you want to see
        MediaScannerConnection.scanFile(this, new String[] {mediaFile.getPath()}, null, null);

        return mediaFile;
    }

    void setPreviewSize(boolean fullScreen) {
        // get screen size
        Display display = getWindowManager().getDefaultDisplay();
        boolean widthIsMax = display.getWidth() > display.getHeight();

        Camera.Parameters parameters = camera.getParameters();
        // Check what resolutions are supported by your camera
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        List<Camera.Size> videoSizes = parameters.getSupportedVideoSizes();

        // get camera preview size
        Camera.Size size = camera.getParameters().getPreviewSize();


        RectF rectDisplay = new RectF();
        RectF rectPreview = new RectF();

        // screen's RectF
        rectDisplay.set(0, 0, display.getWidth(), display.getHeight());

        // preview's RectF
        if (widthIsMax) {
            // horizontal preview
            rectPreview.set(0, 0, size.width, size.height);
        } else {
            // vertical preview
            rectPreview.set(0, 0, size.height, size.width);
        }
        Matrix matrix = new Matrix();
        // make ready matrix to convert
        if (!fullScreen) {
            matrix.setRectToRect(rectPreview, rectDisplay, Matrix.ScaleToFit.START);


        } else {
            matrix.setRectToRect(rectDisplay, rectPreview, Matrix.ScaleToFit.START);
            matrix.invert(matrix);
            surfaceView.setLayoutParams(new ConstraintLayout.LayoutParams((int)(rectPreview.right), (int)(rectPreview.bottom)));
        }
        // convert
        matrix.mapRect(rectPreview);

        // set surface size
        //surfaceView.getLayoutParams().height = (int)(rectPreview.bottom);
        //surfaceView.getLayoutParams().width = (int)(rectPreview.right);


    }

    void setCameraDisplayOrientation(int cameraId) {
        try {
            Camera.CameraInfo camInfo = new Camera.CameraInfo();

            if (cameraId == 0)
                Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);
            else
                Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, camInfo);
            int cameraRotationOffset = camInfo.orientation;
            Camera.Parameters parameters = camera.getParameters();

            // Check what resolutions are supported by your camera
            List<Camera.Size> sizes = parameters.getSupportedPictureSizes();

            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            //int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break; // Natural orientation
                case Surface.ROTATION_90:
                    degrees = 90;
                    break; // Landscape left
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;// Upside down
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;// Landscape right
            }
            int displayRotation;
            if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                displayRotation = (cameraRotationOffset + degrees) % 360;
                displayRotation = (360 - displayRotation) % 360; // compensate
                // the
                // mirror
            } else { // back-facing
                displayRotation = (cameraRotationOffset - degrees + 360) % 360;
            }

            camera.setDisplayOrientation(displayRotation);
            if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                rotate = (360 + cameraRotationOffset + degrees) % 360;
            } else {
                rotate = (360 + cameraRotationOffset - degrees) % 360;
            }
            parameters.set("orientation", "portrait");
            parameters.setRotation(rotate);
            camera.setParameters(parameters);

            Camera.Size pictureSize = camera.getParameters().getPictureSize();
            Camera.Size videoSize = camera.getParameters().getPreviewSize();

            photoButton.setText(pictureSize.width + "x" + pictureSize.height);
            recordButton.setText(videoSize.width + "x" + videoSize.height);


        } catch (Exception e) { }
    }

    public boolean isChargerConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }
}
