package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.masterthesis.personaldata.symptoms.datamodel.SymptomContext;

import java.util.Observable;
import java.util.Observer;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherExceptionListener;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;

/**
 * Created by Konstantinos Michail on 2/14/2016.
 */
public class WeatherManager implements YahooWeatherInfoListener,
        YahooWeatherExceptionListener, Observer {
    private static final String TAG = "Weather";
    private static WeatherManager weatherManager = new WeatherManager();
    private static Context context;
    SymptomContext symptomContext;
    private YahooWeather mYahooWeather;

    private WeatherManager() {
    }

    public static WeatherManager getInstance() {
        return weatherManager;
    }


    public void init(Context _context) {
        context = _context;
        symptomContext = new SymptomContext();
        symptomContext.addObserver(weatherManager);
        mYahooWeather = YahooWeather.getInstance(5000, 5000, true);
        mYahooWeather.setExceptionListener(this);
        mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.GPS);
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
//            if (mYahooWeather.getSearchMode() == YahooWeather.SEARCH_MODE.GPS) {
//                if (weatherInfo.getAddress() != null) {
//                }
//            }


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
        }else{
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
        Log.i(TAG,data.getClass().getName());
        Log.i(TAG,data.getClass().getSimpleName());

        if (data.toString().equals("OK")){
            //TODO add logic for saving data
            Log.i(TAG,data.toString());

        }else if (data.getClass().getSimpleName().equals("Exception")){
            Log.i(TAG, data.toString());
            if (data.toString().contains("Network")){
                // No network available do not retry to get info

            }else{
                weatherManager.init(context);
            }

        }else if(data.toString().equals("Error")){
            Toast.makeText(context,"Unknown error occured. Contact the administrator",Toast.LENGTH_LONG);
        }
    }
}
