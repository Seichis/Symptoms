package com.masterthesis.personaldata.symptoms.datamodel;

/**
 * Created by Konstantinos Michail on 2/11/2016.
 */
public class Symptom {
    String symptomType;
    long timestamp;
    SymptomContext context = null;

    public Symptom() {
    }

    public Symptom(String _symptomType) {
        this.symptomType = _symptomType;
    }

    public String getSymptomType() {
        return symptomType;
    }

    public void setSymptomType(String symptomType) {
        this.symptomType = symptomType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public SymptomContext getContext() {
        return context;
    }

    public void setContext(SymptomContext context) {
        this.context = context;
    }

    public boolean isValid() {
        if (context == null) {
            return false;
        } else {
            return true;
        }
    }

}
