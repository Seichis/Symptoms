package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.CountDownTimer;
import android.util.Log;

import com.google.gson.Gson;
import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.DAO.model.Diary;
import com.masterthesis.personaldata.symptoms.DAO.model.Symptom;
import com.masterthesis.personaldata.symptoms.DAO.model.SymptomContext;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Konstantinos Michail on 2/14/2016.
 */
public class SymptomManager implements Observer {
    static List<Double> symptomInputList = new ArrayList<>();
    static SymptomManager symptomManager = new SymptomManager();
    static String TAG = "SymptomManager";
    boolean isWeatherException = false; //Check if an exception was caught while getting the weather

//    Context context;
//    Date timeStamp = null;

    Symptom symptom;
    DataManager dataManager = DataManager.getInstance();
    CountDownTimer countDownTimer;
    DatabaseHelper dbHelper;
    private boolean isCountDown;
    private SharedPreferences preferences;

    private SymptomManager() {

    }

    public static SymptomManager getInstance() {
        return symptomManager;
    }

    public void init(Context context) {
//        context = _context;
        dataManager.init(context);
        dbHelper = new DatabaseHelper(context);
    }

    public void manageSymptomInput(final Context context, double input) {
//        final Symptom symptom = new Symptom();
        if (symptomInputList.size() == 0) {
//            timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK).format(new Date());
            symptom = new Symptom(new Timestamp(System.currentTimeMillis()));
            // A symptom occured. get the weather data and start a timer for ending the registration of a symptom
            dataManager.fetchWeatherData(context);
            isCountDown = true;

//            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.UK);


//            dataManager.movesStorylineDay(format.format(new Date()),true);


            countDownTimer = new CountDownTimer(15000, 1000) {

                public void onTick(long millisUntilFinished) {
                    // If an exception is caught and it is not a network exception try to get weather again
                    if (isWeatherException) {
                        dataManager.fetchWeatherData(context);
                    }
                    Log.i(TAG, "seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    Gson gson = new Gson();
                    String sContext = gson.toJson(dataManager.getSymptomContext(), SymptomContext.class);
                    symptom.setContext(sContext);
                    symptom.setIntensity(Collections.max(symptomInputList));
                    Diary diary = DiaryManager.getInstance().getActiveDiary();
                    symptom.setDiary(diary);

                    int symptomType = symptomInputList.size() - 1;
                    //TODO add the real symptom saved in shared preferences probably
                    preferences=context.getSharedPreferences("com.masterthesis.personaldata.symptoms",Context.MODE_PRIVATE);

                    symptom.setSymptomType(String.valueOf(symptomType));



                    //TODO save the completed symptom in the db
                    if (symptom.isValid()) {
                        Log.i(TAG, "Symptom saved");
                        saveSymptomInput();
                    }
                    isCountDown = false;
                }
            }.start();
//            Log.i(TAG, timeStamp);
        }
        if (symptomInputList.size() < 4) {
            symptomInputList.add(input);
            Log.i(TAG, "  " + symptomInputList.size());
        } else {
            Log.i(TAG, "Measurement completed");
        }
    }


    private void resetSymptomInput() {
        symptomInputList.clear();
        Log.i(TAG, "List cleared");

    }

    private void saveSymptomInput() {
        resetSymptomInput();
        try {
            dbHelper.getSymptomDAO().create(symptom);
//            dbHelper.getDiaryDAO().update(diary);
            Log.i(TAG, "Symptom saved to database");
        } catch (SQLException e) {
            throw new RuntimeException("Could not create a new Thing in the database", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
//        //Symptom is completed
//        if (data.getClass().getSimpleName().equals(Symptom.class.getSimpleName())) {
//            Log.i(TAG, "Class symptom");
//
//
//        }
        if (data.getClass().getSimpleName().equals(SymptomContext.class.getSimpleName())) {
            Log.i(TAG, "Class symptom context");

//            symptom.setContext(dataManager.getSymptomContext());
            dataManager.getSymptomContext().deleteObservers();
            if (!isCountDown) {
                //That means that the weather data returned after the symptoms was saved
                saveSymptomInput();
            }

        } else if (data.getClass().getSimpleName().equals("Exception")) {
            Log.i(TAG, data.toString());
            if (data.toString().contains("Network")) {
                // No network available do not retry to get info

            } else {
                isWeatherException = true;
            }

        } else if (data.toString().equals("Error")) {
            Log.i(TAG, "Unknown error occured. Contact the administrator");
        }
    }

    public List<Symptom> getAllSymptoms() throws java.sql.SQLException {

        if (dbHelper.getSymptomDAO() != null) {
            return dbHelper.getSymptomDAO().queryForAll();
        } else {
            return new ArrayList<>();
        }
    }


//    public void belongsToDiary(Symptom symptomToDiary) {
//        switch (Integer.parseInt(symptom.getSymptomType())){
//            case 1:
//                diary.getSymptom1().add(symptom);
//                return;
//            case 2:
//                diary.getSymptom2().add(symptom);
//                return;
//            case 3:
//                diary.getSymptom3().add(symptom);
//                return;
//        }
//    }
}
