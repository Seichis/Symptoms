package com.masterthesis.personaldata.symptoms.DAO.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Konstantinos Michail on 2/16/2016.
 */

@DatabaseTable
public class Thing {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer thingId) {
        this.id = thingId;
    }
}


