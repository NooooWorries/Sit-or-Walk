package com.hammer.sitorwalk.background;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.hammer.sitorwalk.R;

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
        editor.putInt("numSteps", 0).commit();

    }
}
