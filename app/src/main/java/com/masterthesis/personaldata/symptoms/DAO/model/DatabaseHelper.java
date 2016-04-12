package com.masterthesis.personaldata.symptoms.DAO.model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by Konstantinos Michail on 2/16/2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "symptoms_first.db";
    private static final int DATABASE_VERSION = 27;
    private final String LOG_NAME = getClass().getName();
    private Dao<Diary, Integer> diaryDAO;
    private Dao<Symptom, Integer> symptomDAO;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Dao<Diary, Integer> getDiaryDAO() throws SQLException {
        if (diaryDAO == null) {
            try {
                diaryDAO = getDao(Diary.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return diaryDAO;
    }

    public Dao<Symptom, Integer> getSymptomDAO() throws SQLException {
        if (symptomDAO == null) {
            try {
                symptomDAO = getDao(Symptom.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return symptomDAO;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Diary.class);
            TableUtils.createTable(connectionSource, Symptom.class);
        } catch (SQLException e) {
            Log.e(LOG_NAME, "Could not create new table for Diary and symptom", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Diary.class, true);
            TableUtils.dropTable(connectionSource, Symptom.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(LOG_NAME, "Could not upgrade the table for Thing", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}

