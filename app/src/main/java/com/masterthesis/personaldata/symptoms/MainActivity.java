package com.masterthesis.personaldata.symptoms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.masterthesis.personaldata.symptoms.DAO.model.Symptom;
import com.masterthesis.personaldata.symptoms.fragments.DiaryFragment;
import com.masterthesis.personaldata.symptoms.fragments.HomeFragment;
import com.masterthesis.personaldata.symptoms.fragments.SymptomFragment;
import com.masterthesis.personaldata.symptoms.managers.DiaryManager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.Map;

import io.flic.lib.FlicBroadcastReceiverFlags;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnHomeFragmentInteractionListener
        , SymptomFragment.OnSymptomFragmentInteractionListener, DiaryFragment.OnDiaryFragmentListener {

    private static final String TAG = "MainActivity";
    static MainActivity mainActivity = null;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    Intent serviceIntent;
    FlicButton button;

    public static MainActivity getInstance() {
        return mainActivity;
    }


    public FlicButton getButton() {
        return button;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            checkRule(extras.getInt("rule", -1));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Home", HomeFragment.class)
                .add("Diary", DiaryFragment.class)
                .add("Symptom", SymptomFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        startActivity(new Intent(this, MovesActivity.class));


        serviceIntent = new Intent(this, BackgroundService.class);
        if (!Utils.isMyServiceRunning(BackgroundService.class, this)) {
            startService(serviceIntent);
        } else {
            Log.i(TAG, "Service running");
        }

        DiaryManager diaryManager = DiaryManager.getInstance();
        diaryManager.init(this);

        preferences = getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE);

        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
//        preferences.edit().clear().apply();

    }

    private void checkRule(int rule) {
        switch (rule) {
            case Constants.BUTTON_NOT_TRACKING:
                //TODO throw an alarm show that the user gets back his button
                Log.i(TAG, "Button out of sync or range maybe you lost it");
                break;
            case Constants.UNKNOWN_ERROR:
                //TODO Tell the user to contact me and give me details of the error or I will log them and get them somehow
                Log.i(TAG, "Rules : Invalid rule");
                break;
            case Constants.NEED_RESET:
                //TODO Restart everything and alarm the user to connect the button if needed
                Log.i(TAG, "Rules : Invalid rule");
                break;
            case Constants.SERVICE_CRASHED:
                //TODO Restart the service
                startService(serviceIntent);
                Log.i(TAG, "Rules : Invalid rule");
                break;
            default:
                Log.i(TAG, "Rules : Invalid rule");
                break;
        }
    }

//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
            @Override
            public void onInitialized(FlicManager manager) {
                button = manager.completeGrabButton(requestCode, resultCode, data);
                if (button != null) {
                    button.registerListenForBroadcast(FlicBroadcastReceiverFlags.ALL);
//                    button.setActiveMode(false);
                    Toast.makeText(MainActivity.this, "Grabbed a button", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "button id" + button.getButtonId());
                    Log.i(TAG, "button connection status" + button.getConnectionStatus());

                } else {
                    Toast.makeText(MainActivity.this, "Did not grab any button", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSymptomRegistration(Symptom symptom) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDiaryFragmentInteraction(Uri uri) {

    }
}
