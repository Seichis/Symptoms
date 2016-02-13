package com.masterthesis.personaldata.symptoms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.midhunarmid.movesapi.MovesAPI;
import com.midhunarmid.movesapi.MovesHandler;
import com.midhunarmid.movesapi.activity.ActivityData;
import com.midhunarmid.movesapi.activity.TrackPointsData;
import com.midhunarmid.movesapi.auth.AuthData;
import com.midhunarmid.movesapi.profile.ProfileData;
import com.midhunarmid.movesapi.segment.SegmentData;
import com.midhunarmid.movesapi.storyline.StorylineData;
import com.midhunarmid.movesapi.summary.SummaryData;
import com.midhunarmid.movesapi.summary.SummaryListData;
import com.midhunarmid.movesapi.util.MovesStatus;

import java.util.ArrayList;

public class MovesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Moves";

    private static final String CLIENT_ID = "U2hOIga3gar6ftAXiHzJ0e1YWAi0tNUF";
    private static final String CLIENT_SECRET = "JPYX6Xf57bG6j4y1JyfDdp25jQpDDo8Ds01Oh2oX0xDA0LkS86a4ul6BdPf21b8P";
    private static final String CLIENT_REDIRECTURL = "http://dfuinspector.com/";
    private static final String CLIENT_SCOPES = "activity location";
    private static final int REQUEST_AUTHORIZE = 1;

    private Spinner mSpinnerAPI;
    private Button mButtonSubmit;
    private ProgressBar mProgressRequest;
    private TextView mTvResponse;
    private MovesHandler<AuthData> authDialogHandler = new MovesHandler<AuthData>() {
        @Override
        public void onSuccess(AuthData arg0) {
            toggleProgress(false);
            updateResponse("Access Token : " + arg0.getAccessToken() + "\n"
                    + "Expires In : " + arg0.getExpiresIn() + "\n"
                    + "User ID : " + arg0.getUserID());
        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            toggleProgress(false);
            updateResponse("Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };
    private MovesHandler<ProfileData> profileHandler = new MovesHandler<ProfileData>() {

        @Override
        public void onSuccess(ProfileData arg0) {
            toggleProgress(false);
            updateResponse("User ID : " + arg0.getUserID()
                    + "\nUser Platform : " + arg0.getPlatform());
        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            toggleProgress(false);
            updateResponse("Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };
    private MovesHandler<ArrayList<SummaryListData>> summaryHandler = new MovesHandler<ArrayList<SummaryListData>>() {
        @Override
        public void onSuccess(ArrayList<SummaryListData> result) {
            toggleProgress(false);
            for (SummaryListData sld : result) {
                Log.i(TAG, "Calories Idle : " + sld.getCaloriesIdle());
                Log.i(TAG, "Last update : " + sld.getLastUpdate());
                Log.i(TAG, "Date : " + sld.getDate());

                ArrayList<SummaryData> summaries = sld.getSummaries();
                for (SummaryData sd : summaries) {
                    Log.i(TAG, "Activity : " + sd.getActivity());
                    Log.i(TAG, "Calories : " + sd.getCalories());
                    Log.i(TAG, "Distance : " + sd.getDistance());
                    Log.i(TAG, "Duration : " + sd.getDuration());
                    Log.i(TAG, "Group : " + sd.getGroup());
                    Log.i(TAG, "Steps : " + sd.getSteps());
                }

            }
            updateResponse("Summary Items : " + result.size());
        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            toggleProgress(false);
            updateResponse("Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };
    private MovesHandler<ArrayList<StorylineData>> storylineHandler = new MovesHandler<ArrayList<StorylineData>>() {
        @Override
        public void onSuccess(ArrayList<StorylineData> result) {
            toggleProgress(false);
            for (StorylineData sld : result) {
                Log.i(TAG, "Calories Idle : " + sld.getCaloriesIdle());
                Log.i(TAG, "Last update : " + sld.getLastUpdate());
                Log.i(TAG, "Date : " + sld.getDate());

                ArrayList<SegmentData> segments = sld.getSegments();
                for (SegmentData sd : segments) {
                    Log.i(TAG, "Segments start time : " + sd.getStartTime());
                    Log.i(TAG, "Segments end time : " + sd.getEndTime());
                    Log.i(TAG, "Segments type : " + sd.getType());
                    if (sd.getPlace() != null) {
                        Log.i(TAG, "Segments place foursquare: " + sd.getPlace().getFoursquareId());
                        Log.i(TAG, "Segments place type : " + sd.getPlace().getType());
                        Log.i(TAG, "Segments place name: " + sd.getPlace().getName());
                        Log.i(TAG, "Segments place foursquare id : " + sd.getPlace().getFoursquareCategoryIds());
                        Log.i(TAG, "Segments place location : " + sd.getPlace().getLocation());
                    }
                    ArrayList<ActivityData> activities = sd.getActivities();
                    for (ActivityData ad : activities) {
                        Log.i(TAG, "Activities: name : " + ad.getActivity());
                        Log.i(TAG, "Activities: start time : " + ad.getStartTime());
                        Log.i(TAG, "Activities: end time : " + ad.getEndTime());
                        Log.i(TAG, "Activities: calories : " + ad.getCalories());
                        Log.i(TAG, "Activities: manual : " + ad.getManual());
                        Log.i(TAG, "Activities: distance : " + ad.getDistance());
                        Log.i(TAG, "Activities: duration : " + ad.getDuration());
                        Log.i(TAG, "Activities: group : " + ad.getGroup());
                        Log.i(TAG, "Activities: steps : " + ad.getSteps());
                        ArrayList<TrackPointsData> trackPoints = ad.getTrackPoints();
                        for (TrackPointsData tpd : trackPoints) {
                            Log.i(TAG, "Track points" + tpd.getLat());
                            Log.i(TAG, "Track points" + tpd.getLat());
                            Log.i(TAG, "Track points" + tpd.getLat());
                        }
                        Log.i(TAG, "Activities: start time : " + ad.getTrackPoints());


                    }

                }

                ArrayList<SummaryData> summaries = sld.getSummary();
                for (SummaryData sd : summaries) {
                    Log.i(TAG, "Activity : " + sd.getActivity());
                    Log.i(TAG, "Calories : " + sd.getCalories());
                    Log.i(TAG, "Distance : " + sd.getDistance());
                    Log.i(TAG, "Duration : " + sd.getDuration());
                    Log.i(TAG, "Group : " + sd.getGroup());
                    Log.i(TAG, "Steps : " + sd.getSteps());
                }

            }

            updateResponse("Summary Items : " + result.size());
        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            toggleProgress(false);
            updateResponse("Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moves);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSpinnerAPI = (Spinner) findViewById(R.id.spinnerAPI);
        mButtonSubmit = (Button) findViewById(R.id.buttonSubmit);
        mTvResponse = (TextView) findViewById(R.id.tvResponse);
        mProgressRequest = (ProgressBar) findViewById(R.id.progressRequest);

        mButtonSubmit.setOnClickListener(this);

        try {
            MovesAPI.init(this, CLIENT_ID, CLIENT_SECRET, CLIENT_SCOPES, CLIENT_REDIRECTURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        toggleProgress(true);
        switch (mSpinnerAPI.getSelectedItemPosition()) {
            case 0: // Authenticate
                MovesAPI.authenticate(authDialogHandler, MovesActivity.this);
                break;
            case 1: // Get Auth Data
                AuthData auth = MovesAPI.getAuthData();
                if (auth != null) {
                    updateResponse("Access Token : " + auth.getAccessToken() + "\n"
                            + "Expires In : " + auth.getExpiresIn() + "\n"
                            + "User ID : " + auth.getUserID());
                } else {
                    updateResponse("Not Authenticated Yet!");
                }
                toggleProgress(false);
                break;
            case 2: // Get Profile
                MovesAPI.getProfile(profileHandler);
                break;
            case 4: // Get Summary Day
                MovesAPI.getSummary_SingleDay(summaryHandler, "20160212", null);
                break;
            case 5: // Get Summary Week
                MovesAPI.getSummary_SpecificWeek(summaryHandler, "2016-W06", null);
                break;
            case 6: // Get Summary Month
                MovesAPI.getSummary_SpecificMonth(summaryHandler, "201602", null);
                break;
            case 7: // Get Summary Range
                MovesAPI.getSummary_WithinRange(summaryHandler, "20160211", "20160212", null);
                break;
            case 8: // Get Summary PastDays
                MovesAPI.getSummary_PastDays(summaryHandler, "2", null);
                break;
            case 10: // Get Storyline Day
                MovesAPI.getStoryline_SingleDay(storylineHandler, "20160212", null, false);
                break;
            case 11: // Get Storyline Week
                MovesAPI.getStoryline_SpecificWeek(storylineHandler, "2016-W06", null, false);
                break;
            case 12: // Get Storyline Month
                MovesAPI.getStoryline_SpecificMonth(storylineHandler, "201601", null, false);
                break;
            case 13: // Get Storyline Range
                MovesAPI.getStoryline_WithinRange(storylineHandler, "20160210", "20160212", null, true);
                break;
            case 14: // Get Storyline PastDays
                MovesAPI.getStoryline_PastDays(storylineHandler, "2", null, true);
                break;
            case 16: // Get Activities Day
                MovesAPI.getActivities_SingleDay(storylineHandler, "20160212", null);
                break;
            case 17: // Get Activities Week
                MovesAPI.getActivities_SpecificWeek(storylineHandler, "2016-W06", null);
                break;
            case 18: // Get Activities Month
                MovesAPI.getActivities_SpecificMonth(storylineHandler, "201602", "20140314T073812Z");
                break;
            case 19: // Get Activities Range
                MovesAPI.getActivities_WithinRange(storylineHandler, "20160211", "20160212", null);
                break;
            case 20: // Get Activities PastDays
                MovesAPI.getActivities_PastDays(storylineHandler, "31", null);
                break;
            default:
                toggleProgress(false);
                break;
        }
    }

    public void toggleProgress(final boolean isProgrressing) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressRequest.setVisibility(isProgrressing ? View.VISIBLE : View.GONE);
                mButtonSubmit.setVisibility(isProgrressing ? View.GONE : View.VISIBLE);
            }
        });
    }

    public void updateResponse(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvResponse.setText(String.valueOf(message));
            }
        });
    }
}