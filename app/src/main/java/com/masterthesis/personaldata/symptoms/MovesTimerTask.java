package com.masterthesis.personaldata.symptoms;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Konstantinos Michail on 2/14/2016.
 */
public class MovesTimerTask extends TimerTask {
    private final static long ONCE_PER_DAY = 1000 * 60 * 60 * 24;

    //private final static int ONE_DAY = 1;
    private final static int TWO_AM = 2;
    private final static int ZERO_MINUTES = 0;
    private static final String TAG = "MovesTask";

    private static Date getTomorrowMorning2AM() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,TWO_AM);
        cal.set(Calendar.MINUTE, ZERO_MINUTES);
        return cal.getTime();
    }

    //call this method from your servlet init method
    public static void startTask() {
        MovesTimerTask movesTimerTask = new MovesTimerTask();
        Timer timer = new Timer();
        timer.schedule(movesTimerTask, getTomorrowMorning2AM(), ONCE_PER_DAY);
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            long ftime = System.currentTimeMillis() + 5000;
            while (System.currentTimeMillis() < ftime) {
                synchronized (this) {
                    try {
                        wait(ftime - System.currentTimeMillis());
                        Log.i(TAG, "waiting");
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

}
