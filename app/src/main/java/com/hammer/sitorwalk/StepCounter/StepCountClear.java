package com.hammer.sitorwalk.StepCounter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.hammer.sitorwalk.R;
import com.hammer.sitorwalk.StepCounter.HistoryModel;
import com.hammer.sitorwalk.StepCounter.HistoryRepo;

/**
 * Created by Cheng on 13/10/17.
 */

public class StepCountClear extends BroadcastReceiver {
    // Global variables for settings

    private static final String PREFERENCE_NAME = "stepCountPref";
    @Override
    public void onReceive(Context context, Intent intent) {

        // Initialize shared settings
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
            Toast.makeText(context, Integer.toString(sharedPref.getInt("numSteps", 0)),
                    Toast.LENGTH_LONG).show();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editorSit = settings.edit();

        // Get the date of yesterday
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, -1);

        // Format date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format.format(date.getTime());

        // Add step count to database
        HistoryModel historyModel = new HistoryModel();
        historyModel.setDate(formatted );
        historyModel.setSteps(sharedPref.getInt("numSteps", 0));

        // Insert data into database
        HistoryRepo repo = new HistoryRepo(context);
        repo.insert(historyModel);

        editor.putInt("numSteps", 0).commit();

        // Add sit count to database

    }
}
