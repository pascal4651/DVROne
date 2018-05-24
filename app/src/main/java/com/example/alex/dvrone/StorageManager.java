package com.example.alex.dvrone;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class StorageManager {
    // Get internal (data partition) free space
// This will match what's shown in System Settings > Storage for
// Internal Space, when you subtract Total - Used
    public static long getFreeInternalMemory() {
        return getFreeMemory(Environment.getDataDirectory());
    }

    // Get external (SDCARD) free space
    public static long getFreeExternalMemory() {
        return getFreeMemory(Environment.getExternalStorageDirectory());
    }

    // Get Android OS (system partition) free space
    public static long getFreeSystemMemory() {
        return getFreeMemory(Environment.getRootDirectory());
    }

    // Get free space for provided path
    // Note that this will throw IllegalArgumentException for invalid paths
    public static long getFreeMemory(File path) {
        StatFs stats = new StatFs(path.getAbsolutePath());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //noinspection deprecation
            return (stats.getAvailableBlocks() * stats.getBlockSize());
        } else {
            return stats.getAvailableBlocksLong() * stats.getBlockSizeLong();
        }
    }

    public static long totalMemory(String path) {
        StatFs stats = new StatFs(path);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //noinspection deprecation
            return (stats.getBlockCount() * stats.getBlockSize());
        } else {
            return (stats.getBlockCountLong() * stats.getBlockSizeLong());
        }
    }

    public static String floatForm(double d) {
        return new DecimalFormat("#.##").format(d);
    }

    public static String bytesToHuman(long size) {
        long Kb = 1 * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size < Kb) return floatForm(size) + " byte";
        if (size >= Kb && size < Mb) return floatForm((double) size / Kb) + " Kb";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + " Mb";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + " Gb";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + " Tb";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + " Pb";
        if (size >= Eb) return floatForm((double) size / Eb) + " Eb";

        return "???";
    }

    private static boolean isValidDir(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    public static long getDirSize(File dir) {
        if (!isValidDir(dir))
            return 0L;
        List<File> allFiles = getFilesFromDir(dir);
        Queue<File> dirs = new LinkedList();
        dirs.add(dir);
        while (!dirs.isEmpty()) {
            for (File f : dirs.poll().listFiles()) {
                if (f.isDirectory()) {
                    dirs.add(f);
                } else if (f.isFile()) {
                    allFiles.add(f);
                }
            }
        }
        long size = 0;
        if (allFiles.size() > 0) {
            for (File f : allFiles) {
                size += f.length();
            }
        }
        return size;
    }

    public static List<File> getFilesFromDir(File dir){
        List<File> allFiles = new ArrayList();
        Queue<File> dirs = new LinkedList();
        dirs.add(dir);
        while (!dirs.isEmpty()) {
            for (File f : dirs.poll().listFiles()) {
                if (f.isDirectory()) {
                    dirs.add(f);
                } else if (f.isFile()) {
                    allFiles.add(f);
                }
            }
        }
        return allFiles;
    }

    public static List<File> getSortedFilesFromDir(File dir){
        if (!isValidDir(dir))
            return null;
        List<File> allFiles = getFilesFromDir(dir);
        if(allFiles.size() > 1){
            Collections.sort(allFiles, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    long k = file1.lastModified() - file2.lastModified();
                    if(k > 0){
                        return 1;
                    }else if(k == 0){
                        return 0;
                    }else{
                        return -1;
                    }
                }
            });
        }
        return allFiles;
    }
}
