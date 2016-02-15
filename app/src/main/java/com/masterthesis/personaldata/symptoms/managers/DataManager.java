package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.masterthesis.personaldata.symptoms.datamodel.SymptomContext;
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
import java.util.Observable;
import java.util.Observer;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherExceptionListener;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;

/**
 * Created by Konstantinos Michail on 2/14/2016.
 */
public class DataManager implements YahooWeatherInfoListener,
        YahooWeatherExceptionListener, Observer {

    private static final String TAG2 = "Moves";

    private static final String CLIENT_ID = "U2hOIga3gar6ftAXiHzJ0e1YWAi0tNUF";
    private static final String CLIENT_SECRET = "JPYX6Xf57bG6j4y1JyfDdp25jQpDDo8Ds01Oh2oX0xDA0LkS86a4ul6BdPf21b8P";
    private static final String CLIENT_REDIRECTURL = "http://dfuinspector.com/";
    private static final String CLIENT_SCOPES = "activity location";
    private static final String TAG = "DataManager";
    private static DataManager dataManager = new DataManager();
    private static Context context;
    SymptomContext symptomContext;
    private YahooWeather mYahooWeather;
    private MovesHandler<AuthData> authDialogHandler = new MovesHandler<AuthData>() {
        @Override
        public void onSuccess(AuthData arg0) {
            Log.i(TAG2, "Access Token : " + arg0.getAccessToken() + "\n"
                    + "Expires In : " + arg0.getExpiresIn() + "\n"
                    + "User ID : " + arg0.getUserID());
        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            Log.i(TAG2, "Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };
    private MovesHandler<ProfileData> profileHandler = new MovesHandler<ProfileData>() {

        @Override
        public void onSuccess(ProfileData arg0) {
            Log.i(TAG2, "User ID : " + arg0.getUserID()
                    + "\nUser Platform : " + arg0.getPlatform());
        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            Log.i(TAG2, "Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };
    private MovesHandler<ArrayList<SummaryListData>> summaryHandler = new MovesHandler<ArrayList<SummaryListData>>() {
        @Override
        public void onSuccess(ArrayList<SummaryListData> result) {
            for (SummaryListData sld : result) {
                Log.i(TAG2, "Calories Idle : " + sld.getCaloriesIdle());
                Log.i(TAG2, "Last update : " + sld.getLastUpdate());
                Log.i(TAG2, "Date : " + sld.getDate());

                ArrayList<SummaryData> summaries = sld.getSummaries();
                for (SummaryData sd : summaries) {
                    Log.i(TAG2, "Activity : " + sd.getActivity());
                    Log.i(TAG2, "Calories : " + sd.getCalories());
                    Log.i(TAG2, "Distance : " + sd.getDistance());
                    Log.i(TAG2, "Duration : " + sd.getDuration());
                    Log.i(TAG2, "Group : " + sd.getGroup());
                    Log.i(TAG2, "Steps : " + sd.getSteps());
                }

            }
        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            Log.i(TAG2, "Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };
    private MovesHandler<ArrayList<StorylineData>> storylineHandler = new MovesHandler<ArrayList<StorylineData>>() {
        @Override
        public void onSuccess(ArrayList<StorylineData> result) {
            for (StorylineData sld : result) {
                Log.i(TAG2, "-Calories Idle : " + sld.getCaloriesIdle());
                Log.i(TAG2, "-Last update : " + sld.getLastUpdate());
                Log.i(TAG2, "-Date : " + sld.getDate());

                ArrayList<SegmentData> segments = sld.getSegments();
                for (SegmentData sd : segments) {
                    Log.i(TAG2, "-Segments start time : " + sd.getStartTime());
                    Log.i(TAG2, "-Segments end time : " + sd.getEndTime());
                    Log.i(TAG2, "-Segments type : " + sd.getType());
                    if (sd.getPlace() != null) {
                        Log.i(TAG2, "-Segments place foursquare: " + sd.getPlace().getFoursquareId());
                        Log.i(TAG2, "-Segments place type : " + sd.getPlace().getType());
                        Log.i(TAG2, "-Segments place name: " + sd.getPlace().getName());
                        Log.i(TAG2, "-Segments place foursquare id : " + sd.getPlace().getFoursquareCategoryIds());
                        Log.i(TAG2, "-Segments place location lat : " + sd.getPlace().getLocation().getLat());
                        Log.i(TAG2, "-Segments place location lon : " + sd.getPlace().getLocation().getLon());
                    }
                    ArrayList<ActivityData> activities = sd.getActivities();
                    for (ActivityData ad : activities) {
                        Log.i(TAG2, "--Activities: name : " + ad.getActivity());
                        Log.i(TAG2, "--Activities: start time : " + ad.getStartTime());
                        Log.i(TAG2, "--Activities: end time : " + ad.getEndTime());
                        Log.i(TAG2, "--Activities: calories : " + ad.getCalories());
                        Log.i(TAG2, "--Activities: manual : " + ad.getManual());
                        Log.i(TAG2, "--Activities: distance : " + ad.getDistance());
                        Log.i(TAG2, "--Activities: duration : " + ad.getDuration());
                        Log.i(TAG2, "--Activities: group : " + ad.getGroup());
                        Log.i(TAG2, "--Activities: steps : " + ad.getSteps());
                        ArrayList<TrackPointsData> trackPoints = ad.getTrackPoints();
                        for (TrackPointsData tpd : trackPoints) {
                            Log.i(TAG2, "---Track points" + tpd.getLat() + " ; " + tpd.getLon());
                            Log.i(TAG2, "---Track points" + tpd.getTime());
                        }

                    }

                }

                ArrayList<SummaryData> summaries = sld.getSummary();
                for (SummaryData sd : summaries) {
                    Log.i(TAG2, "-Summary Activity : " + sd.getActivity());
                    Log.i(TAG2, "-Summary Calories : " + sd.getCalories());
                    Log.i(TAG2, "-Summary Distance : " + sd.getDistance());
                    Log.i(TAG2, "-Summary Duration : " + sd.getDuration());
                    Log.i(TAG2, "-Summary Group : " + sd.getGroup());
                    Log.i(TAG2, "-Summary Steps : " + sd.getSteps());
                }

            }

        }

        @Override
        public void onFailure(MovesStatus status, String message) {
            Log.i(TAG2, "Request Failed! \n"
                    + "Status Code : " + status + "\n"
                    + "Status Message : " + message + "\n\n"
                    + "Specific Message : " + status.getStatusMessage());
        }
    };

    private DataManager() {
    }

    public static DataManager getInstance() {
        return dataManager;
    }

    public void init(Context _context) {
        context = _context;
        symptomContext = new SymptomContext();
        symptomContext.addObserver(dataManager);
        mYahooWeather = YahooWeather.getInstance(5000, 5000, true);
        mYahooWeather.setExceptionListener(dataManager);
        mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.GPS);
        try {
            MovesAPI.init(_context, CLIENT_ID, CLIENT_SECRET, CLIENT_SCOPES, CLIENT_REDIRECTURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchByGPS();

    }

    @Override
    public void onFailConnection(final Exception e) {
        symptomContext.notifyObservers(e);
        Toast.makeText(context, "Fail Connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailParsing(final Exception e) {
        symptomContext.notifyObservers(e);
        Toast.makeText(context, "Fail Parsing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailFindLocation(final Exception e) {
        symptomContext.notifyObservers(e);
        Toast.makeText(context, "Fail Find Location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void gotWeatherInfo(WeatherInfo weatherInfo) {


        if (weatherInfo != null) {
            symptomContext.setBaroPressureRising(weatherInfo.getAtmosphereRising());
            symptomContext.setHumidity(weatherInfo.getAtmosphereHumidity());
            symptomContext.setWindChill(weatherInfo.getWindChill());
            symptomContext.setPressure(weatherInfo.getAtmospherePressure());
            symptomContext.setTemperature(String.valueOf(weatherInfo.getCurrentTemp()));
            symptomContext.setVisibility(weatherInfo.getAtmosphereVisibility());
            symptomContext.setWindDirection(weatherInfo.getWindDirection());
            symptomContext.setWindSpeed(weatherInfo.getWindSpeed());
            symptomContext.setWeatherCondition(weatherInfo.getCurrentText());
            symptomContext.setAddress(weatherInfo.getAddress().getAddressLine(0));
            symptomContext.setCountry(weatherInfo.getLocationCountry());
            symptomContext.setCity(weatherInfo.getLocationCity());
            symptomContext.setLatLng(new LatLng(Double.parseDouble(weatherInfo.getConditionLat()), Double.parseDouble(weatherInfo.getConditionLon())));
            symptomContext.setPostCode(weatherInfo.getAddress().getPostalCode());
            Log.i(TAG, "observers" + symptomContext.countObservers());
            Log.i(TAG, "observers" + symptomContext.hasChanged());

            symptomContext.notifyObservers("OK");

//            Log.i(TAG, weatherInfo.getTitle() + "\n"
//                    + weatherInfo.getWOEIDneighborhood() + ", "
//                    + weatherInfo.getWOEIDCounty() + ", "
//                    + weatherInfo.getWOEIDState() + ", "
//                    + weatherInfo.getWOEIDCountry());
//            Log.i(TAG, "====== CURRENT ======" + "\n" +
//                            "date: " + weatherInfo.getCurrentConditionDate() + "\n" +
//                            "weather: " + weatherInfo.getCurrentText() + "\n" +
//                            "temperature in ºC: " + weatherInfo.getCurrentTemp() + "\n" +
//                            "wind chill: " + weatherInfo.getWindChill() + "\n" +
//                            "wind direction: " + weatherInfo.getWindDirection() + "\n" +
//                            "wind speed: " + weatherInfo.getWindSpeed() + "\n" +
//                            "Humidity: " + weatherInfo.getAtmosphereHumidity() + "\n" +
//                            "Pressure: " + weatherInfo.getAtmospherePressure() + "\n" +
//                            "Atmosphere rising: " + weatherInfo.getAtmosphereRising() + "\n" +
//                            "Astronomy sunrise: " + weatherInfo.getAstronomySunrise() + "\n" +
//                            "Astronomy sunset: " + weatherInfo.getAstronomySunset() + "\n" +
//                            "Address: " + weatherInfo.getAddress() + "\n" +
//                            "Code: " + weatherInfo.getCurrentCode() + "\n" +
//                            "Conditional title: " + weatherInfo.getConditionTitle() + "\n" +
//                            "Description: " + weatherInfo.getDescription() + "\n" +
//                            "Country: " + weatherInfo.getLocationCountry() + "\n" +
//                            "City: " + weatherInfo.getLocationCity() + "\n" +
//                            "Region: " + weatherInfo.getLocationRegion() + "\n" +
//                            ":Lat " + weatherInfo.getConditionLat() + "Lon" + weatherInfo.getConditionLon() + "\n" +
//                            "Visibility: " + weatherInfo.getAtmosphereVisibility()
//            );
        } else {
            symptomContext.notifyObservers("Error");
        }
    }

    private void searchByGPS() {
        mYahooWeather.setNeedDownloadIcons(true);
        mYahooWeather.setUnit(YahooWeather.UNIT.CELSIUS);
        mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.GPS);
        mYahooWeather.queryYahooWeatherByGPS(context, this);
    }

    @Override
    public void update(Observable observable, Object data) {
        Log.i(TAG, data.getClass().getName());
        Log.i(TAG, data.getClass().getSimpleName());

        if (data.toString().equals("OK")) {
            //TODO add logic for saving data
            Log.i(TAG, data.toString());

        } else if (data.getClass().getSimpleName().equals("Exception")) {
            Log.i(TAG, data.toString());
            if (data.toString().contains("Network")) {
                // No network available do not retry to get info

            } else {
                dataManager.init(context);
            }

        } else if (data.toString().equals("Error")) {
            Toast.makeText(context, "Unknown error occured. Contact the administrator", Toast.LENGTH_LONG).show();
        }
    }

    public void movesAuthenticate(AppCompatActivity activity) {
        MovesAPI.authenticate(authDialogHandler, activity);
    }

    public AuthData getMovesAuthData() {

        return MovesAPI.getAuthData();
    }

    public void movesProfile() {
        MovesAPI.getProfile(profileHandler);
    }

    /**
     * @param day Format example "20160212"
     */
    public void movesSummarySingleDay(String day) {
        MovesAPI.getSummary_SingleDay(summaryHandler, day, null);
    }

    /**
     * @param week Format example "2016-W06"
     */
    public void movesSummaryWeek(String week) {
        MovesAPI.getSummary_SpecificWeek(summaryHandler, week, null);
    }

    /**
     * @param month Format example "201602"
     */
    public void movesSummaryMonth(String month) {
        MovesAPI.getSummary_SpecificMonth(summaryHandler, month, null);
    }

    /**
     * @param start Format example "20160211"
     * @param end   Format example "20160212"
     */
    public void movesSummaryRange(String start, String end) {
        MovesAPI.getSummary_WithinRange(summaryHandler, start, end, null);
    }

    /**
     * @param pastDays Format example "2"
     */
    public void movesSummaryPastDays(String pastDays) {
        MovesAPI.getSummary_PastDays(summaryHandler, pastDays, null);

    }

    /**
     * @param day Format example "20160212"
     * @param trp To return track points or not
     */
    public void movesStorylineDay(String day, boolean trp) {
        MovesAPI.getStoryline_SingleDay(storylineHandler, day, null, trp);
    }

    /**
     * @param week Format example "2016-W06"
     * @param trp  To return track points or not
     */
    public void movesStorylineWeek(String week, boolean trp) {
        MovesAPI.getStoryline_SpecificWeek(storylineHandler, week, null, trp);
    }

    /**
     * @param month Format example "201602"
     */
    public void movesStorylineMonth(String month) {
        MovesAPI.getStoryline_SpecificMonth(storylineHandler, month, null, false);
    }

    /**
     * @param start Format example "20160211"
     * @param end   Format example "20160212"
     * @param trp   To return track points or not
     */
    public void movesStorylineRange(String start, String end, boolean trp) {
        MovesAPI.getStoryline_WithinRange(storylineHandler, start, end, null, trp);
    }

    /**
     * @param pastDays Format example "2"
     * @param trp      To return track points or not
     */
    public void movesStorylinePastDays(String pastDays, boolean trp) {
        MovesAPI.getStoryline_PastDays(storylineHandler, pastDays, null, trp);
    }

    /**
     * @param day Format example "20160212"
     */
    public void movesActivitiesDay(String day) {
        MovesAPI.getActivities_SingleDay(storylineHandler, day, null);
    }

    /**
     * @param week Format example "2016-W06"
     */
    public void movesActivitiesWeek(String week) {
        MovesAPI.getActivities_SpecificWeek(storylineHandler, week, null);
    }

    /**
     * @param month Format example "201602"
     */
    public void movesActivitiesMonth(String month) {
        MovesAPI.getActivities_SpecificMonth(storylineHandler, "201602", null);
    }

    /**
     * @param start Format example "20160211"
     * @param end   Format example "20160212"
     */
    public void movesActivitiesRange(String start, String end) {
        MovesAPI.getActivities_WithinRange(storylineHandler, "20160211", "20160212", null);
    }

    /**
     * @param pastDays Format example "2"
     */
    public void movesActivitiesPastDays(String pastDays) {
        MovesAPI.getActivities_PastDays(storylineHandler, "31", null);
    }



}

