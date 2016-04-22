package com.masterthesis.personaldata.symptoms.handlers;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.masterthesis.personaldata.symptoms.BackgroundService;
import com.masterthesis.personaldata.symptoms.DAO.model.DatabaseHelper;
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
        Random random = new Random();

        Intent thingIntent = new Intent(context, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, thingIntent, 0);

        // The ticker text, this uses a formatted string so our message could be localized

        BackgroundService.getInstance().updateNotification(String.valueOf(random.nextInt(1000)),contentIntent);
    }


}
