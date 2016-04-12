package com.masterthesis.personaldata.symptoms.DAO.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;

@DatabaseTable(tableName = "movesData")
public class MovesDataStoryline extends BaseDAO {

    // for QueryBuilder to be able to find the fields
    public static final String ACTIVITIES_FIELD_NAME = "activities";
    public static final String SEGMENTS_FIELD_NAME = "segments";
    public static final String TRACKPOINGS_FIELD_NAME = "trackpoints";

    @DatabaseField(columnName = TRACKPOINGS_FIELD_NAME, canBeNull = false)
    String trackPoints;
    @DatabaseField(columnName = ACTIVITIES_FIELD_NAME, canBeNull = false)
    String activities;
    @DatabaseField(columnName = SEGMENTS_FIELD_NAME, canBeNull = false)
    String segments;

    public MovesDataStoryline(){
    }
    public MovesDataStoryline(Timestamp createdAt){
        super(createdAt);
    }

    public String getTrackPoints() {
        return trackPoints;
    }

    public void setTrackPoints(String trackPoints) {
        this.trackPoints = trackPoints;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getSegments() {
        return segments;
    }

    public void setSegments(String segments) {
        this.segments = segments;
    }

}
