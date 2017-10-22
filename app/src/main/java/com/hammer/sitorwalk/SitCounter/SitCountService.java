package com.hammer.sitorwalk.SitCounter;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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
                                        if (value >= 1) {
                                            int sitMinutes = sharedPref.getInt("sit", 0);
                                            sitMinutes ++;
                                            editor.putInt("sit", sitMinutes).commit();
                                        }
                                    }
                                    catch (JSONException e) {
                                        editor.putInt("sensor", 0).commit(); // disconnected
                                    }
                                    Log.d("Response", Integer.toString(value) );
                                    Log.d("value", Integer.toString(sharedPref.getInt("sensor", 0)));
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
                    handler.postDelayed(this, 5000);
                }
            }, 5000);
        }
    }
}
