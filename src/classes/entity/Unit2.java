/*
 * @written 4/2/2025
 */
package classes.entity;

import classes.abstracts.BaseVector;
import classes.util.Console;

/**
 * A class similar to Vector2, however it's components must be initialized as
 * int values and they will never change into a decimal form, even though they
 * are stored as doubles.
 */
public class Unit2 extends BaseVector<Unit2> {

	public Unit2() {
		this(0, 0);
	}

	public Unit2(int x, int y) {
		this.components.add((double) x);
		this.components.add((double) y);
	}

	// TODO: Add documentation
	//
	// Public getters
	//
	public int getX() {
		return getInt(0);
	}

	public int getY() {
		return getInt(1);
	}

	public Vector2 toVector2() {
		return new Vector2(getX(), getY());
	}

	@Override
	protected Unit2 newVector(Double[] components) {
		return new Unit2(
				components[0].intValue(),
				components[1].intValue());
	}

	@Override
	public String toString() {
		return Console.filterConsoleColors(String.format(
				"Unit2<%s, %s>",
				getX(),
				getY()));
	}

	@Override
	public String serialize() {
		return new StringBuilder("Unit2{")
				.append(getX())
				.append(", ")
				.append(getY())
				.append("}")
				.toString();
	}
}
