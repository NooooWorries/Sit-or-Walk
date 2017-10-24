package com.hammer.sitorwalk.SitCounter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hammer.sitorwalk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Cheng on 21/10/17.
 */

public class SitCountService extends Service{

    private SitCounterBinder sitCounterBinder = new SitCounterBinder();

    // Global variables for settings
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    // Data access address variables
    private String token;
    private String accessAddress;

    // Variables to send HTTP request
    RequestQueue queue;
    JsonObjectRequest getRequest;

    // Global variables for notification
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;

    private int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize shared settings
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();

        // Get the access address
        token = sharedPref.getString("key_token", "0");
        accessAddress = "https://us.wio.seeed.io/v1/node/GenericAInA0/analog?access_token=" + token;

        // Initialize request objects
        queue = Volley.newRequestQueue(this);

        notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getApplicationContext());

        // Initialize notification


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sitCounterBinder;
    }

    public class SitCounterBinder extends Binder {

        public void startCounting() {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final String url = accessAddress;
                    final int interval = Integer.parseInt(sharedPref.getString("key_sit_notification", "60"));
                    Log.d("INTERVAL", Integer.toString(interval));
                    getRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null,
                            new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // display response
                                    int value = -1;
                                    try{
                                        editor.putInt("sensor", 1).commit(); // connected
                                        value = response.getInt("analog");
                                        if (value >= 1024) {
                                            int sitMinutes = sharedPref.getInt("sit", 0);
                                            sitMinutes ++;
                                            editor.putInt("sit", sitMinutes).commit();
                                            count ++;
                                            Log.d("NOTIFICATION", Integer.toString(count));
                                            if (count >= interval) {
                                                count = 0;
                                                mBuilder.setContentTitle("Walk or Sit")
                                                        .setContentText("It's time to leave your chair")
                                                        .setTicker("Do some exercise")
                                                        .setWhen(System.currentTimeMillis())
                                                        .setPriority(Notification.PRIORITY_HIGH)
                                                        .setOngoing(false)
                                                        .setDefaults(Notification.DEFAULT_VIBRATE)
                                                        .setSmallIcon(R.mipmap.ic_launcher);
                                                Notification notification = mBuilder.build();
                                                notification.flags = Notification.FLAG_ONGOING_EVENT;
                                                notificationManager.notify(0, notification);
                                                Uri notificationPush = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notificationPush);
                                                r.play();

                                            }
                                        }
                                    }
                                    catch (JSONException e) {
                                        editor.putInt("sensor", 0).commit(); // disconnected
                                    }
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    editor.putInt("sensor", 2).commit(); // Internet error
                                    Log.d("Error.Response", error.toString());
                                    Log.d("value", Integer.toString(sharedPref.getInt("sensor", 0)));
                                }
                            }
                    );
                    queue.add(getRequest);
                    getRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    handler.postDelayed(this, 60000);
                }
            }, 60000);
        }
    }
}
