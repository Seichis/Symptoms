package com.masterthesis.personaldata.symptoms.alarms;

/**
 * Created by Konstantinos Michail on 2/17/2016.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.masterthesis.personaldata.symptoms.BackgroundService;
import com.masterthesis.personaldata.symptoms.handlers.MovesRunnable;

import java.util.Calendar;

public class AlarmBReceiver extends BroadcastReceiver {
    private final static int TWO_AM = 2;
    private final static int ZERO_MINUTES = 0;
    private final static long ONCE_PER_DAY = 1000 * 60 * 60 * 24;

    private static long getMillisUntilMorning2AM() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, TWO_AM);
        cal.set(Calendar.MINUTE, ZERO_MINUTES);

        Log.i("MovesHandler", "time until 2 " + Math.max(cal.getTimeInMillis() - System.currentTimeMillis(),System.currentTimeMillis()-cal.getTimeInMillis()));
        return (Math.max(cal.getTimeInMillis() - System.currentTimeMillis(), System.currentTimeMillis()-cal.getTimeInMillis()));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
        Handler mHandler;
        mHandler = new Handler();
        mHandler.post(new MovesRunnable(context, BackgroundService.getInstance().getHelper()));
        wl.release();
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmBReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, getMillisUntilMorning2AM(), ONCE_PER_DAY, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmBReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}


