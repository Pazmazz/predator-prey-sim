/*
 * @written 3/30/2025
 */
package classes.entity;

import java.util.HashMap;

import classes.abstracts.BaseVector;
import classes.abstracts.Vector;

/**
 * A custom implementation of the Vector class for dealing with math in 2D
 * space.
 */
public class Vector2 extends Vector<Vector2> {

	public Vector2() {
		this(0, 0);
	}

	public Vector2(double x, double y) {
		this.components.add(x);
		this.components.add(y);
	}

	// TODO: Add documentation
	//
	// Public getters
	//
	public double getX() {
		return get(0);
	}

	public double getY() {
		return get(1);
	}

	public double angle() {
		return Math.atan2(getY(), getX());
	}

	public double screenAngle() {
		return Math.atan2(-getY(), getX());
	}

	/**
	 * Evaluate the general linear function of the line passing through this
	 * Vector2 (x0, y0) and a target Vector2 (x1, y1), with respect to {@code x}
	 *
	 * @param x0        point1.x value
	 * @param x1        point2.x value
	 * @param y0        point1.y value
	 * @param y1        point2.y value
	 * @param x         input value to the function
	 * @param useDomain whether or not to check for the domain set by the two
	 *                  points
	 * @return f(x)
	 */
	private Double evaluateLinearFunctionWithRespectTo(
			double x0,
			double x1,
			double y0,
			double y1,
			double x,
			boolean useDomain) {
		double dx = x1 - x0;
		double dy = y1 - y0;

		// check domain
		if (useDomain && x < Math.min(x0, x1) || x > Math.max(x0, x1)) {
			return null;
		}

		// not a function
		if (dx == 0) {
			return null;
		}

		Double m = dy / dx;
		Double y = m * (x - x0) + y0;
		return y;
	}

	/**
	 * Evaluate the equation of a line passing through this Vector2 and another
	 * Vector2 with respect to {@code x}
	 *
	 * @param p2 the second point
	 * @param x  the input to the function
	 * @return f(x)
	 */
	public Double evaluateY(Vector2 p2, double x) {
		return evaluateLinearFunctionWithRespectTo(
				getX(), p2.getX(),
				getY(), p2.getY(),
				x, true);
	}

	/**
	 * Evaluate the equation of a line passing through this Vector2 and another
	 * Vector2 with respect to {@code y}
	 *
	 * @param p2 the second point
	 * @param y  the input to the function
	 * @return x(y)
	 */
	public Double evaluateX(Vector2 p2, double y) {
		return evaluateLinearFunctionWithRespectTo(
				getY(), p2.getY(),
				getX(), p2.getX(),
				y, true);
	}

	@Override
	protected Vector2 newVector(Double[] components) {
		return new Vector2(components[0], components[1]);
	}

	@Override
	public Vector2 construct(HashMap<String, Object> data) {
		this.components.add(data.get(0))
		return new Vector2();
	}
}
