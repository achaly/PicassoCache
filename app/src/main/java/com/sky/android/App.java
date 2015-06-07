package com.sky.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhgn on 15-6-7.
 */
public class App extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return sContext;
    }
}
