package classes.abstracts;

import java.util.ArrayList;
import java.util.List;

import classes.util.Console;
import classes.util.Formatter;
import exceptions.VectorMismatchException;

public abstract class Vector<T extends Vector> {
	protected abstract T newVector(Double[] components);
	protected ArrayList<Double> components = new ArrayList<>();

	private T negated;

	public ArrayList<Double> getComponents() {
		return this.components;
	}

	public int size() {
		return this.components.size();
	}

	public boolean isSize(int comparedSize) {
		return size() == comparedSize;
	}

	public Double[] getComponentArray() {
		return this.components.toArray(new Double[this.components.size()]);
	}

	public Double get(int index) {
		return this.components.get(index);
	}

	public Integer getInt(int index) {
		return get(index).intValue();
	}

	public T negate() {
		if (negated != null) return negated;

		Double[] resultComponents = new Double[size()];
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = -get(i);
		}

		negated = newVector(resultComponents);
		return negated;
	}

	public String toString() {
		String className = this.getClass().getSimpleName();

		String replacementStrings = Formatter
			.concatArray(getComponentArray())
			.replaceAll("\\-?\\d+.?\\d*", "%s");

		return String.format(
			className + "<" + replacementStrings + ">",
			getComponentArray()
		);
	}

	public T add(T v) {
		if (!v.isSize(size())) {
			throw new VectorMismatchException("add");
		}

		Double[] resultComponents = new Double[size()];
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = get(i) + v.get(i);
		}
		return newVector(resultComponents);
	}

	public T subtract(T v) {
		return add((T) v.negate());
	}

	public Vector scale(int scalar) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'scale'");
	}

	public Vector scale(double scalar) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'scale'");
	}

	public Vector multiply(Vector v) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'multiply'");
	}

	public Vector divide(Vector v) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'divide'");
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
