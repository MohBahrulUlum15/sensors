package com.rememberdev.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.series.DataPoint;

import java.io.IOException;

public class ProximitySensorActivity extends AppCompatActivity implements SensorEventListener {

    private TextView txtInfo;
    private SensorManager sensorManager;
    private Sensor sensorProximity;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity_sensor);

        txtInfo = findViewById(R.id.txt_information);
        txtInfo.setText("Loading...");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (sensorProximity == null) {
            txtInfo.setText("Sensor tidak tersedia!");
        } else {
            sensorManager.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sensorManager.unregisterListener(this, sensorProximity);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        switch (sensorType) {
            case Sensor.TYPE_PROXIMITY:
                txtInfo.setText(getResources().getString(R.string.label_proxity, sensorEvent.values[0]));

                if (sensorEvent.values[0] == 0) {
                    mediaPlayer = new MediaPlayer();

                    try {
                        AssetFileDescriptor as = this.getAssets().openFd("objek_mendekat.mp3");
                        mediaPlayer.setDataSource(as.getFileDescriptor(), as.getStartOffset(), as.getLength());
                        as.close();
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (sensorEvent.values[0] > 150) {
                    mediaPlayer = new MediaPlayer();
                    try {
                        AssetFileDescriptor as = this.getAssets().openFd("objek_menjauh.mp3");
                        mediaPlayer.setDataSource(as.getFileDescriptor(), as.getStartOffset(), as.getLength());
                        as.close();
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}