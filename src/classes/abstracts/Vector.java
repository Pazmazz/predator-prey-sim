package classes.abstracts;

import java.util.ArrayList;

public abstract class Vector<T extends Vector> extends BaseVector<T> {
	public T multiply(T v) {
		return newVector(computeComponents(
			v,
			"add",
			(args) -> (Double) args[0] * (Double) args[1]
		));
	}

	public T divide(T v) {
		return newVector(computeComponents(
			v,
			"add",
			(args) -> (Double) args[0] / (Double) args[1]
		));
	}

	public T scale(double scalar) {
		return newVector(computeComponents(
			"negate",
			(args) -> (Double) args[0] * scalar
		));
	}

	public Integer[] getComponentArrayAsInt() {
		return this.components.toArray(new Integer[size()]);
	}

	public ArrayList<Integer> getComponentsAsInt() {
		ArrayList<Integer> intComponents = new ArrayList<>();
		for (Double d : this.components) {
			intComponents.add(d.intValue());
		}
		return intComponents;
	}
}
