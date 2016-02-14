package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;

import com.masterthesis.personaldata.symptoms.datamodel.Symptom;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Konstantinos Michail on 2/14/2016.
 */
public class SymptomManager{

    Context context;
    static SymptomManager symptomManager = new SymptomManager();
    static Symptom symptom;
    float buttonMeasument;
    boolean isFinished;
    long occurenceTimeStamp, pressButtonTimestamp;


    private SymptomManager() {

    }

    public static SymptomManager getInstance() {
        return symptomManager;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void createSymptom(String type,long timestamp) {
        Symptom symptom=new Symptom(type);
        WeatherManager wm = WeatherManager.getInstance();
        wm.init(context);

    }

    public boolean isFinished() {
        return isFinished;
    }

    public void save() {

    }

    public void init(Context _context){
        context=context;
    }
}
