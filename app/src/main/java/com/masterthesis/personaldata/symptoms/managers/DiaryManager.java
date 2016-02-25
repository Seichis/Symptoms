package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.DAO.model.Diary;
import com.masterthesis.personaldata.symptoms.fragments.DiaryFragment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class DiaryManager {
    private static final String TAG = "DiaryManager";
    private static DiaryManager diaryManager = new DiaryManager();
    private DatabaseHelper dbHelper;

    private Context context;

    private DiaryManager() {

    }

    public static DiaryManager getInstance() {
        return diaryManager;
    }

    public void init(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
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

        return diary;
    }

    public boolean addSymptomType(Diary diary, String symType) {
        TreeMap<Integer, String> symTypes = new Gson().fromJson(diary.getSymptomTypes(), new TypeToken<TreeMap<Integer, String>>() {
        }.getType());

//        for (TreeMap.Entry<Integer, String> entry : symTypes.entrySet()) {
//            Log.d("map values", entry.getKey() + ": " + entry.getValue());
//        }
        if (symTypes==null) {
            symTypes=new TreeMap<>();
            symTypes.put(1, symType);
            diary.setSymptomTypes(new Gson().toJson(symTypes));
            updateDiary(diary);
            return true;
        } else {
            if (symTypes.containsValue(symType) || symTypes.size() >= 3) {
                // TODO Show the user feedback that the symptom list is full for this diary:

                Log.i(TAG, "This Diary has no more room for more symptoms");
                return false;
            } else {
                int size = symTypes.size()+1;
                symTypes.put(size, symType);
                diary.setSymptomTypes(new Gson().toJson(symTypes));
                updateDiary(diary);
                Log.i(TAG, "More symptom types existed.Symptom type added." + symTypes.size());
                return true;
            }
        }
    }

    public void updateDiary(Diary diary) {
        try {
            dbHelper.getDiaryDAO().update(diary);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
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
        return dbHelper.getDiaryDAO().queryForEq(Diary.NAME_FIELD_NAME, name);
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

