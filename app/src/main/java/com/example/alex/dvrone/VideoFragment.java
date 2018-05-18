package com.example.alex.dvrone;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.io.File;

public class VideoFragment extends Fragment implements View.OnClickListener {

    private String path = (Environment.getExternalStorageDirectory() + "/DVROne/Video");
    private static File[] files;
    private String[] fileNames;
    private View view;
    private static int currentIndex;
    private boolean isSelection;
    private LinearLayout controlsLayout;
    private File[] filesForDelete;
    private Button buttonSelect, buttonDelete, buttonCancel;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video, container, false);
        buttonSelect = view.findViewById(R.id.buttonAllVideos);
        buttonDelete = view.findViewById(R.id.buttonDeleteVideos);
        buttonCancel = view.findViewById(R.id.buttonCancelVideos);
        buttonSelect.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        return view;
    }

    public static File getClickedFile() {
        return files[currentIndex];
    }

    public static File getNextFile() {
        if (++currentIndex == files.length) {
            currentIndex = 0;
        }
        return files[currentIndex];
    }

    public static File getPreviousFile() {
        if (--currentIndex < 0) {
            currentIndex = files.length - 1;
        }
        return files[currentIndex];
    }

    @Override
    public void onResume() {
        super.onResume();

        isSelection = false;
        controlsLayout = view.findViewById(R.id.linearLayoutControls);
        Log.d("Files", "Path: " + path);
        File directory = new File(path);

        files = directory.listFiles();
        filesForDelete = new File[files.length];

        if (files != null) {
            fileNames = new String[files.length];
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }
            final GridView gridview = view.findViewById(R.id.gridview);
            gridview.setAdapter(new ImageAdapter(this.getContext(), files, fileNames));

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int i, long id) {
                    if (isSelection) {
                        if (filesForDelete[i] == null) {
                            filesForDelete[i] = files[i];
                            v.setBackgroundColor(Color.YELLOW);
                        } else {
                            filesForDelete[i] = null;
                            v.setBackgroundColor(Color.WHITE);
                            checkFilesForDelete();
                        }
                    } else {
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
                    if (isSelection) {
                        controlsLayout.setVisibility(View.VISIBLE);
                        v.setBackgroundColor(Color.YELLOW);
                        filesForDelete[i] = files[i];
                    } else {
                        buttonCancel.performClick();
                    }
                    return true;
                }
            });
        }
    }

    public void checkFilesForDelete() {
        for (File f : filesForDelete) {
            if (f != null) {
                return;
            }
        }
        isSelection = false;
        controlsLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAllVideos:
                boolean allSelected = true;
                for(File f : filesForDelete){
                    if(f == null){
                        allSelected = false;
                        break;
                    }
                }
                if(allSelected){
                    buttonCancel.performClick();
                } else {
                    for (int i = 0; i < files.length; i++) {
                        filesForDelete[i] = files[i];
                    }
                    setItemsBackgrounColor(Color.YELLOW);
                }
                break;
            case R.id.buttonDeleteVideos:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirm delete")
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setMessage("Are you sure you want to delete this file(s) ?")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for(int i = 0; i < filesForDelete.length; i++)
                                        {
                                            if(filesForDelete[i] != null)
                                            {
                                                filesForDelete[i] = null;
                                                files[i].delete();
                                            }
                                        }
                                        getActivity().recreate();
                                    }
                                })
                        .setNegativeButton("Cancel", null).create();
                builder.show();
                break;
            case R.id.buttonCancelVideos:
                controlsLayout.setVisibility(View.GONE);
                isSelection = false;
                filesForDelete = new File[files.length];
                setItemsBackgrounColor(Color.WHITE);
        }
    }

    public void setItemsBackgrounColor(int color){
        for (int index = 0; index < ((ViewGroup) view).getChildCount(); ++index) {
            View nextChild = ((ViewGroup) view).getChildAt(index);
            if (nextChild instanceof GridView) {
                for (int index2 = 0; index2 < ((ViewGroup) nextChild).getChildCount(); ++index2) {
                    View nextChild2 = ((ViewGroup) nextChild).getChildAt(index2);
                    nextChild2.setBackgroundColor(color);
                }
            }
        }
    }
}
