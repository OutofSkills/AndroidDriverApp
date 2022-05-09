package com.intelligentcarmanagement.carmanagementapp.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.intelligentcarmanagement.carmanagementapp.models.utils.Motion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DataCollector implements SensorEventListener {

    private static final String TAG = "DataCollector";
    private static final int DATA_PROCESSING_INTERVAL = 500; // In milliseconds

    /* Sensors api configuration */
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mGyroscopeSensor;

    private long lastTimestamp;

    /* Data collection objects */
    private Motion mMotion;
    MutableLiveData<Motion> mMotionLiveData = new MutableLiveData<>();


    public DataCollector(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mMotion = new Motion();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            mMotion.setAccelerometerX(sensorEvent.values[0]);
            mMotion.setAccelerometerY(sensorEvent.values[1]);
            mMotion.setAccelerometerZ(sensorEvent.values[2]);
        }else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            mMotion.setGyroX(sensorEvent.values[0]);
            mMotion.setGyroY(sensorEvent.values[1]);
            mMotion.setGyroZ(sensorEvent.values[2]);
        }

        long currentTimestamp = sensorEvent.timestamp;

        if(currentTimestamp - lastTimestamp >= TimeUnit.MILLISECONDS.toNanos(DATA_PROCESSING_INTERVAL)){
            lastTimestamp = currentTimestamp;

            mMotion.setTimestamp(TimeUnit.NANOSECONDS.toSeconds(currentTimestamp));
            mMotionLiveData.setValue(mMotion);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void register() {
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "register: Accelerometer listener registered.");

        mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "register: Gyroscope listener registered.");
    }

    public void unregister(){
        mSensorManager.unregisterListener(this, mAccelerometerSensor);
        Log.d(TAG, "unregister: Accelerometer listener unregistered.");

        mSensorManager.unregisterListener(this, mGyroscopeSensor);
        Log.d(TAG, "unregister: Gyroscope listener unregistered.");
    }

    public void setDataClass(String dataClass) {
        mMotion.setDrivingClass(dataClass);
    }

    public LiveData<Motion> getCollectedData(){
        return mMotionLiveData;
    }
}
