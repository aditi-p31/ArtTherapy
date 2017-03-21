package android.csulb.edu.arttherapy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

public class DrawActivity extends AppCompatActivity implements SensorEventListener {

    DrawingView drawView;
    private ImageButton currPaint;

    // Canvas will be updated only when phone is shaken 3 times //
    private SensorManager SensorManager;
    private static final int FORCE_SENSOR_THRESHOLD = 25;
    private static final int TIME_SENSOR_THRESHOLD = 350;
    private static final int SUBSEQUENT_SHAKE_TIMEOUT = 750;
    private static final int SHAKE_INTERVAL_DURATION = 2000;
    private static final int SHAKE_ITERATOR = 3;



    private float LastSensorX = -1.0f, LastSensorY = -1.0f, LastSensorZ = -1.0f;
    private long LastSensorTime;
    private int ShakeIterator = 0;
    private long LastSensorShake;
    private long LastSensorForce;
    private float Accelerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawView = (DrawingView)findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        SensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        SensorManager.registerListener(this, SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        LastSensorForce = System.currentTimeMillis();
        LastSensorTime = LastSensorForce;
        LastSensorShake = LastSensorForce;


    }

    public void paintClicked(View view){
        //use chosen color
        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
        }

    }

    public void clearCanvas(View v) {

        drawView.clearCanvas();

    }

    protected void OnResume() {
        super.onResume();
        SensorManager.registerListener(this,SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        SensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long now = System.currentTimeMillis();

        if ((now - LastSensorForce) > SUBSEQUENT_SHAKE_TIMEOUT) {
            ShakeIterator = 0;
        }

        if ((now - LastSensorTime) > TIME_SENSOR_THRESHOLD) {
            long diff = now - LastSensorTime;

            Accelerator = Math.abs(event.values[0] + event.values[1] + event.values[2] - LastSensorX - LastSensorY - LastSensorZ) / diff * 10000;
            if (Accelerator > FORCE_SENSOR_THRESHOLD) {

                if ((++ShakeIterator >= SHAKE_ITERATOR) && ((now - LastSensorShake) > SHAKE_INTERVAL_DURATION)) {
                    Vibrator v = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(100);
                    clearCanvas(drawView);
                    Intent intent = new Intent(this,Eraser.class);
                    startService(intent);


                    LastSensorShake = now;
                    ShakeIterator = 0;
                }
                LastSensorForce = now;
            }
            else
            {
            }

            LastSensorTime = now;
            LastSensorX = event.values[0];
            LastSensorY = event.values[1];
            LastSensorZ = event.values[2];
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

