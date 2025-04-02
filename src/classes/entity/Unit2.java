/*
 * @Written: 4/2/2025
 * 
 * class Unit2:
 * 
 * A class similar to Vector2, however it's components must be
 * initialized as int values and they will never change into
 * a decimal form, even though they are stored as doubles.
 */
package classes.entity;

import classes.abstracts.BaseVector;

public class Unit2 extends BaseVector<Unit2> {
	public Unit2() {
		this(0, 0);
	}

	public Unit2(int x, int y) {
		this.components.add((double) x);
		this.components.add((double) y);
	}

	public int getX() {
		return getInt(0);
	}

	public int getY() {
		return getInt(1);
	}

	@Override
	protected Unit2 newVector(Double[] components) {
		return new Unit2(
			components[0].intValue(), 
			components[1].intValue()
		);
	}

}
