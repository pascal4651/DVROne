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
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class PhotoFragment extends Fragment implements View.OnClickListener {

    private String path = (Environment.getExternalStorageDirectory() + "/DVROne/Photo");
    private static File[] files;
    private String[] fileNames;
    private View view;
    private File[] filesForDelete;
    private static int currentIndex;
    private GridView gridview;

    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_photo, container, false);

        Button b = (Button) view.findViewById(R.id.button);
        b.setOnClickListener((View.OnClickListener) this);
        return view;

    }

    @Override
    public void onResume(){

        super.onResume();

        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        files = directory.listFiles();
        fileNames = new String[files.length];
        filesForDelete = new File[files.length];
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            fileNames[i] = files[i].getName();
        }

        gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this.getContext(), files, fileNames));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Toast.makeText(getContext(), "" + files[position].getAbsolutePath(),
                        Toast.LENGTH_SHORT).show();
                currentIndex = position;


                Intent intent = new Intent(getActivity(), GalleryActivityImage.class);
                startActivity(intent);
            }
        });
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id)  {

                view.setBackgroundColor(Color.RED);
               filesForDelete[i] = files[i];

                Toast.makeText(getContext(), "" + filesForDelete[i].getAbsolutePath(),
                        Toast.LENGTH_SHORT).show();
return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:

                for(int i = 0;i < files.length; i++)
                {
                    filesForDelete[i] = files[i];

                }
                for(int index=0; index<((ViewGroup)view).getChildCount(); ++index) {
                    View nextChild = ((ViewGroup)view).getChildAt(index);
                    if(nextChild instanceof GridView)
                    {
                        for(int index2=0; index2<((ViewGroup)nextChild).getChildCount(); ++index2) {
                            View nextChild2 = ((ViewGroup) nextChild).getChildAt(index2);
                            nextChild2.setBackgroundColor(Color.RED);
                        }
                    }

                }


/*
                for(int i = 0;i < filesForDelete.length; i++)
                {
                    if(filesForDelete[i] != null)
                    {
                        filesForDelete[i].delete();
                        files[i].delete();
                    }
                }
*/
                break;
        }
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
}
