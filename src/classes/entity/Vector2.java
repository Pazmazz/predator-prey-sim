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

import java.util.ArrayList;
import java.util.List;

import classes.abstracts.Vector;

public class Vector2 extends Vector<Double> {
	private List<Double> components;

	public Vector2() {
		this(0, 0);
	}

	public Vector2(double x, double y) {
		components = new ArrayList<>();
		components.add(x);
		components.add(y);
	}

	@Override
	protected List<Double> getComponents() {
		return components;
	}

	@Override
	protected Vector<Double> newVector(List<Double> components) {
		return new Vector2(components.get(0), components.get(1));
	}

	// @Override
	// public String toString() {
	// 	return String.format(this.getClass().getSimpleName() + "<%s, %s>", get(0), get(1));
	// }
}
