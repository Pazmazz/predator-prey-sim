package classes.entity;

public class Vector2 {
	final public double X;
	final public double Y;

	private Double magnitude;

	public Vector2(double x, double y) {
		this.X = x;
		this.Y = y;
	}

	public Vector2 subtract(Vector2 v2) {
		return new Vector2(X - v2.X, Y - v2.Y);
	}

	public Vector2 add(Vector2 v2) {
		return new Vector2(X + v2.X, Y + v2.Y);
	}

	public double getMagnitude() {
		if (magnitude == null) magnitude = Math.sqrt(X * X + Y * Y);
		return magnitude;
	}

	public Vector2 getUnit() {
		double mag = getMagnitude();
		return new Vector2(X / mag, Y / mag);
	}

	public Vector2 invert() {
		return new Vector2(Y, X);
	}

	@Override
	public String toString() {
		return "<%s, %s>".formatted(X, Y);
	}
}
