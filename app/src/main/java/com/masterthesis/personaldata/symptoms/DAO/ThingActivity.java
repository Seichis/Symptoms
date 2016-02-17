package com.masterthesis.personaldata.symptoms.DAO;

import android.database.SQLException;
import android.os.Bundle;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.DAO.model.Diary;
import com.masterthesis.personaldata.symptoms.DAO.model.Symptom;
import com.masterthesis.personaldata.symptoms.DAO.model.Thing;
import com.masterthesis.personaldata.symptoms.R;

import java.util.List;

public class ThingActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    public static final String KEY_THING_ID = "NOTIFY_SERVICE_ID";
    List<Symptom> symptoms ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing);

        int thingId = getIntent().getIntExtra(KEY_THING_ID, -1);
        Diary diary= null;
        try {
            symptoms=getHelper().getSymptomDAO().queryForAll();

            diary = getHelper().getDiaryDAO().queryForId(thingId);
        } catch (SQLException e) {
            throw new RuntimeException("Could not lookup Think in the database", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        String idString;
        String description;
        if (diary == null) {
            idString = "id " + thingId + " not found";
            description = "id " + thingId + " not found";
        } else {
            idString = String.valueOf(diary.getId());
            description = diary.getDescription();
        }

        ((TextView) findViewById(R.id.thingId)).setText(String.valueOf(symptoms.get(0).getContext().getTemperature()));
        ((TextView) findViewById(R.id.thingDescription)).setText(description);
    }
}
