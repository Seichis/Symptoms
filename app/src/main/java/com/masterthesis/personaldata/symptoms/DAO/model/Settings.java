package com.masterthesis.personaldata.symptoms.DAO.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;

/**
 * Created by Konstantinos Michail on 4/17/2016.
 */

@DatabaseTable(tableName = "settings")
public class Settings extends BaseDAO {
    public final static int BINARY_BUTTON_MODE = 0;
    public final static int FULL_BUTTON_MODE = 1;
    public final static int NO_BUTTON_MODE = 2;
    public final static String MODE_FIELD = "mode";
    public final static String IS_MOVES_FIELD = "isMoves";
    public final static String IS_LIFELOG_FIELD = "isLifelog";
    public final static String IS_FLIC_FIELD = "isFlic";
    public final static String PATIENT_FIELD = "patient";

    @DatabaseField(columnName = IS_MOVES_FIELD, dataType = DataType.BOOLEAN)
    boolean isMovesActivated;

    @DatabaseField(columnName = IS_LIFELOG_FIELD,dataType = DataType.BOOLEAN)
    boolean isLifelogActivated;

    @DatabaseField(columnName = IS_FLIC_FIELD,dataType = DataType.BOOLEAN)
    boolean isFlicActivated;
    //Default mode no button
    @DatabaseField(columnName = MODE_FIELD, defaultValue = "2",dataType = DataType.INTEGER)
    int mode;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = PATIENT_FIELD)
    Patient patient;

    public Settings() {
    }
    public Settings(Timestamp createdAt) {
        super(createdAt);
    }

    public boolean isMovesActivated() {
        return isMovesActivated;
    }

    public void setMovesActivated(boolean movesActivated) {
        isMovesActivated = movesActivated;
    }

    public boolean isLifelogActivated() {
        return isLifelogActivated;
    }

    public void setLifelogActivated(boolean lifelogActivated) {
        isLifelogActivated = lifelogActivated;
    }

    public boolean isFlicActivated() {
        return isFlicActivated;
    }

    public void setFlicActivated(boolean flicActivated) {
        isFlicActivated = flicActivated;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
