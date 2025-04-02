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

	@Override
	protected Unit2 newVector(Double[] components) {
		return new Unit2(
			components[0].intValue(), 
			components[1].intValue()
		);
	}

}
