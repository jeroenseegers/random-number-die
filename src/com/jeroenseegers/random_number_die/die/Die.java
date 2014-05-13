package com.jeroenseegers.random_number_die.die;

public class Die {

	private int mMinValue;
	private int mMaxValue;
	private int mCurrentValue;

	public Die(int minValue, int maxValue) {
		this.mMinValue = minValue;
		this.mMaxValue = maxValue;
	}

	public void rollDie() {
		this.mCurrentValue = (int)(Math.random() * this.mMaxValue) + this.mMinValue;
	}

	public int getCurrentValue() {
		return this.mCurrentValue;
	}

}
