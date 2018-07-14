package com.dhbw.legosteuerung;

        import android.content.Context;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;

/**
 * Created by D062299 on 12.02.2017.
 */

public class GyroThread extends Thread implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;

    public GyroThread(SensorManager importedSensorManager) {
        mSensorManager = importedSensorManager;
        mRotationVectorSensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ROTATION_VECTOR);
        mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
    }

    public void GyroThread(SensorManager nSensorManager) {


    }

    public void run() {
        mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}