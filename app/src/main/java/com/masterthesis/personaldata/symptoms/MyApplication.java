package com.masterthesis.personaldata.symptoms;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import butterknife.ButterKnife;

public class MyApplication extends Application {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "Symptoms";


    @Override
    public void onCreate() {
        super.onCreate();

        ButterKnife.setDebug(BuildConfig.DEBUG);

        preferences = getSharedPreferences("com.masterthesis.personaldata.symptoms", Context.MODE_PRIVATE);
        editor=preferences.edit();

    }

}