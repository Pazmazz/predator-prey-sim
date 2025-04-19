package classes.entity;

import classes.util.Math2;

public class ValueMeter {
	private int value;
	private int min;
	private int max;
	private boolean resetOnMax;

	public EventSignal onMaxValueReached = new EventSignal();
	public EventSignal onMinValueReached = new EventSignal();
	public EventSignal onValueChanged = new EventSignal();

	public ValueMeter(int maxValue, int value, boolean resetOnMax) {
		this.value = value;
		this.max = maxValue;
		this.resetOnMax = resetOnMax;
	}

	public ValueMeter(int maxValue, int value) {
		this(maxValue, value, false);
	}

	public ValueMeter(int maxValue) {
		this(maxValue, maxValue);
	}

	public int getValue() {
		return this.value;
	}

	public int getMax() {
		return this.max;
	}

	public int getMin() {
		return this.min;
	}

	public double getRatio() {
		return this.value / this.max;
	}

	public void setValue(int value) {
		this.value = Math2.clamp(value, this.min, this.max);
		this.onValueChanged.fire(this.value);
		if (this.value == this.max) {
			this.onMaxValueReached.fire();
			if (this.resetOnMax)
				this.value = this.min;
		}
		if (this.value == this.min)
			this.onMinValueReached.fire();
	}

	public void setMax(int value) {
		this.max = value;
	}

	public void setMin(int value) {
		this.min = value;
	}

	public void setMaxAndFill(int value) {
		this.setMax(value);
		this.setValue(value);
	}

	public void setResetOnMax(boolean bool) {
		this.resetOnMax = bool;
	}

	public void incrementBy(int value) {
		if (value < 0)
			throw new Error("Cannot increment value meter by a negative number");
		this.setValue(this.value + value);
	}

	public void decrementBy(int value) {
		if (value < 0)
			throw new Error("Cannot decrement value meter by a negative number");
		this.setValue(this.value - value);
	}

	public void increment() {
		this.incrementBy(1);
	}

	public void decrement() {
		this.decrementBy(1);
	}
}
