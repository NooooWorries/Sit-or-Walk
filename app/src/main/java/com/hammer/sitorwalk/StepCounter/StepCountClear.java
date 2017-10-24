package com.hammer.sitorwalk.StepCounter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.hammer.sitorwalk.R;
import com.hammer.sitorwalk.SitCounter.SitModel;
import com.hammer.sitorwalk.SitCounter.SitRepo;
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
        SitModel sitModel = new SitModel();
        sitModel.setDate(formatted);
        sitModel.setSit(settings.getInt("sit", 0));

        // Insert data into database
        SitRepo repoSit = new SitRepo(context);
        repoSit.insert(sitModel);

        editorSit.putInt("sit", 0).commit();

        // Recommendation
        if (settings.getBoolean("key_step_recom", false) == true) {
            ArrayList<HistoryModel> historyList = repo.getList();
            int length = historyList.size();
            int target = 0;
            int sum = 0;
            int average = 0;

            // Get the average history record;
            for (int i = 0; i < length; i ++) {
                sum = sum + historyList.get(i).getSteps();
            }
            average = sum / length;
            target = 8000 + (8000 - average);
            if (target > 11000)
                target = 11000;
            int gender = Integer.parseInt(settings.getString("gender", "2"));
            if (gender == 0) target = target + 1000;
            else if (gender == 1) target = target - 1000;
            double height = Integer.parseInt(settings.getString("height", "0")) / 100;
            int weight = Integer.parseInt(settings.getString("weight", "0"));
            Double bmi = weight / (height * height);
            int bmiInt = bmi.intValue();
            target = target + ((bmiInt - 23) * 1000);
            if (target > 14000) target = 14000;
            editorSit.putString("key_step_target", Integer.toString(target)).commit();
        }
    }
}
