package com.jeroenseegers.random_number_die;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jeroenseegers.random_number_die.R;
import com.jeroenseegers.random_number_die.die.Die;

public class RandomNumberActivity extends Activity implements OnClickListener, SensorEventListener {

	private Die mDie;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Vibrator mVibrator;
	private SharedPreferences mPreferences;
	
	private long mShakeTimestamp;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_number);

        this.mDie			= new Die(1, 20);
        this.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mVibrator		= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        this.mPreferences	= getPreferences(MODE_PRIVATE);
        
        Button randomNumberButton = (Button)findViewById(R.id.randomNumberButton);
        randomNumberButton.setOnClickListener(this);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		
		if (!this.mVibrator.hasVibrator()) {
			menu.findItem(R.id.vibrate_option).setEnabled(false);
		}
		
		return true;
	}
	
	public void toggleVibration(MenuItem item) {
		if (this.mPreferences.getBoolean("vibrateOnTouch", true)) {
			this.mPreferences.edit().putBoolean("vibrateOnTouch", false).commit();
			item.setChecked(false);
		} else {
			this.mPreferences.edit().putBoolean("vibrateOnTouch", true).commit();
			item.setChecked(true);
		}
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
		TextView randomNumberTextDark = (TextView)findViewById(R.id.randomNumberTextDark);
		TextView randomNumberTextLight = (TextView)findViewById(R.id.randomNumberTextLight);
		this.mDie.rollDie();
		randomNumberTextDark.setText(String.valueOf(this.mDie.getCurrentValue()));
		randomNumberTextLight.setText(String.valueOf(this.mDie.getCurrentValue()));
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

	@Override
	public void onClick(View arg0) {
		if (this.mVibrator.hasVibrator() && this.mPreferences.getBoolean("vibrateOnTouch", true)) {
			this.mVibrator.vibrate(20);
		}
		
		this.throwDie();
	}

}
