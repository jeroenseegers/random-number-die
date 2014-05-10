package com.jeroenseegers.mtg_sidekick;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.jeroenseegers.mtg_sidekick.die.Die;

public class RandomNumberActivity extends Activity implements SensorEventListener {

	private Die mDie;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	
	private long mShakeTimestamp;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_number);

        this.mDie = new Die(1, 20);
        this.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		this.mSensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.mSensorManager.registerListener(this, this.mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void throwDie() {
		TextView randomNumberText = (TextView)findViewById(R.id.randomNumberText);
		this.mDie.rollDie();
		randomNumberText.setText(String.valueOf(this.mDie.getCurrentValue()));
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			
			float gX = x / SensorManager.GRAVITY_EARTH;
			float gY = y / SensorManager.GRAVITY_EARTH;
			float gZ = z / SensorManager.GRAVITY_EARTH;
			
			double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);
			
			if (gForce > 2.0) {
				final long now = System.currentTimeMillis();
				
				if (this.mShakeTimestamp + 500 > now) {
					return;
				}
				
				this.mShakeTimestamp = now;
				this.throwDie();
			}
		}
	}

}
