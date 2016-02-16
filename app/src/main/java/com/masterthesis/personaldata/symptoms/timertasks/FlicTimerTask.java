package com.masterthesis.personaldata.symptoms.timertasks;

import android.util.Log;

import com.masterthesis.personaldata.symptoms.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

import io.flic.lib.FlicButton;
import io.flic.lib.FlicButtonCallback;

/**
 * Created by Konstantinos Michail on 2/16/2016.
 */
public class FlicTimerTask extends TimerTask {
    private final static long EVERY_TEN_SECONDS = 10000;
    private static final String TAG = "FlicTask";
    static FlicButton button= MainActivity.getInstance().getButton();

    //call this method from your servlet init method
    public static void startTask() {
        final FlicTimerTask flicTimerTask=new FlicTimerTask();
        final Timer mTimer=new Timer();
        button.addFlicButtonCallback(new FlicButtonCallback(){
            @Override
            public void onDisconnect(FlicButton button) {
                super.onDisconnect(button);
                mTimer.schedule(flicTimerTask,0,2000);

                Log.i(TAG, "Button disconected");
            }

            @Override
            public void onConnectionCompleted(FlicButton button) {
                super.onConnectionCompleted(button);
                mTimer.cancel();
                mTimer.purge();
            }
        });


    }

    @Override
    public boolean cancel() {
        return super.cancel();
    }

    @Override
    public void run() {
        if (button.getConnectionStatus() == FlicButton.BUTTON_DISCONNECTED) {

            //TODO when button is disconnected
            Log.i(TAG,"Stuff to do when it is disconnected - Alarm maybe");
        }
    }
}

