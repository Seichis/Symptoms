package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

public class MovesManager {

    private static final String TAG = "Moves";

    private static final String CLIENT_ID = "U2hOIga3gar6ftAXiHzJ0e1YWAi0tNUF";
    private static final String CLIENT_SECRET = "JPYX6Xf57bG6j4y1JyfDdp25jQpDDo8Ds01Oh2oX0xDA0LkS86a4ul6BdPf21b8P";
    private static final String CLIENT_REDIRECTURL = "http://dfuinspector.com/";
    private static final String CLIENT_SCOPES = "activity location";

    private static MovesManager movesManager = new MovesManager();
    private MovesHandler<AuthData> authDialogHandler = new MovesHandler<AuthData>() {
        @Override
        public void onSuccess(AuthData arg0) {
            Log.i(TAG, "Access Token : " + arg0.getAccessToken() + "\n"
                    + "Expires In : " + arg0.getExpiresIn() + "\n"
                    + "User ID : " + arg0.getUserID());
        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            Log.i(TAG, "Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };
    private MovesHandler<ProfileData> profileHandler = new MovesHandler<ProfileData>() {

        @Override
        public void onSuccess(ProfileData arg0) {
            Log.i(TAG, "User ID : " + arg0.getUserID()
                    + "\nUser Platform : " + arg0.getPlatform());
        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            Log.i(TAG, "Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };
    private MovesHandler<ArrayList<SummaryListData>> summaryHandler = new MovesHandler<ArrayList<SummaryListData>>() {
        @Override
        public void onSuccess(ArrayList<SummaryListData> result) {
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
        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            Log.i(TAG, "Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };
    private MovesHandler<ArrayList<StorylineData>> storylineHandler = new MovesHandler<ArrayList<StorylineData>>() {
        @Override
        public void onSuccess(ArrayList<StorylineData> result) {
            for (StorylineData sld : result) {
                Log.i(TAG, "-Calories Idle : " + sld.getCaloriesIdle());
                Log.i(TAG, "-Last update : " + sld.getLastUpdate());
                Log.i(TAG, "-Date : " + sld.getDate());

                ArrayList<SegmentData> segments = sld.getSegments();
                for (SegmentData sd : segments) {
                    Log.i(TAG, "-Segments start time : " + sd.getStartTime());
                    Log.i(TAG, "-Segments end time : " + sd.getEndTime());
                    Log.i(TAG, "-Segments type : " + sd.getType());
                    if (sd.getPlace() != null) {
                        Log.i(TAG, "-Segments place foursquare: " + sd.getPlace().getFoursquareId());
                        Log.i(TAG, "-Segments place type : " + sd.getPlace().getType());
                        Log.i(TAG, "-Segments place name: " + sd.getPlace().getName());
                        Log.i(TAG, "-Segments place foursquare id : " + sd.getPlace().getFoursquareCategoryIds());
                        Log.i(TAG, "-Segments place location lat : " + sd.getPlace().getLocation().getLat());
                        Log.i(TAG, "-Segments place location lon : " + sd.getPlace().getLocation().getLon());
                    }
                    ArrayList<ActivityData> activities = sd.getActivities();
                    for (ActivityData ad : activities) {
                        Log.i(TAG, "--Activities: name : " + ad.getActivity());
                        Log.i(TAG, "--Activities: start time : " + ad.getStartTime());
                        Log.i(TAG, "--Activities: end time : " + ad.getEndTime());
                        Log.i(TAG, "--Activities: calories : " + ad.getCalories());
                        Log.i(TAG, "--Activities: manual : " + ad.getManual());
                        Log.i(TAG, "--Activities: distance : " + ad.getDistance());
                        Log.i(TAG, "--Activities: duration : " + ad.getDuration());
                        Log.i(TAG, "--Activities: group : " + ad.getGroup());
                        Log.i(TAG, "--Activities: steps : " + ad.getSteps());
                        ArrayList<TrackPointsData> trackPoints = ad.getTrackPoints();
                        for (TrackPointsData tpd : trackPoints) {
                            Log.i(TAG, "---Track points" + tpd.getLat() + " ; " + tpd.getLon());
                            Log.i(TAG, "---Track points" + tpd.getTime());
                        }


                    }

                }

                ArrayList<SummaryData> summaries = sld.getSummary();
                for (SummaryData sd : summaries) {
                    Log.i(TAG, "-Summary Activity : " + sd.getActivity());
                    Log.i(TAG, "-Summary Calories : " + sd.getCalories());
                    Log.i(TAG, "-Summary Distance : " + sd.getDistance());
                    Log.i(TAG, "-Summary Duration : " + sd.getDuration());
                    Log.i(TAG, "-Summary Group : " + sd.getGroup());
                    Log.i(TAG, "-Summary Steps : " + sd.getSteps());
                }

            }

        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            Log.i(TAG, "Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };

    private MovesManager() {
    }

    public static MovesManager getInstance() {
        return movesManager;
    }


    public void init(Context _context) {
        try {
            MovesAPI.init(_context, CLIENT_ID, CLIENT_SECRET, CLIENT_SCOPES, CLIENT_REDIRECTURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void authenticate(AppCompatActivity activity) {
        MovesAPI.authenticate(movesManager.authDialogHandler, activity);
    }

    public AuthData getAuthData() {

        return MovesAPI.getAuthData();
    }

    public void profile() {
        MovesAPI.getProfile(movesManager.profileHandler);
    }

    /**
     * @param day Format example "20160212"
     */
    public void summarySingleDay(String day) {
        MovesAPI.getSummary_SingleDay(movesManager.summaryHandler, day, null);
    }

    /**
     * @param week Format example "2016-W06"
     */
    public void summaryWeek(String week) {
        MovesAPI.getSummary_SpecificWeek(movesManager.summaryHandler, week, null);
    }

    /**
     * @param month Format example "201602"
     */
    public void summaryMonth(String month) {
        MovesAPI.getSummary_SpecificMonth(movesManager.summaryHandler, month, null);
    }

    /**
     * @param start Format example "20160211"
     * @param end   Format example "20160212"
     */
    public void summaryRange(String start, String end) {
        MovesAPI.getSummary_WithinRange(movesManager.summaryHandler, start, end, null);
    }

    /**
     * @param pastDays Format example "2"
     */
    public void summaryPastDays(String pastDays) {
        MovesAPI.getSummary_PastDays(movesManager.summaryHandler, pastDays, null);

    }

    /**
     * @param day Format example "20160212"
     * @param trp To return track points or not
     */
    public void storylineDay(String day, boolean trp) {
        MovesAPI.getStoryline_SingleDay(movesManager.storylineHandler, day, null, trp);
    }

    /**
     * @param week Format example "2016-W06"
     * @param trp  To return track points or not
     */
    public void storylineWeek(String week, boolean trp) {
        MovesAPI.getStoryline_SpecificWeek(movesManager.storylineHandler, week, null, trp);
    }

    /**
     * @param month Format example "201602"
     */
    public void storylineMonth(String month) {
        MovesAPI.getStoryline_SpecificMonth(movesManager.storylineHandler, month, null, false);
    }

    /**
     * @param start Format example "20160211"
     * @param end   Format example "20160212"
     * @param trp   To return track points or not
     */
    public void storylineRange(String start, String end, boolean trp) {
        MovesAPI.getStoryline_WithinRange(movesManager.storylineHandler, start, end, null, trp);
    }

    /**
     * @param pastDays Format example "2"
     * @param trp      To return track points or not
     */
    public void storylinePastDays(String pastDays, boolean trp) {
        MovesAPI.getStoryline_PastDays(movesManager.storylineHandler, pastDays, null, trp);
    }

    /**
     * @param day Format example "20160212"
     */
    public void activitiesDay(String day) {
        MovesAPI.getActivities_SingleDay(movesManager.storylineHandler, day, null);
    }

    /**
     * @param week Format example "2016-W06"
     */
    public void activitiesWeek(String week) {
        MovesAPI.getActivities_SpecificWeek(movesManager.storylineHandler, week, null);
    }

    /**
     * @param month Format example "201602"
     */
    public void activitiesMonth(String month) {
        MovesAPI.getActivities_SpecificMonth(movesManager.storylineHandler, "201602", null);
    }

    /**
     * @param start Format example "20160211"
     * @param end   Format example "20160212"
     */
    public void activitiesRange(String start, String end) {
        MovesAPI.getActivities_WithinRange(movesManager.storylineHandler, "20160211", "20160212", null);
    }

    /**
     * @param pastDays Format example "2"
     */
    public void activitiesPastDays(String pastDays) {
        MovesAPI.getActivities_PastDays(movesManager.storylineHandler, "31", null);
    }


}



