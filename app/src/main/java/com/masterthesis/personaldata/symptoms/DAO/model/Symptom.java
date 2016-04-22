package com.masterthesis.personaldata.symptoms.DAO.model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.maps.android.clustering.ClusterItem;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;

/**
 * Created by Konstantinos Michail on 2/11/2016.
 */
@DatabaseTable(tableName = "Symptom")
public class Symptom extends BaseDAO {

    // for QueryBuilder to be able to find the fields
    public static final String SYMPTOM_TYPE_FIELD_NAME = "type";
    public static final String SYMPTOM_CONTEXT_FIELD_NAME = "context";
    public static final String INTENSITY_FIELD_NAME = "intensity";
    public static final String DIARY_ID_FIELD_NAME = "diary_id";
    @DatabaseField(columnName = SYMPTOM_TYPE_FIELD_NAME, canBeNull = false)
    String symptomType;
    @DatabaseField(columnName = SYMPTOM_CONTEXT_FIELD_NAME)
    String context = "";
    @DatabaseField(columnName = INTENSITY_FIELD_NAME, canBeNull = false)
    double intensity;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = DIARY_ID_FIELD_NAME, canBeNull = false)
    private Diary diary;
    public Symptom() {
    }

    public Symptom(Timestamp createdAt) {
        super(createdAt);
    }

//    public Symptom(String _symptomType) {
//        this.symptomType = _symptomType;
//    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

//    @Override
//    public Timestamp getUpdatedAt() {
//        return updatedAt;
//    }
//
//    @Override
//    public void setUpdatedAt(Timestamp updatedAt) {
//
//    }
//
//    @Override
//    public Timestamp getCreatedAt() {
//        return createdAt;
//    }
//
//    @Override
//    public void setCreatedAt(Timestamp createdAt) {
//
//    }

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


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context=context;
    }


    public boolean isValid() {
        if (this.context.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public JsonObject getSymptomContext() {
        Log.i("Symptom context",context);
        return new Gson().fromJson(context, JsonObject.class);
    }

}
