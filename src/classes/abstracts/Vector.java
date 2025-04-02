package classes.abstracts;

import java.util.ArrayList;
import java.util.List;

import classes.util.Console;
import classes.util.Formatter;
import exceptions.VectorMismatchException;
import interfaces.Callback;

public abstract class Vector<T extends Vector> extends BaseVector<T> {

	public T multiply(T v) {
		Double[] resultComponents = newResultArray();
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = get(i) * v.get(i);
		}
		return newVector(resultComponents);
	}

	public T divide(T v) {
		Double[] resultComponents = newResultArray();
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = get(i) / v.get(i);
		}
		return newVector(resultComponents);
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
