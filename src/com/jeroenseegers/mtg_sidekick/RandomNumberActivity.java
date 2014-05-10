package com.jeroenseegers.mtg_sidekick;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RandomNumberActivity extends Activity implements OnClickListener {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_number);

        Button randomNumberButton = (Button)findViewById(R.id.randomNumberButton);

        randomNumberButton.setOnClickListener(this);
    }
	
	@Override
	public void onClick(View arg0) {
		TextView randomNumberText = (TextView)findViewById(R.id.randomNumberText);
		randomNumberText.setText("You clicked me!");
		Log.d("mtg_sidekick", "Button clicked");
	}

}
