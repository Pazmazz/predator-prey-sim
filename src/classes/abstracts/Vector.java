package classes.abstracts;

import java.util.List;

public abstract class Vector {
	protected abstract List<? extends Number> getComponents();
	protected abstract Vector newVector(List<? extends Number> components);

	public Vector subtract(Vector v) {
		return newVector(getComponents());
	}

	public Object get(int index) {
		return getComponents().get(index);
	}

	public int getInt(int index) {
		return (int) getComponents().get(index);
	}

	public double getDouble(int index) {
		return (double) getComponents().get(index);
	}

	@Override
	public String toString() {
		return String.format(this.getClass().getSimpleName() + "<%s, %s>", get(0), get(1));
	}
}
