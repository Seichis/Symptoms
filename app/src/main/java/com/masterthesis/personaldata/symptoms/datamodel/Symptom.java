package com.masterthesis.personaldata.symptoms.datamodel;

import java.util.Observable;

/**
 * Created by Konstantinos Michail on 2/11/2016.
 */
public class Symptom extends Observable {
    String symptomType;
    String timestamp;
    SymptomContext context = null;
    double intensity;

    public Symptom() {
    }

    public Symptom(String _symptomType) {
        this.symptomType = _symptomType;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public String getSymptomType() {
        return symptomType;
    }

    public void setSymptomType(String symptomType) {
        this.symptomType = symptomType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public SymptomContext getContext() {
        return context;
    }

    public void setContext(SymptomContext context) {
        this.context = context;
    }

    public boolean isValid() {
        if (this.context == null) {
            return false;
        } else {
//            setChanged();
            return true;
        }
    }

}
