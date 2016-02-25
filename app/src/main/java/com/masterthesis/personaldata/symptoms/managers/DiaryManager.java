package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.util.Log;

import com.masterthesis.personaldata.symptoms.Constants;
import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.DAO.model.Diary;
import com.masterthesis.personaldata.symptoms.fragments.DiaryFragment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class DiaryManager {
    private static final String TAG = "DiaryManager";
    private static DiaryManager diaryManager = new DiaryManager();
    private SharedPreferences preferencesDiary;
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
        preferencesDiary = context.getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE);
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
        Set<String> symTypes = new LinkedHashSet<>();

        editor = preferencesDiary.edit();
        editor.putStringSet(name, symTypes);
        boolean committed = editor.commit();

        Log.i(TAG, "Set Created. " + committed + diary.getId());

        return diary;
    }

    public boolean addSymptomType(Diary diary, String symType) {
        Set<String> symTypes;
        String diaryName = diary.getName();
        Log.i(TAG, "pref" + preferencesDiary.contains(diaryName));
        if (preferencesDiary.contains(diaryName)) {
            symTypes = preferencesDiary.getStringSet(diaryName, null);
            if (symTypes != null) {
                if (symTypes.contains(symType) || symTypes.size() >= 3) {
                    // TODO Show the user feedback that the symptom list is full for this diary:

                    Log.i(TAG, "This Diary has no more room for more symptoms");
                    return false;
                } else {
                    int size=symTypes.size();
                    symTypes.add(String.valueOf(size)+","+symType);
                    editor = preferencesDiary.edit();

                    editor.putStringSet(diaryName, symTypes);
                    editor.apply();
                    Log.i(TAG, "Set existed.Symptom type added." + symTypes.size());
                    return true;
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

    public List<Diary> searchByName(String name) throws java.sql.SQLException {
        return dbHelper.getDiaryDAO().queryForEq(Diary.NAME_FIELD_NAME,name);
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

