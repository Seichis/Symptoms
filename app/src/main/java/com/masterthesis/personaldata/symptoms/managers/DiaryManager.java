package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.util.Log;

import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.DAO.model.Diary;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Konstantinos Michail on 2/17/2016.
 */
public class DiaryManager {
    private static final String TAG = "DiaryManager";
    private static DiaryManager diaryManager = new DiaryManager();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private DatabaseHelper dbHelper;
    private Context context;

    private DiaryManager() {

    }

    public static DiaryManager getInstance() {
        return diaryManager;
    }

    public void init(Context _context) {
        context = _context;
        dbHelper = new DatabaseHelper(context);
        preferences = context.getSharedPreferences("com.masterthesis.personaldata.symptoms", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void create(String name, String description) {
        Diary diary = new Diary();
        diary.setDescription("My diary for pain");
        try {
            dbHelper.getDiaryDAO().create(diary);
        } catch (SQLException e) {
            throw new RuntimeException("Could not create a new Diary in the database", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createSymptomType(Diary diary, String symType) {
        Set<String> symTypes = null;
        boolean isCreated = false;
        if (preferences.contains(diary.getName())) {
            symTypes = preferences.getStringSet(diary.getName(), null);

            if (symTypes.contains(symType) || symType.length() >= 3) {
                //TODO Handle this better for the user
                Log.i(TAG, "This Diary has no more room for more symptoms");
                return false;
            } else {
                symTypes.add(symType);
                editor.putStringSet(diary.getName(), symTypes);
                editor.commit();
                return true;
            }

        } else {
            symTypes = new HashSet<>();
            symTypes.add(symType);
            editor.putStringSet(diary.getName(), symTypes);
            editor.commit();
            return true;
        }

    }

    public void deleteDiary(){

    }



    public Diary getActiveDiary() {
        Diary mDiary = new Diary();
        return mDiary;
    }
}
