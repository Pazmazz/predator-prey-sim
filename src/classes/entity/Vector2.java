/*
 * @Written: 3/30/2025
 * 
 * class Vector2:
 * 
 * A custom implementation of a Vector class for
 * dealing with math in higher dimensional space
 * 
 * TODO: 
 * - Create an abstract superclass for general vectors [DONE]
 */
package classes.entity;

import classes.abstracts.Vector;

public class Vector2 extends Vector<Vector2> {
	public Vector2() {
		this(0, 0);
	}

	public Vector2(double x, double y) {
		this.components.add(x);
		this.components.add(y);
	}

	public double getX() {
		return get(0);
	}

	public double getY() {
		return get(1);
	}

	public Double slopeX() {
		if (getX() == 0) return null;
		return getY() / getX();
	}

	public Double slopeY() {
		if (getY() == 0) return null;
		return getX() / getY();
	}

	public double angle() {
		return Math.atan2(getY(), getX());
	}

	public double screenAngle() {
		return Math.atan2(-getY(), getX());
	}

	private Double evaluateLinearFunctionWithRespectToN(
		double x0, 
		double x1, 
		double y0, 
		double y1, 
		double n
	) {
		double dx = x1 - x0;
		double dy = y1 - y0;

		if (dx == 0) {
			return null;
		}

		Double m = dy / dx;
		return m*(n - x0) + y0;
	}

	public Double evalFunctionX(Vector2 p2, double x) {
		return evaluateLinearFunctionWithRespectToN(getX(), p2.getX(), getY(), p2.getY(), x);
	}

	public Double evalFunctionY(Vector2 p2, double y) {
		return evaluateLinearFunctionWithRespectToN(getY(), p2.getY(), getX(), p2.getY(), y);
	}

	@Override
	public Vector2 newVector(Double[] components) {
		return new Vector2(components[0], components[1]);
	}
}
