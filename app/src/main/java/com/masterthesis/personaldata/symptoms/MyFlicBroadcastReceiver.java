package com.masterthesis.personaldata.symptoms;

import android.content.Context;
import android.util.Log;

import com.masterthesis.personaldata.symptoms.managers.SymptomManager;

import io.flic.lib.FlicBroadcastReceiver;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicManager;

/**
 * Created by User1 on 8/1/2016.
 */
public class MyFlicBroadcastReceiver extends FlicBroadcastReceiver {
    static long tStart;
    static long tEnd;
    //    static long measureEndTime=10000;
    static String TAG = "FlicBroadcastReceiver";

    @Override
    protected void onRequestAppCredentials(Context context) {
        // Set app credentials by calling FlicManager.setAppCredentials here
        FlicManager.setAppCredentials("59eab426-39a4-4457-8e7d-2f67f9733d54", "d0ef92f6-a494-4f3d-96c0-841c6b434909", "ScaleMeasurement");

    }


    @Override
    public void onButtonUpOrDown(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isUp, boolean isDown) {
        long tDelta;
        double elapsedSeconds;

        if (isDown) {
            // Code for button up event here
            tStart = System.currentTimeMillis();
            Log.i("time", "  " + tStart);
        } else if (isUp) {
            tEnd = System.currentTimeMillis();
            tDelta = tEnd - tStart;
            elapsedSeconds = tDelta;
            SymptomManager symptomManager = SymptomManager.getInstance();
            symptomManager.manageSymptomInput(elapsedSeconds);
            Log.i(TAG, "  " + tStart);
            Log.i(TAG, "  " + tEnd);
        }


    }

    @Override
    public void onButtonRemoved(Context context, FlicButton button) {
        // Button was removed
    }
}
