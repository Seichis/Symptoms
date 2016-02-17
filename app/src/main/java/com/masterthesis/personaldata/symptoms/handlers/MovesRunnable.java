package com.masterthesis.personaldata.symptoms.handlers;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;

import com.masterthesis.personaldata.symptoms.BackgroundService;
import com.masterthesis.personaldata.symptoms.DAO.ThingActivity;
import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
import com.masterthesis.personaldata.symptoms.DAO.model.Thing;
import com.masterthesis.personaldata.symptoms.MainActivity;

import java.util.Random;

/**
 * Created by Konstantinos Michail on 2/17/2016.
 */
public class MovesRunnable implements Runnable {
    Context context;
    DatabaseHelper databaseHelper;

    public MovesRunnable(Context _context,DatabaseHelper _databaseHelper){
        context=_context;
        databaseHelper=_databaseHelper;
    }

    @Override
    public void run() {
//        Thing thing = new Thing();
        Random random = new Random();
//        thing.setDescription("My thing " + random.nextInt(1000));
//        try {
////            databaseHelper.getThingDao().create(thing);
//        } catch (SQLException e) {
//            throw new RuntimeException("Could not create a new Thing in the database", e);
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
        Intent thingIntent = new Intent(context, MainActivity.class);
//        thingIntent.putExtra(ThingActivity.KEY_THING_ID, thing.getId());

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, thingIntent, 0);

        // The ticker text, this uses a formatted string so our message could be localized
//        String tickerText = "New Thing: " + thing.getDescription();

        BackgroundService.getInstance().updateNotification(String.valueOf(random.nextInt(1000)),contentIntent);
    }


}
