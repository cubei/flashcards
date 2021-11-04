package com.quchen.flashcard;

import android.app.Application;
import android.content.Context;
import com.google.android.material.color.DynamicColors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Lars on 10.02.2018.
 */

public class App extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static File getListRootDir() {
        File listDirectory = new File(context.getFilesDir(), "lists");

        File oldDir = new File(context.getExternalCacheDir(), "lists");
        if(oldDir.exists()) {
            try {
                copyDirectoryOneLocationToAnotherLocation(oldDir, listDirectory);
                deleteDirectory(oldDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return listDirectory;
    }

    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }


    public static boolean deleteDirectory(File file) {
        // First delete all content recursively
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if(!deleteDirectory(f)) {
                    return false;
                }
            }
        }

        // Then delete the folder itself
        return file.delete();
    }

    /* https://stackoverflow.com/questions/4178168/how-to-programmatically-move-copy-and-delete-files-and-directories-on-sd */
    private static void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);

            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    }
}
