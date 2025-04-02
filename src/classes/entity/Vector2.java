/*
 * @Written: 3/30/2025
 * 
 * class Vector2:
 * 
 * A custom implementation of a Vector class for
 * dealing with math in higher dimensional space
 * 
 * TODO: Create an abstract superclass for general vectors
 */
package classes.entity;

public class Vector2 extends Vector<Vector2> {
	public Vector2() {
		this(0, 0);
	}

	public Vector2(double x, double y) {
		super(x, y);
	}

	// @Override
	// public Vector2 newVector(Double[] components) {
	// 	return new Vector2(components[0], components[1]);
	// }
}
