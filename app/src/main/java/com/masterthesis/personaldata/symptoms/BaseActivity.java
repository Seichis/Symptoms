package com.masterthesis.personaldata.symptoms;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


/**
 * Created by Konstantinos Michail on 2/11/2016.
 */
public class BaseActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    public static final int REQUEST_FINE_LOCATION = 0;
    private static final int REQUEST_COARSE_LOCATION = 1;
    private static final String TAG = "BaseActivity";
    /*
     * Other class member variables
     */
    private Location currentLocation;
    /**
     * Requests the fine location permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    @Nullable
    View view;
    // A request to connect to Location Services
    private LocationRequest locationRequest;
    // Stores the current instantiation of the location client in this object
    private GoogleApiClient locationClient;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildAlertMessageNoGps();
        // Create a new global location parameters object
        locationRequest = LocationRequest.create();

        // Set the update interval
        locationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        locationRequest.setFastestInterval(Constants.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        // Create a new location client, using the enclosing class to handle callbacks.
        locationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationClient.connect();


        try {
            view = this.findViewById(android.R.id.content).getRootView();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /*
       * Called when the Activity is no longer visible at all. Stop updates and disconnect.
       */
    @Override
    public void onStop() {
        // If the client is connected
        if (locationClient.isConnected()) {
            stopPeriodicUpdates();
        }

        // After disconnect() is called, the client is considered "dead".
        locationClient.disconnect();

        super.onStop();
    }

    /**
     * Requests the Coarse location permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestCoarseLocationPermission() {

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            Snackbar.make(view, R.string.permission_location_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(getParent(),
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_COARSE_LOCATION);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_LOCATION);
        }
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {
        super.onStart();

        // Connect to the location services client
    }

    /*
     * Called when the Activity is resumed. Updates the view.
     */
    @Override
    protected void onResume() {
        super.onResume();

    }

    /*
     * Handle results returned to this Activity by other Activities started with
     * startActivityForResult(). In particular, the method onConnectionFailed() in
     * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to start
     * an Activity that handles Google Play services problems. The result of this call returns here,
     * to onActivityResult.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST:

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        if (MyApplication.APPDEBUG) {
                            // Log the result
                            Log.d(MyApplication.APPTAG, "Connected to Google Play services");
                        }

                        break;

// If any other result was returned by Google Play services
                    default:
                        if (MyApplication.APPDEBUG) {
                            // Log the result
                            Log.d(MyApplication.APPTAG, "Could not connect to Google Play services");
                        }
                        break;
                }

// If any other request code was received
            default:
                if (MyApplication.APPDEBUG) {
                    // Report that this Activity received an unknown requestCode
                    Log.d(MyApplication.APPTAG, "Unknown request code received for the activity");
                }
                break;
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
            Dialog dialog = googleAPI.getErrorDialog(this,resultCode,0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), MyApplication.APPTAG);
            }
            return false;
        }
    }

    /*
     * Called by Location Services when the request to connect the client finishes successfully. At
     * this point, you can request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        if (MyApplication.APPDEBUG) {
            Log.d("Connected loc serv", MyApplication.APPTAG);
        }
        startPeriodicUpdates();
        currentLocation = getLocation();
        if (currentLocation != null) {
            Log.i(TAG, currentLocation.toString());
        } else {
            Log.i(TAG, "paparia");

        }
    }


    AlertDialog alert;

    private void buildAlertMessageNoGps() {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            recreate();
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


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_FINE_LOCATION) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Snackbar.make(view, R.string.permision_available_location,
                        Snackbar.LENGTH_SHORT).show();
                currentLocation=getLocation();
            } else {
                Snackbar.make(view, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT).show();

            }
            // END_INCLUDE(permission_result)


        } else if (requestCode == REQUEST_COARSE_LOCATION) {
            // BEGIN_INCLUDE(permission_result)

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Snackbar.make(view, R.string.permision_available_location,
                        Snackbar.LENGTH_SHORT).show();
                currentLocation=getLocation();
            } else {
                Snackbar.make(view, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT).show();

            }
            // END_INCLUDE(permission_result)


        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestFineLocationPermission() {
        // BEGIN_INCLUDE(camera_permission_request)

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(view, R.string.permission_location_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(getParent(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_FINE_LOCATION);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION);
        }
        // END_INCLUDE(camera_permission_request)
    }

    /*
     * Called by Location Services if the connection to the location client drops because of an error.
     */
    public void onDisconnected() {
        if (MyApplication.APPDEBUG) {
            Log.d("No location services", MyApplication.APPTAG);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(MyApplication.APPTAG, "GoogleApiClient connection has been suspend");
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Google Play services can resolve some errors it detects. If the error has a resolution, try
        // sending an Intent to start a Google Play services activity that can resolve error.
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {

                if (MyApplication.APPDEBUG) {
                    // Thrown if Google Play services canceled the original PendingIntent
                    Log.d(MyApplication.APPTAG, "An error occurred when connecting to location services.", e);
                }
            }
        } else {
            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    /*
     * In response to a request to start updates, send a request to Location Services
     */
    private void startPeriodicUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkPermissions();
            Log.i(TAG,"no permissions");

            return;
        }
        Log.i(TAG,"periodic updates started");
        LocationServices.FusedLocationApi.requestLocationUpdates(locationClient, locationRequest, this);
    }

    /*
     * In response to a request to stop updates, send a request to Location Services
     */
    private void stopPeriodicUpdates() {
        locationClient.disconnect();
    }

    /*
     * Get the current location
     */
    private Location getLocation() {
        // If Google Play Services is available
        if (servicesConnected()) {
            // Get the current location
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkPermissions();
                Log.i(TAG,"no permissions");
                return null;
            }
            Log.i(TAG,"getting  location");

            return LocationServices.FusedLocationApi.getLastLocation(locationClient);
        } else {
            return null;
        }
    }

    protected void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { // Permission was added in API Level 16
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestFineLocationPermission();
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestCoarseLocationPermission();
            }
        }
    }

    /*
     * Show a dialog returned by Google Play services for the connection error code
     */
    private void showErrorDialog(int errorCode) {
        // Get the error dialog from Google Play services
        Dialog errorDialog =
                GooglePlayServicesUtil.getErrorDialog(errorCode, this,
                        Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorDialog.show();
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
