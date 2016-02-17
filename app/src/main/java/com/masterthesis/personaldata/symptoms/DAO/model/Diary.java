package com.masterthesis.personaldata.symptoms.DAO.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Konstantinos Michail on 2/17/2016.
 */


@DatabaseTable(tableName = "accounts")
public class Diary {

    // for QueryBuilder to be able to find the fields
    public static final String NAME_FIELD_NAME = "name";
    public static final String COLOR_FIELD_NAME = "color";
    public static final String FIRST_SYMPTOM_FIELD_NAME = "symptom1";
    public static final String SECOND_SYMPTOM_FIELD_NAME = "symptom2";
    public static final String THIRD_SYMPTOM_FIELD_NAME = "symptom3";
    public static final String DESCRIPTION_FIELD_NAME = "symptom3";
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false)
    private String name;
    @DatabaseField(columnName = COLOR_FIELD_NAME)
    private String color;
    @ForeignCollectionField(columnName = FIRST_SYMPTOM_FIELD_NAME)
    private ForeignCollection<Symptom> symptom1;
    @ForeignCollectionField(columnName = SECOND_SYMPTOM_FIELD_NAME)
    private ForeignCollection<Symptom> symptom2;
    @ForeignCollectionField(columnName = THIRD_SYMPTOM_FIELD_NAME)
    private ForeignCollection<Symptom> symptom3;
    @DatabaseField(columnName = DESCRIPTION_FIELD_NAME)
    private String description;

    public Diary() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ForeignCollection<Symptom> getSymptom1() {
        return symptom1;
    }

    public void setSymptom1(ForeignCollection<Symptom> symptom1) {
        this.symptom1 = symptom1;
    }

    public ForeignCollection<Symptom> getSymptom2() {
        return symptom2;
    }

    public void setSymptom2(ForeignCollection<Symptom> symptom2) {
        this.symptom2 = symptom2;
    }

    public ForeignCollection<Symptom> getSymptom3() {
        return symptom3;
    }

    public void setSymptom3(ForeignCollection<Symptom> symptom3) {
        this.symptom3 = symptom3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
