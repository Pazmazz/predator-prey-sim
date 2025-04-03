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

	public double angle() {
		return Math.atan2(getY(), getX());
	}

	public double screenAngle() {
		return Math.atan2(-getY(), getX());
	}

	/*
	 * evaluateLinearFunctionWithRespectToN()
	 * 
	 * Evaluate the general linear function of the line passing through
	 * this Vector2 (x0, y0) and a target Vector2 (x1, y1), with
	 * respect to `n` (x or y)
	 */
	private Double evaluateLinearFunctionWithRespectToN(
		double x0, 
		double x1, 
		double y0, 
		double y1, 
		double n,
		boolean useDomain
	) {
		/* NEEDS FIXING */
		if (useDomain && (n < x0 || n > x1)) {
			return null;
		}
		
		double dx = x1 - x0;
		double dy = y1 - y0;

		if (dx == 0) {
			return null;
		}

		Double m = dy / dx;
		Double y = m * (n - x0) + y0;
		
		return y;
	}

	/*
	 * Evaluate with respect to X, solving for Y
	 */
	public Double solveFunctionOfXForY(Vector2 p2, double x) {
		return evaluateLinearFunctionWithRespectToN(
			getX(), p2.getX(),
			getY(), p2.getY(),
			x, true
		);
	}

	/*
	 * Evaluate with respect to X, solving for X
	 */
	public Double solveFunctionOfXForX(Vector2 p2, double y) {
		return evaluateLinearFunctionWithRespectToN(
			getY(), p2.getY(),
			getX(), p2.getX(),
			y, true
		);
	}

	@Override
	public Vector2 newVector(Double[] components) {
		return new Vector2(components[0], components[1]);
	}
}
