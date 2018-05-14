package com.example.alex.dvrone;

import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
=======
import android.net.Uri;
import android.os.AsyncTask;
>>>>>>> f5f5b858f2d76e373bbf98108453c27c00cb0a8f
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

public class VideoFragment extends Fragment {

    private String path = (Environment.getExternalStorageDirectory() + "/DVROne/Video");
    private static File[] files;
    private String[] fileNames;
    private View view;
    private static int currentIndex;
    private GridView gridview;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_video, container, false);
        return view;
    }

    public static File getClickedFile() {
        return files[currentIndex];
    }

    public static File getNextFile(){
        if(++currentIndex == files.length){
            currentIndex = 0;
        }
        return files[currentIndex];
    }

    public static File getPreviousFile(){
        if(--currentIndex < 0){
            currentIndex = files.length - 1;
        }
        return files[currentIndex];
    }


    @Override
    public void onResume(){
        super.onResume();



        Log.d("Files", "Path: " + path);
        File directory = new File(path);

        files = directory.listFiles();
<<<<<<< HEAD
        fileNames = new String[files.length];
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            fileNames[i] = files[i].getName();
        }
        gridview = (GridView) view.findViewById(R.id.gridview);
        //gridview.setAdapter(new VideoAdapter(this.getContext(),files,fileNames));
=======
        if(files != null) {
            fileNames = new String[files.length];
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }
            GridView gridview = (GridView) view.findViewById(R.id.gridview);
            gridview.setAdapter(new VideoAdapter(this.getContext(), files, fileNames));
>>>>>>> f5f5b858f2d76e373bbf98108453c27c00cb0a8f

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

<<<<<<< HEAD
                Toast.makeText(getContext(), "" + files[position].getAbsolutePath(), Toast.LENGTH_SHORT).show();
                currentIndex = position;
=======
                    Toast.makeText(con, "" + files[position].getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    currentIndex = position;
>>>>>>> f5f5b858f2d76e373bbf98108453c27c00cb0a8f

                    Intent intent = new Intent(getActivity(), GalleryActivityVideo.class);
                    startActivity(intent);
                }
            });
        }
    }
}
