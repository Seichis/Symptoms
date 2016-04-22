package com.masterthesis.personaldata.symptoms;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.masterthesis.personaldata.symptoms.DAO.model.Settings;
import com.masterthesis.personaldata.symptoms.DAO.model.Symptom;
import com.masterthesis.personaldata.symptoms.fragments.DiaryFragment;
import com.masterthesis.personaldata.symptoms.fragments.FirstRunSetupFragment;
import com.masterthesis.personaldata.symptoms.fragments.HomeFragment;
import com.masterthesis.personaldata.symptoms.fragments.NoFlicFragment;
import com.masterthesis.personaldata.symptoms.fragments.SymptomFragment;
import com.masterthesis.personaldata.symptoms.managers.SettingsManager;
import com.masterthesis.personaldata.symptoms.maps.HeatmapsDemoActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.flic.lib.FlicBroadcastReceiverFlags;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnHomeFragmentInteractionListener
        , SymptomFragment.OnSymptomFragmentInteractionListener, DiaryFragment.OnDiaryFragmentListener, NoFlicFragment.onNoFlicFragmentListener, FirstRunSetupFragment.OnFirstRunSetupListener {

    private static final String TAG = "WelcomeMain";
    static MainActivity mainActivity;
    //    private static SharedPreferences preferences;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.viewpagertab)
    SmartTabLayout viewPagerTab;
    FragmentPagerItemAdapter adapter;
    Intent serviceIntent;
    FlicButton button;
    HashMap<String, Class<? extends Fragment>> fragmentTitleMap;
    Settings settings;

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
        ButterKnife.bind(mainActivity);
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            checkRule(extras.getInt("rule", -1));
        }

        setupLayout();
        startBackgroundService();
//        startActivity(new Intent(this, ActivityRecognitionActivity.class));
//        startActivity(new Intent(this, WelcomeMain.class));
    }

    private void startBackgroundService() {
        serviceIntent = new Intent(this, BackgroundService.class);
        if (!Utils.isMyServiceRunning(BackgroundService.class, this)) {
            startService(serviceIntent);
        } else {
            Log.i(TAG, "Service running");
        }
    }

    private void setupLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



//        FragmentPagerItems.Creator creator = FragmentPagerItems.with(mainActivity);
//
//        creator.add("Home", HomeFragment.class);
//        settings = SettingsManager.getInstance().getSettings();
//
//
//        creator.add("Home", HomeFragment.class);
//        adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), creator.create());
//        viewPager.setAdapter(adapter);
//
//        viewPagerTab.setViewPager(viewPager);
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

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
            @Override
            public void onInitialized(FlicManager manager) {
                button = manager.completeGrabButton(requestCode, resultCode, data);
                if (button != null) {
                    button.registerListenForBroadcast(FlicBroadcastReceiverFlags.UP_OR_DOWN | FlicBroadcastReceiverFlags.REMOVED);
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
        settings = SettingsManager.getInstance().getSettings();
        if (settings == null) {
            startActivity(new Intent(mainActivity,CreatePatientActivity.class));
        }else{
            Log.i("settings",settings.getPatient().getPatientName());
        }
//
////        preferences = getSharedPreferences("com.masterthesis.personaldata.symptoms", MODE_PRIVATE);
//        settings = SettingsManager.getInstance().getSettings();
//        fragmentTitleMap = new HashMap<>();
//
////        if (preferences.getBoolean("firstrun", true)) {
////            // Do first run stuff here then set 'firstrun' as false
////            // using the following line to edit/commit prefs
////            preferences.edit().putBoolean("firstrun", false).apply();
////        }
//        if (settings != null) {
//            if (settings.isFlicActivated()) {
//                fragmentTitleMap.put("Home", HomeFragment.class);
//            } else {
//                fragmentTitleMap.put("Diary", DiaryFragment.class);
//                fragmentTitleMap.put("Symptom", SymptomFragment.class);
//            }
//            updateAdapter(fragmentTitleMap);
//        } else {
//            fragmentTitleMap.put("Diary", DiaryFragment.class);
//            fragmentTitleMap.put("Symptom", SymptomFragment.class);
//        }

    }


    private void updateAdapter(final HashMap<String, Class<? extends Fragment>> titleMap) {

        FragmentPagerItems.Creator creator = FragmentPagerItems.with(mainActivity);


        for (String s : titleMap.keySet()) {
            creator.add(s, titleMap.get(s));
        }
        adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), creator.create());
        viewPager.setAdapter(adapter);
        viewPager.invalidate();

        viewPagerTab.setViewPager(viewPager);
        viewPagerTab.invalidate();

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

        if (id == R.id.manage_symptoms) {
            // Handle the camera action
        } else if (id == R.id.symptoms_on_map) {
            startActivity(new Intent(this, HeatmapsDemoActivity.class));
        } else if (id == R.id.raw_history) {

        } else if (id == R.id.app_settings) {
            startActivity(new Intent(mainActivity, SettingsActivity.class));
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

    @Override
    public void onNoFlicFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFirstRunSetupInteraction(Uri uri) {

    }
}
