package com.masterthesis.personaldata.symptoms.DAO.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Konstantinos Michail on 2/21/2016.
 */
public class BaseDAO {
    public static final String UPDATED_AT_FIELD_NAME = "updatedAt";
    public static final String CREATED_AT_FIELD_NAME = "createdAt";
    @DatabaseField(generatedId = true)
    Integer id;
    @DatabaseField(columnName = UPDATED_AT_FIELD_NAME, dataType = DataType.DATE)
    Date updatedAt;
    @DatabaseField(columnName = CREATED_AT_FIELD_NAME, dataType = DataType.DATE, canBeNull = false)
    Date createdAt;

    public BaseDAO() {
    }

    public BaseDAO(Timestamp _createdAt) {
        createdAt = _createdAt;
        updatedAt = _createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
