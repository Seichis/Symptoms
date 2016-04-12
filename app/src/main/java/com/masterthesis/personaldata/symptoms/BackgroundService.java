package com.masterthesis.personaldata.symptoms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.broadcastreceivers.AlarmBReceiver;
import com.masterthesis.personaldata.symptoms.broadcastreceivers.CustomFlicBroadcastReceiver;
import com.masterthesis.personaldata.symptoms.managers.SymptomManager;

import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class BackgroundService extends OrmLiteBaseService<DatabaseHelper> {

    private final static String LOG_NAME = BackgroundService.class.getName();
    private static final int notif_id = 1337;
    static Notification notification;
    private static PendingIntent pi;
    private static BackgroundService backgroundService;
    MainActivity mainActivity = null;

    AlarmBReceiver alarmBReceiver = null;

    public BackgroundService() {
    }

    public static BackgroundService getInstance() {
        return backgroundService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        CustomFlicBroadcastReceiver customFlicBroadcastReceiver = new CustomFlicBroadcastReceiver();
        mainActivity = MainActivity.getInstance();

//        preferences = this.getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE);
//        editor = preferences.edit();
        backgroundService = this;
        runAsForeground();
        FlicManager.setAppCredentials("59eab426-39a4-4457-8e7d-2f67f9733d54", "d0ef92f6-a494-4f3d-96c0-841c6b434909", "ScaleMeasurement");
        if (mainActivity != null) {
            if (mainActivity.getButton() == null) {
                try {
                    FlicManager.getInstance(backgroundService, new FlicManagerInitializedCallback() {
                        @Override
                        public void onInitialized(FlicManager manager) {
                            manager.initiateGrabButton(MainActivity.getInstance());

                        }
                    });
                } catch (FlicAppNotInstalledException err) {
                    Toast.makeText(backgroundService, "Flic App is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        SymptomManager symptomManager = SymptomManager.getInstance();
        symptomManager.init(backgroundService);

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
        PendingIntent contentIntent = PendingIntent.getActivity(backgroundService,
                0, new Intent(backgroundService, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        notification = getMyActivityNotification("No run yet", contentIntent);
//        if (alarmBReceiver==null) {
//            alarmBReceiver = new AlarmBReceiver();
//            alarmBReceiver.setAlarm(backgroundService);
//        }
        backgroundService.startForeground(notif_id, notification);
    }


    private Notification getMyActivityNotification(String text, PendingIntent pendingIntent) {
        // The PendingIntent to launch our activity if the user selects
        // this notification
        CharSequence title = getText(R.string.app_name);

        return new Notification.Builder(backgroundService)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
                .setContentIntent(pendingIntent).build();
    }

    /**
     * This is the method that can be called to update the Notification
     */
    public void updateNotification(String text, PendingIntent pendingIntent) {


        notification = getMyActivityNotification(text, pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notif_id, notification);
    }



}
