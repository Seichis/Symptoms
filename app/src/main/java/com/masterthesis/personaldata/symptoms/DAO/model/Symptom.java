package com.masterthesis.personaldata.symptoms.DAO.model;

import com.google.gson.Gson;
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

    public Symptom(){
    }

    public Symptom(Timestamp createdAt){
        super(createdAt);
    }

    @DatabaseField(columnName = SYMPTOM_TYPE_FIELD_NAME, canBeNull = false)
    String symptomType;
    @DatabaseField(columnName = SYMPTOM_CONTEXT_FIELD_NAME, canBeNull = false)
    String context = null;
    @DatabaseField(columnName = INTENSITY_FIELD_NAME, canBeNull = false)
    double intensity;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = DIARY_ID_FIELD_NAME)
    private Diary diary;

    public Symptom(String _symptomType) {
        this.symptomType = _symptomType;
    }

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
        this.context = context;
    }

    public void setContext(SymptomContext context) {
        Gson gson = new Gson();
        String sContext = gson.toJson(context, SymptomContext.class);
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
