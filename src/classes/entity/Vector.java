package classes.entity;

import java.util.ArrayList;
import java.util.List;

import classes.util.Console;
import classes.util.Formatter;
import exceptions.VectorMismatchException;
import interfaces.StrictVector;

public class Vector<SubType extends Vector> implements StrictVector<SubType> {
	final private ArrayList<Double> components = new ArrayList<>();
	private SubType negated;

	public Vector(Double... components) {
		for (int i = 0; i < components.length; i++) {
			this.components.add(i, components[i]);
		}
	}

	@Override
	public List<Double> getComponents() {
		return this.components;
	}

	@Override
	public int size() {
		return this.components.size();
	}

	@Override
	public boolean isSize(int comparedSize) {
		return size() == comparedSize;
	}

	@Override
	public Double[] getComponentArray() {
		return this.components.toArray(new Double[this.components.size()]);
	}

	@Override
	public Double get(int index) {
		return this.components.get(index);
	}

	@Override
	public Integer getInt(int index) {
		return get(index).intValue();
	}

	@Override
	public SubType negate() {
		if (negated != null) return negated;

		Double[] resultComponents = new Double[size()];
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = -get(i);
		}

		negated = (SubType) new Vector(resultComponents);
		return negated;
	}

	@Override
	public String toString() {
		String className = this.getClass().getSimpleName();

		String replacementStrings = Formatter
			.concatArray(getComponentArray())
			.replaceAll("\\d+.?\\d*", "%s");

		return String.format(
			className + "<" + replacementStrings + ">",
			getComponentArray()
		);
	}

	@Override
	public SubType add(Vector v) {
		if (!v.isSize(size())) {
			throw new VectorMismatchException("add");
		}

		Double[] resultComponents = new Double[size()];
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = get(i) + v.get(i);
		}
		return newVector(resultComponents);
	}

	@Override
	public SubType subtract(Vector v) {
		return add(v.negate());
	}

	@Override
	public SubType scale(int scalar) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'scale'");
	}

	@Override
	public SubType scale(double scalar) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'scale'");
	}

	@Override
	public SubType multiply(Vector v) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'multiply'");
	}

	@Override
	public SubType divide(Vector v) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'divide'");
	}

	@Override
	public Integer[] getComponentArrayAsInt() {
		return this.components.toArray(new Integer[size()]);
	}

	@Override
	public List<Integer> getComponentsAsInt() {
		List<Integer> intComponents = new ArrayList<>();
		for (Double d : this.components) {
			intComponents.add(d.intValue());
		}
		return intComponents;
	}

	@Override
	public SubType newVector(Double[] components) {
		throw new UnsupportedOperationException("Unimplemented method 'newVector'");
	}
}
