package com.example.alex.dvrone;

import android.content.Intent;
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

        if(files != null) {
            fileNames = new String[files.length];
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }
            GridView gridview = (GridView) view.findViewById(R.id.gridview);
            gridview.setAdapter(new ImageAdapter(this.getContext(), files, fileNames));

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    Toast.makeText(getContext(), "" + files[position].getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    currentIndex = position;

                    Intent intent = new Intent(getActivity(), GalleryActivityVideo.class);
                    startActivity(intent);
                }
            });
        }
    }
}
