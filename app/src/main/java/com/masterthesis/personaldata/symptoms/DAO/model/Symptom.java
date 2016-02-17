package com.masterthesis.personaldata.symptoms.DAO.model;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Konstantinos Michail on 2/11/2016.
 */
@DatabaseTable(tableName = "Symptom")
public class Symptom { // extends Observable {

    // for QueryBuilder to be able to find the fields
    public static final String SYMPTOM_TYPE_FIELD_NAME = "type";
    public static final String TIMESTAMP_FIELD_NAME = "timestamp";
    public static final String SYMPTOM_CONTEXT_FIELD_NAME = "context";
    public static final String INTENSITY_FIELD_NAME = "intensity";
    public static final String DIARY_ID_FIELD_NAME = "diary_id";


    @DatabaseField(columnName = SYMPTOM_TYPE_FIELD_NAME, canBeNull = false)
    String symptomType;
    @DatabaseField(columnName = TIMESTAMP_FIELD_NAME, canBeNull = false)
    Date timestamp;
    @DatabaseField(columnName = SYMPTOM_CONTEXT_FIELD_NAME, canBeNull = false)
    String context = null;
    @DatabaseField(columnName = INTENSITY_FIELD_NAME, canBeNull = false)
    double intensity;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = DIARY_ID_FIELD_NAME)
    private Diary diary;

    public Symptom() {
    }

    public Symptom(Diary _diary, String _symptomType, Date _timestamp) {
        this.diary = _diary;
        this.symptomType = _symptomType;
        this.timestamp = _timestamp;
    }

    public Symptom(Date _timestamp) {
        this.timestamp = _timestamp;
    }

    public Symptom(String _symptomType) {
        this.symptomType = _symptomType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Diary getDiary() {

        return diary;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
    public void setContext(SymptomContext context) {
        Gson gson=new Gson();
        String sContext=gson.toJson(context,SymptomContext.class);
        this.context = sContext;
    }

    public boolean isValid() {
        if (this.context == null) {
            return false;
        } else {
            return true;
        }
    }

}
