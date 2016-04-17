package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;
import android.util.Log;

import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.DAO.model.Patient;
import com.masterthesis.personaldata.symptoms.DAO.model.Settings;

import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by Konstantinos Michail on 4/17/2016.
 */
public class SettingsManager {
    private static SettingsManager settingsManager = new SettingsManager();
    DatabaseHelper dbHelper;
    Patient patient;
    PatientManager patientManager;

    public static SettingsManager getInstance() {
        return settingsManager;
    }

    public void init(Context context) {
        dbHelper = new DatabaseHelper(context);
        patientManager = PatientManager.getInstance();
        patient = patientManager.getPatient();
    }

    public Settings getSettings() {

        try {
            return (patient != null) ? dbHelper.getSettingsDAO().queryForEq(Settings.PATIENT_FIELD, patient.getId()).get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateSettings(Settings settings) {
            try {
                dbHelper.getSettingsDAO().update(settings);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void createSettings(Patient p) {
        Settings settings = new Settings(new Timestamp(System.currentTimeMillis()));
        settings.setPatient(p);
        settings.setFlicActivated(false);
        settings.setMovesActivated(false);
        settings.setLifelogActivated(false);
        settings.setMode(2);
        try {
            dbHelper.getSettingsDAO().create(settings);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
