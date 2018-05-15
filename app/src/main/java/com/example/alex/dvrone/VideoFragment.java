package com.example.alex.dvrone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class VideoFragment extends Fragment {

    private String path = (Environment.getExternalStorageDirectory() + "/DVROne/Video");
    private static File[] files;
    private String[] fileNames;
    private View view;
    private static int currentIndex;
    private boolean isSelection;
    private LinearLayout controlsLayout;
    private File[] filesForDelete;

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


        isSelection = false;
        controlsLayout = view.findViewById(R.id.linearLayoutControls);
        Log.d("Files", "Path: " + path);
        File directory = new File(path);

        files = directory.listFiles();
        filesForDelete = new File[files.length];

        if(files != null) {
            fileNames = new String[files.length];
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }
            final GridView gridview = view.findViewById(R.id.gridview);
            gridview.setAdapter(new ImageAdapter(this.getContext(), files, fileNames));

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int i, long id) {
                    if(isSelection){
                        if(filesForDelete[i] == null){
                            filesForDelete[i] = files[i];
                            v.setBackgroundColor(Color.BLUE);
                        } else {
                            filesForDelete[i] = null;
                            v.setBackgroundColor(Color.WHITE);
                            checkFilesForDelete();
                        }
                    } else{
                        Toast.makeText(getContext(), "" + files[i].getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        currentIndex = i;

                        Intent intent = new Intent(getActivity(), GalleryActivityVideo.class);
                        startActivity(intent);
                    }
                }
            });

            gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> av, View v, int i, long id) {
                    isSelection = !isSelection;
                    if(isSelection){
                        controlsLayout.setVisibility(View.VISIBLE);
                        v.setBackgroundColor(Color.BLUE);
                        filesForDelete[i] = files[i];
                    } else {
                        controlsLayout.setVisibility(View.GONE);
                        for(int j = 0; j < filesForDelete.length; j++){
                            filesForDelete[i] = null;
                        }
                        for(int index=0; index<((ViewGroup)view).getChildCount(); ++index) {
                            View nextChild = ((ViewGroup)view).getChildAt(index);
                            if(nextChild instanceof GridView)
                            {
                                for(int index2=0; index2<((ViewGroup)nextChild).getChildCount(); ++index2) {
                                    View nextChild2 = ((ViewGroup) nextChild).getChildAt(index2);
                                    nextChild2.setBackgroundColor(Color.WHITE);
                                }
                            }
                        }
                    }
                    return true;
                }
            });
        }
    }

    public void checkFilesForDelete(){
        isSelection = false;
        for(File f : filesForDelete){
            if(f != null){
                isSelection = true;
                break;
            }
        }
        if(!isSelection){
            controlsLayout.setVisibility(View.INVISIBLE);
        }
    }
}
