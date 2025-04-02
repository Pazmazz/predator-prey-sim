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
			resultComponents[i] = (Double) callback.call(get(i));
		}
		return resultComponents;
	}

	public Double[] computedComponents(T v, String methodName, Callback callback) {
		if (!v.isSize(size())) {
			throw new VectorMismatchException(methodName);
		}

		Double[] resultComponents = newResultArray();
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = (Double) callback.call(get(i), v.get(i));
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
			(args) -> (Double) args[0] * -1
		));

		return negated;
	}

	public T add(T v) {
		return newVector(computedComponents(
			v,
			"add",
			(args) -> (Double) args[0] + (Double) args[1]
		));
	}

	public T subtract(T v) {
		return add((T) v.negate());
	}

	public T scale(int scalar) {
		return newVector(computedComponents(
			"negate",
			(args) -> (Double) args[0] * scalar
		));
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
}
