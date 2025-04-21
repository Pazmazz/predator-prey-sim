package classes.entity;

import classes.util.Math2;

public class ValueMeter {
	private int value;
	private int max;
	private int min;
	private RESET_TYPE meterResetType;

	public EventSignal onMaxValueReached = new EventSignal();
	public EventSignal onMinValueReached = new EventSignal();
	public EventSignal onValueChanged = new EventSignal();

	public enum RESET_TYPE {
		ON_MIN,
		ON_MAX,
		NONE
	}

	public ValueMeter(int maxValue, int minValue, int value, RESET_TYPE meterResetType) {
		this.max = maxValue;
		this.min = minValue;
		this.meterResetType = meterResetType;
		this.setValue(value);
	}

	public ValueMeter(int maxValue, int minValue, int value) {
		this(maxValue, minValue, value, RESET_TYPE.NONE);
	}

	public ValueMeter(int maxValue, int value) {
		this(maxValue, 0, value, RESET_TYPE.NONE);
	}

	public ValueMeter(int maxValue) {
		this(maxValue, 0, maxValue, RESET_TYPE.NONE);
	}

	public ValueMeter(int maxValue, RESET_TYPE meterResetType) {
		this(maxValue, 0, maxValue, meterResetType);
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
		return this.value / (double) this.max;
	}

	public void setValue(int value) {
		this.value = Math2.clamp(value, this.min, this.max);
		this.onValueChanged.fire(this.value);

		if (this.value == this.max) {
			this.onMaxValueReached.fire();
			if (this.meterResetType == RESET_TYPE.ON_MAX) {
				this.value = this.min;
				this.onMinValueReached.fire();
				this.onValueChanged.fire(this.value);
			}
		} else if (this.value == this.min) {
			this.onMinValueReached.fire();
			if (this.meterResetType == RESET_TYPE.ON_MIN) {
				this.value = this.max;
				this.onMaxValueReached.fire();
				this.onValueChanged.fire(this.value);
			}
		}
	}

	public ValueMeter setMax(int value) {
		this.max = value;
		return this;
	}

	public ValueMeter setMin(int value) {
		this.min = value;
		return this;
	}

	public void fill() {
		this.setValue(this.max);
	}

	public void empty() {
		this.setValue(this.min);
	}

	public ValueMeter setMaxAndFill(int value) {
		this.setMax(value);
		this.setValue(value);
		return this;
	}

	public ValueMeter setResetType(RESET_TYPE resetType) {
		this.meterResetType = resetType;
		return this;
	}

	public int incrementBy(int value) {
		if (value < 0)
			throw new Error("Cannot increment value meter by a negative number");
		int newValue = this.value + value;
		this.setValue(newValue);
		return newValue;
	}

	public int decrementBy(int value) {
		if (value < 0)
			throw new Error("Cannot decrement value meter by a negative number");
		int newValue = this.value + value;
		this.setValue(this.value - value);
		return newValue;
	}

	public int increment() {
		return this.incrementBy(1);
	}

	public int decrement() {
		return this.decrementBy(1);
	}

	public ValueMeter removeLimit() {
		this.setMax(Integer.MAX_VALUE);
		return this;
	}

	@Override
	public String toString() {
		return new StringBuilder("ValueMeter<")
				.append("MaxValue=")
				.append(this.max)
				.append(", MinValue=")
				.append(this.min)
				.append(", Value=")
				.append(this.value)
				.append(">")
				.toString();
	}
}
