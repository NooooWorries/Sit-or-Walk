package com.hammer.sitorwalk.StepCounter;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.hammer.sitorwalk.R;

/**
 * Created by Cheng on 22/10/17.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private static final String PREFERENCE_NAME = "stepCountPref";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        int target = Integer.parseInt(settings.getString("key_step_target", "0"));
        int current = sharedPref.getInt("numSteps", 0);
        int remaining = target - current;
        if (remaining < 0) remaining = 0;

        if (remaining > 0) {
            mBuilder.setContentTitle("Walk or Sit")
                    .setContentText("You should walk " + Integer.toString(remaining) + " more steps to finish your daily target.")
                    .setTicker("It's time to finish your daily target")
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setOngoing(false)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = mBuilder.build();
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            notificationManager.notify(0, notification);
        }
    }
}
