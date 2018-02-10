package com.quchen.flashcard;

import android.app.Application;
import android.content.Context;

import java.io.File;

/**
 * Created by Lars on 10.02.2018.
 */

public class App extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static File getListRootDir() {
        return new File(context.getExternalCacheDir(), "lists");
    }

    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
    }
}
