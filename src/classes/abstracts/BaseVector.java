package classes.abstracts;

import java.util.ArrayList;

import classes.util.Formatter;
import exceptions.VectorMismatchException;
import interfaces.Callback;

public abstract class BaseVector<T extends BaseVector> {
	protected abstract T newVector(Double[] components);
	protected ArrayList<Double> components = new ArrayList<>();
	private T negated;

	public Double[] computedComponents(String methodName, Callback callback) {
		Double[] resultComponents = newResultArray();
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = callback.computeDouble(get(i));
		}
		return resultComponents;
	}

	public Double[] computedComponents(T v, String methodName, Callback callback) {
		if (!v.isSize(size())) {
			throw new VectorMismatchException(methodName);
		}

		Double[] resultComponents = newResultArray();
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = callback.computeDoubles(get(i), v.get(i));
		}
		return resultComponents;
	}

	public Double[] newResultArray() {
		return new Double[size()];
	}

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

		negated = newVector(computedComponents(
			"negate",
			(prev) -> { return prev * -1; }
		));

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
}
