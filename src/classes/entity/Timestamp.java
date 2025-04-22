package classes.entity;

import classes.util.Time;

public class Timestamp {
	private long currentTime;

	public Timestamp(long time) {
		this.currentTime = time;
	}

	public Timestamp() {
		this.currentTime = Time.tick();
	}

	// Getters
	public long getValue() {
		return this.currentTime;
	}

	public double getValueInSeconds() {
		return Time.nanoToSeconds(this.currentTime);
	}

	public void update() {
		this.currentTime = Time.tick();
	}

	public long getElapsedTime() {
		return Time.tick() - this.currentTime;
	}

	public double getElapsedTimeInSeconds() {
		return Time.nanoToSeconds(this.getElapsedTime());
	}

	public boolean hasTimeElapsed(long nanoseconds) {
		return this.getElapsedTime() >= nanoseconds;
	}

	public boolean hasTimeElapsed(double seconds) {
		return this.getElapsedTimeInSeconds() >= seconds;
	}

	// Setters
	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}
}
