package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.util.Log;

import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.DAO.model.Diary;
import com.masterthesis.personaldata.symptoms.fragments.DiaryFragment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiaryManager {
    private static final String TAG = "DiaryManager";
    private static DiaryManager diaryManager = new DiaryManager();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private DatabaseHelper dbHelper;
//    private Context context;

    private DiaryManager() {

    }

    public static DiaryManager getInstance() {
        return diaryManager;
    }

    public void init(Context context) {
//        context = context;
        dbHelper = new DatabaseHelper(context);
        preferences = context.getSharedPreferences("com.masterthesis.personaldata.symptoms", Context.MODE_PRIVATE);
    }

    public Diary createDiary(String name, String description) {
        Diary diary = new Diary(new Timestamp(System.currentTimeMillis()));
        diary.setName(name);
        diary.setDescription(description);
        try {
            dbHelper.getDiaryDAO().create(diary);
        } catch (SQLException e) {
            throw new RuntimeException("Could not create a new Diary in the database", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        Set<String> symTypes = new HashSet<>();

        editor = preferences.edit();
        editor.putStringSet(String.valueOf(diary.getId()), symTypes);
        boolean committed = editor.commit();

        Log.i(TAG, "Set Created. " + committed + diary.getId());

        return diary;
    }

    public boolean addSymptomType(Diary diary, String symType) {
        Set<String> symTypes;
        String id = String.valueOf(diary.getId());
        Log.i(TAG, "pref" + preferences.contains(id));
        if (preferences.contains(id)) {
            symTypes = preferences.getStringSet(id, null);
            if (symTypes != null) {
                if (symTypes.contains(symType) || symType.length() >= 3) {
                    // TODO Show the user feedback that the symptom list is full for this diary:

                    Log.i(TAG, "This Diary has no more room for more symptoms");
                    return false;
                } else {
                    symTypes.add(symType);
                    editor = preferences.edit();
                    editor.putStringSet(id, symTypes);
                    boolean committed = editor.commit();
                    Log.i(TAG, "Set existed.Symptom type added." + symTypes.size() + committed);
                    return committed;
                }
            } else {
                //TODO Log error: no set was created for that diary error
                Log.i(TAG, "Log error: no set was created for that diary error");

                return false;
            }
        } else {
            //TODO Log error: no entry in the preferences for the diary
            Log.i(TAG, "Log error: no entry in the preferences for the diary");

            return false;
        }
    }


    public void deleteDiary() {

    }

    public List<Diary> getDiaries() throws java.sql.SQLException {

        if (dbHelper.getDiaryDAO() != null) {
            return dbHelper.getDiaryDAO().queryForAll();
        } else {
            return new ArrayList<>();
        }
    }


    public Diary getActiveDiary() {
//        DiaryFragment.newInstance(new String("wow"),new String("2"))
        if (!DiaryFragment.diaries.isEmpty()) {
            Diary mDiary = DiaryFragment.diaries.get(0);
            return mDiary;
        } else {
            return null;
        }
    }

}

