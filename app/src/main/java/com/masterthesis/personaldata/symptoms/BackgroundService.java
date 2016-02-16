package com.masterthesis.personaldata.symptoms;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.masterthesis.personaldata.symptoms.managers.SymptomManager;
import com.masterthesis.personaldata.symptoms.timertasks.FlicTimerTask;

import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";

    MainActivity mainActivity = MainActivity.getInstance();

    public BackgroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        MovesTimerTask.startTask();
        FlicManager.setAppCredentials("59eab426-39a4-4457-8e7d-2f67f9733d54", "d0ef92f6-a494-4f3d-96c0-841c6b434909", "ScaleMeasurement");
        if (mainActivity.getButton() == null) {
            try {
                FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
                    @Override
                    public void onInitialized(FlicManager manager) {
                        manager.initiateGrabButton(MainActivity.getInstance());

                    }
                });
            } catch (FlicAppNotInstalledException err) {
                Toast.makeText(this, "Flic App is not installed", Toast.LENGTH_SHORT).show();
            }
        }
        SymptomManager symptomManager = SymptomManager.getInstance();
        symptomManager.init(this);
        runAsForeground();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void runAsForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, Intent.FILL_IN_ACTION);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentText(String.valueOf("Symptoms running"))
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);

    }


}
