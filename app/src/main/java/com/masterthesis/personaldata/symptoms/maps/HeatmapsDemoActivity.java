/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.masterthesis.personaldata.symptoms.maps;

import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.masterthesis.personaldata.symptoms.R;
import com.masterthesis.personaldata.symptoms.managers.DiaryManager;
import com.masterthesis.personaldata.symptoms.managers.SymptomManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * A demo of the Heatmaps library. Demonstrates how the HeatmapTileProvider can be used to create
 * a colored map overlay that visualises many points of weighted importance/intensity, with
 * different colors representing areas of high and low concentration/combined intensity of points.
 */
public class HeatmapsDemoActivity extends BaseDemoActivity {

    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {
            0.0f, 0.10f, 0.20f, 0.60f, 1.0f
    };
    /**
     * Alternative radius for convolution
     */
    private static final int ALT_HEATMAP_RADIUS = 10;

    private static final String TAG = "Heatmap";
    /**
     * Alternative opacity of heatmap overlay
     */
    private static final double ALT_HEATMAP_OPACITY = 0.4;
    /**
     * Alternative heatmap gradient (blue -> red)
     * Copied from Javascript version
     */
    private static final int[] ALT_HEATMAP_GRADIENT_COLORS = {
            Color.argb(0, 0, 255, 255),// transparent
            Color.argb(255 / 3 * 2, 0, 255, 255),
            Color.rgb(0, 191, 255),
            Color.rgb(0, 0, 127),
            Color.rgb(255, 0, 0)
    };
    public static final Gradient ALT_HEATMAP_GRADIENT = new Gradient(ALT_HEATMAP_GRADIENT_COLORS,
            ALT_HEATMAP_GRADIENT_START_POINTS);
    TreeMap<Integer, String> symptomTypes;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private boolean mDefaultGradient = true;
    private boolean mDefaultRadius = true;
    private boolean mDefaultOpacity = true;
    /**
     * Maps name of data set to data (list of LatLngs)
     * Also maps to the URL of the data set for attribution
     */
    private HashMap<String, DataSet> mLists = new HashMap<String, DataSet>();

    @Override
    protected int getLayoutId() {
        return R.layout.heatmaps_demo;
    }

    @Override
    protected void startDemo() {

        symptomTypes = DiaryManager.getInstance().getActiveDiary().getSymptomTypes();
        for (Integer key : symptomTypes.keySet()) {

            ArrayList<LatLng> pos = SymptomManager.getInstance().getSymptomslatLonByType(symptomTypes.get(key));
//            Log.i(TAG, "pos "+ String.valueOf(pos));
            if (!pos.isEmpty()) {
                Log.i(TAG, symptomTypes.get(key) + String.valueOf(pos));

                mLists.put(symptomTypes.get(key), new DataSet(pos,
                        symptomTypes.get(key)));
            }
        }

        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.6761, 12.5683), 10));

        // Set up the spinner/dropdown list
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(mLists.keySet()));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerActivity());


        // Make the handler deal with the map
        // Input: list of WeightedLatLngs, minimum and maximum zoom levels to calculate custom
        // intensity from, and the map to draw the heatmap on
        // radius, gradient and opacity not specified, so default are used
    }

    public void changeRadius(View view) {
        if (mDefaultRadius) {
            mProvider.setRadius(ALT_HEATMAP_RADIUS);
        } else {
            mProvider.setRadius(HeatmapTileProvider.DEFAULT_RADIUS);
        }
        mOverlay.clearTileCache();
        mDefaultRadius = !mDefaultRadius;
    }

    public void changeGradient(View view) {
        if (mDefaultGradient) {
            mProvider.setGradient(ALT_HEATMAP_GRADIENT);
        } else {
            mProvider.setGradient(HeatmapTileProvider.DEFAULT_GRADIENT);
        }
        mOverlay.clearTileCache();
        mDefaultGradient = !mDefaultGradient;
    }

    public void changeOpacity(View view) {
        if (mDefaultOpacity) {
            mProvider.setOpacity(ALT_HEATMAP_OPACITY);
        } else {
            mProvider.setOpacity(HeatmapTileProvider.DEFAULT_OPACITY);
        }
        mOverlay.clearTileCache();
        mDefaultOpacity = !mDefaultOpacity;
    }

    // Dealing with spinner choices
    public class SpinnerActivity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            TextView attribution = ((TextView) findViewById(R.id.attribution));

            // Check if need to instantiate (avoid setData etc twice)
            if (mProvider == null) {
                mProvider = new HeatmapTileProvider.Builder().data(
                        mLists.get(symptomTypes.get(pos + 1)).getData()).build();
                mOverlay = getMap().addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                // Render links
                attribution.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                mProvider.setData(mLists.get(symptomTypes.get(pos + 1)).getData());
                mOverlay.clearTileCache();
            }
            // Update attribution
            attribution.setText(Html.fromHtml(String.format(getString(R.string.attrib_format),
                    mLists.get(symptomTypes.get(pos + 1)).getUrl())));

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    /**
     * Helper class - stores data sets and sources.
     */
    private class DataSet {
        private ArrayList<LatLng> mDataset;
        private String mUrl;

        public DataSet(ArrayList<LatLng> dataSet, String url) {
            this.mDataset = dataSet;
            this.mUrl = url;
        }

        public ArrayList<LatLng> getData() {
            return mDataset;
        }

        public String getUrl() {
            return mUrl;
        }
    }

}
