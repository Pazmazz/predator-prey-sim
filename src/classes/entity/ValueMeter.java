package classes.entity;

import classes.util.Math2;

public class ValueMeter {
	private double value;
	private double max;
	private double min;
	private MeterResetType meterResetType;

	public EventSignal onMaxValueReached = new EventSignal();
	public EventSignal onMinValueReached = new EventSignal();
	public EventSignal onValueChanged = new EventSignal();

	public enum MeterResetType {
		ON_MIN,
		ON_MAX,
		NONE
	}

	public ValueMeter(double minValue, double maxValue, double value, MeterResetType meterResetType) {
		this.max = maxValue;
		this.min = minValue;
		this.meterResetType = meterResetType;
		this.setValue(value);
	}

	public ValueMeter(double minValue, double maxValue, double value) {
		this(minValue, maxValue, value, MeterResetType.NONE);
	}

	public ValueMeter(double maxValue, double value) {
		this(0, maxValue, value, MeterResetType.NONE);
	}

	public ValueMeter(double maxValue) {
		this(0, maxValue, maxValue, MeterResetType.NONE);
	}

	public ValueMeter(double maxValue, MeterResetType meterResetType) {
		this(0, maxValue, maxValue, meterResetType);
	}

	public double getValue() {
		return this.value;
	}

	public double getMax() {
		return this.max;
	}

	public double getMin() {
		return this.min;
	}

	public double getRatio() {
		return this.value / (double) this.max;
	}

	public void setValue(double value) {
		this.value = Math2.clamp(value, this.min, this.max);
		this.onValueChanged.fire(this.value);

		if (this.value == this.max) {
			this.onMaxValueReached.fire();
			if (this.meterResetType == MeterResetType.ON_MAX) {
				this.value = this.min;
				this.onMinValueReached.fire();
				this.onValueChanged.fire(this.value);
			}
		} else if (this.value == this.min) {
			this.onMinValueReached.fire();
			if (this.meterResetType == MeterResetType.ON_MIN) {
				this.value = this.max;
				this.onMaxValueReached.fire();
				this.onValueChanged.fire(this.value);
			}
		}
	}

	public ValueMeter setMax(double value) {
		this.max = value;
		return this;
	}

	public ValueMeter setMin(double value) {
		this.min = value;
		return this;
	}

	public void fill() {
		this.setValue(this.max);
	}

	public void empty() {
		this.setValue(this.min);
	}

	public ValueMeter setMaxAndFill(double value) {
		this.setMax(value);
		this.setValue(value);
		return this;
	}

	public ValueMeter setResetType(MeterResetType resetType) {
		this.meterResetType = resetType;
		return this;
	}

	public double incrementBy(double value) {
		if (value < 0)
			throw new Error("Cannot increment value meter by a negative number");
		double newValue = this.value + value;
		this.setValue(newValue);
		return newValue;
	}

	public double decrementBy(double value) {
		if (value < 0)
			throw new Error("Cannot decrement value meter by a negative number");
		double newValue = this.value + value;
		this.setValue(this.value - value);
		return newValue;
	}

	public double increment() {
		return this.incrementBy(1);
	}

	public double decrement() {
		return this.decrementBy(1);
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
