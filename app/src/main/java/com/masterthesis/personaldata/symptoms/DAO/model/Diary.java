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
    public static final String FIRST_SYMPTOM_FIELD_NAME = "symptoms";
    public static final String DESCRIPTION_FIELD_NAME = "description";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false)
    private String name;
    @DatabaseField(columnName = COLOR_FIELD_NAME)
    private String color;
    @ForeignCollectionField(columnName = FIRST_SYMPTOM_FIELD_NAME)
    private ForeignCollection<Symptom> symptoms;
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

    public ForeignCollection<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ForeignCollection<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
