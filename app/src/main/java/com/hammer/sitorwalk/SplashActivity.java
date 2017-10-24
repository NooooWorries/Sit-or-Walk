/*
 * Author: Zixin Cheng
 * Date: 6 Sep 2017
 */
package com.hammer.sitorwalk;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hammer.sitorwalk.Settings.PersonalInformationActivity;
import com.hammer.sitorwalk.SitCounter.SitCountService;
import com.hammer.sitorwalk.StepCounter.NotificationReceiver;
import com.hammer.sitorwalk.StepCounter.StepCountClear;
import com.hammer.sitorwalk.StepCounter.StepCountService;

public class SplashActivity extends AppCompatActivity {
    Calendar mCalendar;
    Calendar notificationCalender;

    // Preferences
    private static final String PREFERENCE_NAME = "stepCountPref";
    SharedPreferences sharedPref;
    SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTranslucent(this);

        // Start services
        // Clear step count at 0:00 every day
        setStepCountClearServices();

        // Notify user
        setWalkNotificationService();

        // Pedometer service
        Intent startPedometerIntent = new Intent(SplashActivity.this, StepCountService.class);
        startService(startPedometerIntent);

        // Sit count service
        Intent startSitCountService = new Intent(SplashActivity.this, SitCountService.class);
        startService(startSitCountService);


        Intent intent;
        if (settings.getInt("personal", 0) == 0) {
            intent = new Intent(SplashActivity.this, PersonalInformationActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();

    }

    // Set the status bar to transparent style
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Set status bar to transparent
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Set the attributes of root view
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    private void setStepCountClearServices() {
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        long systemTime = System.currentTimeMillis();

        mCalendar.setTimeInMillis(System.currentTimeMillis());

        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        long selectTime = mCalendar.getTimeInMillis();

        if(systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(SplashActivity.this, StepCountClear.class);
        PendingIntent pi = PendingIntent.getBroadcast(SplashActivity.this, 0, intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
    }

    private void setWalkNotificationService() {
        // Get shared preference
        sharedPref = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.getBoolean("key_step_notification", true) == true) {
            // Get notification time and convert to int
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:MM");
            String timeStr = settings.getString("key_step_time", "00:00");
            Date time = null;
            int hours;
            int minutes;
            try {
                time = simpleDateFormat.parse(timeStr);
            } catch (ParseException e) {
                hours = 18;
                minutes = 0;
            }
            hours = time.getHours();
            minutes = time.getMinutes();

            // Set calender
            notificationCalender = Calendar.getInstance();
            notificationCalender.setTimeInMillis(System.currentTimeMillis());
            long systemTime = System.currentTimeMillis();

            notificationCalender.setTimeInMillis(System.currentTimeMillis());

            notificationCalender.set(Calendar.HOUR_OF_DAY, hours);
            notificationCalender.set(Calendar.MINUTE, minutes);
            notificationCalender.set(Calendar.SECOND, 0);
            notificationCalender.set(Calendar.MILLISECOND, 0);

            long selectTime = notificationCalender.getTimeInMillis();

            if (systemTime > selectTime) {
                notificationCalender.add(Calendar.DAY_OF_MONTH, 1);
            }

            // Start alarm manager
            Intent intent = new Intent(SplashActivity.this, NotificationReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(SplashActivity.this, 0, intent, 0);
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, notificationCalender.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
        }
    }

}
