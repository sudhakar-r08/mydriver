package com.techgimmicks.mydriver;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.HashSet;

public class MovementDetector implements SensorEventListener {

    protected final String TAG = getClass().getSimpleName();

    private SensorManager sensorMan;
    private Sensor accelerometer;

    private MovementDetector() {
    }

    private static MovementDetector mInstance;

    public static MovementDetector getInstance() {
        if (mInstance == null) {
            mInstance = new MovementDetector();
            mInstance.init();
        }
        return mInstance;
    }


    private HashSet<Listener> mListeners = new HashSet<MovementDetector.Listener>();

    private void init() {
        sensorMan = (SensorManager) GlobalData.getInstance().getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void start() {
        sensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorMan.unregisterListener(this);
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    /* (non-Javadoc)
     * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float diff = (float) Math.sqrt(x * x + y * y + z * z);
            if (diff > 0.5) // 0.5 is a threshold, you can test it and change it
                Log.d(TAG,"Device motion detected!!!!");
            for (Listener listener : mListeners) {
                listener.onMotionDetected(event, diff);
            }
        }

    }

    /* (non-Javadoc)
     * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    public interface Listener {
        void onMotionDetected(SensorEvent event, float acceleration);
    }
}
