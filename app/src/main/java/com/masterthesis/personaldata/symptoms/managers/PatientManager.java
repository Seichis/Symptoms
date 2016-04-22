package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;

import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.DAO.model.Patient;
import com.masterthesis.personaldata.symptoms.DAO.model.Settings;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Konstantinos Michail on 4/17/2016.
 */
public class PatientManager {
    private static PatientManager patientManager = new PatientManager();
    DatabaseHelper dbHelper;

    public static PatientManager getInstance() {
        return patientManager;
    }

    public void init(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public Patient getPatient() {
        try {
            List<Patient> patients = dbHelper.getPatientDAO().queryForAll();
            return (!patients.isEmpty()) ? patients.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createNewProfile(Patient patient) {
        try {
            dbHelper.getPatientDAO().create(patient);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
