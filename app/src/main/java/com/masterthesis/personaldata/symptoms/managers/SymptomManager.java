package com.masterthesis.personaldata.symptoms.managers;

import android.content.Context;
import android.database.SQLException;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.DAO.model.Diary;
import com.masterthesis.personaldata.symptoms.DAO.model.Symptom;
import com.masterthesis.personaldata.symptoms.DAO.model.SymptomContext;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

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
    Diary diary;
    Symptom symptom;
    DataManager dataManager = DataManager.getInstance();
    CountDownTimer countDownTimer;
    DatabaseHelper dbHelper;
    Context context;
    private boolean isCountDown;

    private SymptomManager() {

    }

    public static SymptomManager getInstance() {
        return symptomManager;
    }

    public void init(Context context) {
        this.context = context;
        dataManager.init(context);
        dbHelper = new DatabaseHelper(context);
    }

    public void manageSymptomInput(double input) {
//        final Symptom symptom = new Symptom();
        if (symptomInputList.size() == 0) {
//            timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK).format(new Date());
            symptom = new Symptom(new Timestamp(System.currentTimeMillis()));
            // A symptom occured. get the weather data and start a timer for ending the registration of a symptom
            dataManager.fetchWeatherData();
            isCountDown = true;

//            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.UK);


//            dataManager.movesStorylineDay(format.format(new Date()),true);


            countDownTimer = new CountDownTimer(15000, 1000) {

                public void onTick(long millisUntilFinished) {
                    // If an exception is caught and it is not a network exception try to get weather again
                    if (isWeatherException) {
                        dataManager.fetchWeatherData();
                    }
                    Log.i(TAG, "seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    dataManager.getSymptomContext().deleteObservers();
                                    String sContext = gson.toJson(dataManager.getSymptomContext(), SymptomContext.class);
                                    Log.i(TAG, "Symptom context: " + sContext);
                                    symptom.setContext(sContext);

                                }
                            });
                        }
                    };
                    new Thread(r).start();


                    symptom.setIntensity(Collections.max(symptomInputList));
                    diary = DiaryManager.getInstance().getActiveDiary();
                    symptom.setDiary(diary);

                    int symptomT = symptomInputList.size() - 1;
                    TreeMap<Integer, String> symptomTypes =diary.getSymptomTypes();
                    if (!symptomTypes.isEmpty()) {
                        for (TreeMap.Entry<Integer, String> st : symptomTypes.entrySet()) {
                            int position = st.getKey();
                            String type = st.getValue();
                            Log.i(TAG, "symptom type" + type);

                            if (position == symptomT) {
                                Log.i(TAG, "symptom type" + position);
                                symptom.setSymptomType(type);
                            }
                        }
                    }
                    saveSymptomInput();


//                    //TODO save the completed symptom in the db
//                    if (symptom.isValid()) {
//                        Log.i(TAG, "Symptom saved");
//                    }
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
        try {
            dbHelper.getSymptomDAO().create(symptom);
//            dbHelper.getDiaryDAO().update(diary);
            Log.i(TAG, "Symptom saved to database");
        } catch (SQLException e) {
            throw new RuntimeException("Could not create a new Thing in the database", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        } finally {
            resetSymptomInput();
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
                Log.i(TAG, "Network error");

            } else {
                Log.i(TAG, "error" + data.toString());

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

    public HashMap<String, List<Symptom>> getSymptomsByDiary(Diary diary) throws java.sql.SQLException {
        TreeMap<Integer, String> symptomTypes = diary.getSymptomTypes();
        HashMap<String, List<Symptom>> symptomsMap = new HashMap<>();
        if (symptomTypes != null) {
            for (String type : symptomTypes.values()) {
                symptomsMap.put(type, dbHelper.getSymptomDAO().queryForEq("type", type));
            }

        }
        return symptomsMap;
    }
}
