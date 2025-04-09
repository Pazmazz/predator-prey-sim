package classes.entity;

import java.util.HashMap;

import classes.abstracts.Application;
import classes.entity.Properties.Property;

public class TweenData extends Application {
	private Object startValue;
	private Object endValue;
	private double duration = 1;
	private boolean isCritical = false;
	private long startTime = -1;

	public TweenData(Object startValue, Object endValue) {
		this.startValue = startValue;
		this.endValue = endValue;
	}

	public boolean isCritical(Property prop) {
		return this.isCritical;
	}

	public Object getStartValue() {
		return this.startValue;
	}

	public Object getEndValue() {
		return this.endValue;
	}

	public void setStartTime() {
		this.startTime = tick();
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public double getDuration() {
		return this.duration;
	}
}
