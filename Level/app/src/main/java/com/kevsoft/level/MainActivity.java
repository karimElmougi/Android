package com.kevsoft.level;

import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;

public class MainActivity extends Activity {

    private static final float RADIAN_TO_DEGREE = 57.2957795f;

    private float[] gravity;
    private float[] magnetic;
    private float[] accel = new float[3];
    private float[] mag = new float[3];
    private float[] values = new float[3];

    private float azimuth;
    private float pitch;
    private float roll;

    LevelView level;

    SensorEventListener mySensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mag = event.values.clone();
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    accel = event.values.clone();
                    break;
            }

            if (mag != null && accel != null) {
                gravity = new float[9];
                magnetic = new float[9];
                SensorManager.getRotationMatrix(gravity, magnetic, accel, mag);
                float[] outGravity = new float[9];
                SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X, SensorManager.AXIS_Y, outGravity);
                SensorManager.getOrientation(outGravity, values);

                azimuth = values[0] * RADIAN_TO_DEGREE;
                pitch =values[1] * RADIAN_TO_DEGREE;
                roll = values[2] * RADIAN_TO_DEGREE;
                mag = null;
                accel = null;

                level.setAzimuth(azimuth);
                level.setPitch(pitch);
                level.setRoll(roll);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        level = new LevelView(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SensorManager sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(mySensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mySensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL);

        setContentView(level);
    }
}
