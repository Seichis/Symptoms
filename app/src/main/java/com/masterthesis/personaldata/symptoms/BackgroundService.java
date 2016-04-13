package com.masterthesis.personaldata.symptoms;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.broadcastreceivers.AlarmBReceiver;
import com.masterthesis.personaldata.symptoms.managers.DiaryManager;
import com.masterthesis.personaldata.symptoms.managers.SymptomManager;

import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class BackgroundService extends OrmLiteBaseService<DatabaseHelper> implements LocationListener,
        GoogleApiClient.ConnectionCallbacks {

    private final static String TAG = BackgroundService.class.getName();
    private static final int notif_id = 1337;
    static Notification notification;
    private static PendingIntent pi;
    private static BackgroundService backgroundService;
    MainActivity mainActivity = null;
    AlarmBReceiver alarmBReceiver = null;
    AlertDialog alert;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    private Location currentLocation;
    private GoogleApiClient locationClient;

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
        mainActivity = MainActivity.getInstance();

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
        buildAlertMessageNoGps();
        DiaryManager diaryManager = DiaryManager.getInstance();
        diaryManager.init(backgroundService);
        SymptomManager symptomManager = SymptomManager.getInstance();
        symptomManager.init(backgroundService);


        // Create a new location client, using the enclosing class to handle callbacks.
        locationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
        locationClient.connect();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // If the client is connected
        if (locationClient.isConnected()) {
            // After disconnect() is called, the client is considered "dead".
            locationClient.disconnect();

        }
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        currentLocation = getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    private void buildAlertMessageNoGps() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            alert = builder.create();

            alert.show();
        }

    }


    private Location getLocation() {
        // If Google Play Services is available
        if (servicesConnected()) {
            // Get the current location
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkPermissions();
                Log.i(TAG, "no permissions");
                return null;
            }
            Log.i(TAG, "getting  location");

            return LocationServices.FusedLocationApi.getLastLocation(locationClient);
        } else {
            return null;
        }
    }

    protected void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { // Permission was added in API Level 16
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                if (mainActivity != null) {
                    mainActivity.requestFineLocationPermission();
                }
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                if (mainActivity != null) {
                    mainActivity.requestCoarseLocationPermission();
                }
            }
        }
    }


    /*
    * Verify that Google Play services is available before making a request.
    *
    * @return true if Google Play services is available, otherwise false
    */
    private boolean servicesConnected() {
        // Check that Google Play services is available
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            if (MyApplication.APPDEBUG) {
                // In debug mode, log the status
                Log.d(MyApplication.APPTAG, "Google play services available");
            }
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = googleAPI.getErrorDialog(mainActivity, resultCode, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(mainActivity.getSupportFragmentManager(), MyApplication.APPTAG);
            }
            return false;
        }
    }

    /*
     * Define a DialogFragment to display the error dialog generated in showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /*
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}


