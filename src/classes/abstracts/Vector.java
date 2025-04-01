package classes.abstracts;

import java.util.ArrayList;
import java.util.List;

import classes.util.Console;

public abstract class Vector<T extends Number> {
	protected abstract List<T> getComponents();
	protected abstract Vector<T> newVector(List<T> components);

	public Vector<T> subtract(Vector<T> v) {
		List<T> cv0 = getComponents();
		List<T> cv1 = v.getComponents();
		List<Double> cvr = new ArrayList<>();

		for (int i = 0; i < cv0.size(); i++) {
			double c0 = Double.valueOf("" + cv0.get(i));
			double c1 = Double.valueOf("" + cv1.get(i));
			Double cd = c0 - c1;

			@SuppressWarnings("unchecked")
			T result = (T) cd;
			cvr.add(result);
		}

		return newVector(cvr);
	}

	public T get(int index) {
		return getComponents().get(index);
	}

	@Override
	public String toString() {
		return String.format(this.getClass().getSimpleName() + "<%s, %s>", get(0), get(1));
	}
}
