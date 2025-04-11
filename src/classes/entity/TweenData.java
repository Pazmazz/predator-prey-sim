package classes.entity;

import java.util.HashMap;

import classes.abstracts.Entity;
import classes.abstracts.Properties;
import classes.abstracts.Properties.Property;
import classes.util.Time;

public class TweenData {
	private Object startValue;
	private Object endValue;
	private double duration = 1;
	private boolean isCritical = false;
	private long startTime = -1;
	private Entity<?> entity;

	// TweenInfo positionTween = new TweenData(this, Property.POSITION, new
	// Vector2(100, 100), 5, true));
	// positionTween.start();
	// positionTween.stop();
	// positionTween.terminate();
	public TweenData(Entity<?> entity, Property property, Object endValue, double duration) {
		this.duration = duration;
		this.endValue = endValue;
		this.entity = entity;
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
		this.startTime = Time.tick();
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
