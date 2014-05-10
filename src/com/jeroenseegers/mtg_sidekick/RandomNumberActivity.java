package com.jeroenseegers.mtg_sidekick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jeroenseegers.mtg_sidekick.die.Die;

public class RandomNumberActivity extends Activity implements OnClickListener {

	private Die mDie;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_number);

        Button randomNumberButton = (Button)findViewById(R.id.randomNumberButton);
        this.mDie = new Die(1, 20);

        randomNumberButton.setOnClickListener(this);
    }
	
	@Override
	public void onClick(View arg0) {
		TextView randomNumberText = (TextView)findViewById(R.id.randomNumberText);
		this.mDie.rollDie();
		randomNumberText.setText(String.valueOf(this.mDie.getCurrentValue()));
	}

}
