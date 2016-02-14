package com.masterthesis.personaldata.symptoms.datamodel;

import com.google.android.gms.maps.model.LatLng;

import java.util.Observable;

/**
 * Created by Konstantinos Michail on 2/14/2016.
 */

public class SymptomContext extends Observable {
    // Location variables

    private String address;
    private String postCode;
    private LatLng latLng;
    private String country;
    private String type;
    private String city;
    // Weather variables
    private String temperature;
    private String weatherCondition;
    private String windChill;
    private String windDirection;
    private String windSpeed;
    private String humidity;
    private String pressure;
    private String baroPressureRising;
    private String visibility;


    public SymptomContext() {
        super();
        setChanged();
    }

    /**
     * @return Temperature in Celsius
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * @param temperature Temperature in Celsius ("20")
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * @return General weather condition ("Mostly cloudy")
     */
    public String getWeatherCondition() {
        return weatherCondition;
    }

    /**
     * @param weatherCondition General weather condition ("Mostly cloudy")
     */
    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    /**
     * @return
     */
    public String getWindChill() {
        return windChill;
    }

    /**
     * @param windChill
     */
    public void setWindChill(String windChill) {
        this.windChill = windChill;
    }

    /**
     * @return
     */
    public String getWindDirection() {
        return windDirection;
    }

    /**
     * @param windDirection
     */
    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    /**
     * @return
     */
    public String getWindSpeed() {
        return windSpeed;
    }

    /**
     * @param windSpeed
     */
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }



    /**
     * @return
     */
    public String getPressure() {
        return pressure;
    }

    /**
     * @param pressure
     */
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    /**
     * @return
     */
    public String getBaroPressureRising() {
        return baroPressureRising;
    }

    /**
     * @param baroPressureRising
     */
    public void setBaroPressureRising(String baroPressureRising) {
        this.baroPressureRising = baroPressureRising;
    }

    /**
     * @return
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * @param visibility
     */
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    /**
     * @return Municipality name ("Ballerup)
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city Municipality name ("Ballerup)
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return Address name and number ("Sømoseparken 78")
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address Address name and number ("Sømoseparken 78")
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return Postal code ("2750")
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * @param postCode Postal code ("2750")
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * @return Latitude and longitude as a {@link LatLng} object
     */
    public LatLng getLatLng() {
        return latLng;
    }

    /**
     * @param latLng Latitude and longitude as a {@link LatLng} object
     */
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    /**
     * @return Country name ("Denmark")
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country Country name ("Denmark")
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return Type of the place as inputted by the user at the moves app
     * or inferred from Foursquare ("Home")
     */
    public String getType() {
        return type;
    }

    /**
     * @param type Type of the place as inputted by the user at the moves app
     *             or inferred from Foursquare ("Home")
     */
    public void setType(String type) {
        this.type = type;
    }



}
