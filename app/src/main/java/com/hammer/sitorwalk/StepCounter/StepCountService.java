package com.hammer.sitorwalk.StepCounter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.hammer.sitorwalk.HomeFragment;
import com.hammer.sitorwalk.MainActivity;
import com.hammer.sitorwalk.R;

/**
 * Created by Cheng on 15/10/17.
 */

public class StepCountService extends Service implements SensorEventListener, StepListener{
    public static final String TAG = "StepCountService";
    private PedometerBinder pedometerBinder = new PedometerBinder();

    // Global variables for pedometer
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;

    // Global variables for settings
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private int recordSteps;

    private static final String PREFERENCE_NAME = "stepCountPref";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        // Start pedometer service
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);

        // Initialize shared settings
        sharedPref = getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        // Load numSteps from shared preference
        recordSteps = sharedPref.getInt(getString(R.string.settings_num_steps), 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return pedometerBinder;
    }

    public class PedometerBinder extends Binder {

        public void startCounting() {
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        recordSteps++;
        editor.putInt("numSteps", recordSteps);
        editor.commit();
    }

}
