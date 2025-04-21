/*
 * @written 4/2/2025
 */
package classes.abstracts;

import classes.entity.Vector2;
import classes.util.Console;
import classes.util.Formatter;
import classes.util.Math2;
import exceptions.VectorMismatchException;
import interfaces.Callback;
import interfaces.Serializable;

import java.util.ArrayList;

/**
 * An abstract class that provides base functionality for any vector-related
 * object.
 *
 * <p>
 * This class assumes that all interactions with vector components of it's
 * subclasses are integers, however, it stores all components as double values.
 * This is done so that if a subclass wishes to restrict the manipulation of
 * it's components to int types, it can extend this class and guarantee that all
 * inherited methods will not generate a non-int value as a result.
 */
public abstract class BaseVector<T extends BaseVector<T>> implements Serializable {

	protected abstract T newVector(Double[] components);

	protected ArrayList<Double> components = new ArrayList<>();
	private T inverted;

	// TODO: Add documentation
	public String serialize() {
		StringBuilder out = new StringBuilder(
				getClass().getSimpleName()).append("{");

		for (int i = 0; i < components.size(); i++) {
			out.append(components.get(i));
			if (i < components.size() - 1)
				out.append(", ");
		}

		out.append("}");
		return out.toString();
	}

	/**
	 * Accepts a lambda function as the callback parameter to calculate what the
	 * new value of an individual BaseVector<T> components should be.
	 *
	 * @param callback the lambda function to execute
	 *
	 * @return a double array of the new calculated components
	 */
	public Double[] computeComponents(Callback callback) {
		Double[] resultComponents = new Double[size()];
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = (double) callback.call(get(i));
		}
		return resultComponents;
	}

	/**
	 * Computes the components of BaseVector<T>_0 and BaseVector<T>_1 by
	 * processing their new values with a callback lambda function. The
	 * resulting values are stored in a double array.
	 *
	 * @param v
	 * @param methodName the name of the calling method
	 * @param callback   the lambda function that processes components from both
	 *                   BaseVector<T> objects
	 *
	 * @return the double array containing the resulting components
	 */
	public Double[] computeComponents(T v, String methodName, Callback callback) {
		if (v == null) {
			throw new IllegalArgumentException(String.format("BaseVector<T> is null (calling method: %s)", methodName));
		}

		if (!v.isSize(size())) {
			throw new VectorMismatchException(methodName);
		}

		Double[] resultComponents = new Double[size()];
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = (double) callback.call(get(i), v.get(i));
		}
		return resultComponents;
	}

	public ArrayList<Double> components() {
		return this.components;
	}

	/**
	 * Get the size of the BaseVector<T>, denoted by how many components it is
	 * comprised of.
	 *
	 * @return the size of the BaseVector<T>
	 */
	public int size() {
		return this.components.size();
	}

	/**
	 * Checks if the BaseVector<T> is a given size
	 *
	 * @param comparedSize the size to check against
	 * @return true if the BaseVector<T> is the given size
	 */
	public boolean isSize(int comparedSize) {
		return size() == comparedSize;
	}

	/**
	 * Gets the BaseVector<T> components and converts them to a fixed array of
	 * doubles
	 *
	 * @return a Double array of the BaseVector<T> components
	 */
	public Double[] componentsArray() {
		return this.components.toArray(new Double[size()]);
	}

	/**
	 * Get a BaseVector<T> component at a specific index
	 *
	 * @param index the index position of the component
	 * @return the component value at the index
	 */
	public Double get(int index) {
		return this.components.get(index);
	}

	/**
	 * Get a BaseVector<T> component at a specific index, converted to an int
	 * value.
	 *
	 * @param index the index position of the component
	 * @return the (int) component value at the index
	 */
	public Integer getInt(int index) {
		return get(index).intValue();
	}

	/**
	 * Inverts the BaseVector<T> components by changing their individual signs.
	 *
	 * @return BaseVector<T> with inverted (flipped sign) components
	 */
	public T invert() {
		if (inverted != null) {
			return inverted;
		}

		inverted = newVector(computeComponents(
				(args) -> (double) args[0] * -1));

		return inverted;
	}

	/**
	 * Checks if a given BaseVector<T> is equal to another child instance by
	 * comparing their size and the value of their components.
	 *
	 * @param v the BaseVector<T> to check against
	 * @return true if both BaseVector<T> objects are equal
	 */
	public boolean equals(T v) {
		if (!v.isSize(size())) {
			return false;
		}

		for (int i = 0; i < size(); i++) {
			if ((double) get(i) != (double) v.get(i)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Sums the components of two BaseVector<T> objects
	 *
	 * @param v the BaseVector<T> components to add to these components
	 * @return the resulting BaseVector<T> with summed components
	 */
	public T add(T v) {
		return newVector(computeComponents(
				v,
				"add(BaseVector<T>)",
				(args) -> (double) args[0] + (double) args[1]));
	}

	/**
	 * Overload: {@code add}
	 *
	 * Add a common scalar value to all BaseVector<T> components
	 *
	 * @param scalar the value to add to all components of this BaseVector<T>
	 * @return ...
	 */
	public T add(int scalar) {
		return newVector(computeComponents(
				(args) -> (double) args[0] + scalar));
	}

	/**
	 * Take the absolute value of all components in this BaseVector<T>
	 *
	 * @return the resulting BaseVector<T> with absolute valued components
	 */
	public T abs() {
		return newVector(computeComponents(
				(args) -> Math.abs((double) args[0])));
	}

	/**
	 * Get the signed components denoting which n-D spacial quadrant the
	 * BaseVector<T> is in (the direction BaseVector<T> is facing from the
	 * origin)
	 *
	 * @return the unit vector of BaseVector<T> with only signed components
	 *         (i.e, (-1, 0))
	 */
	public T signedUnit() {
		return newVector(computeComponents(
				(args) -> Math2.toSign((double) args[0])));
	}

	/**
	 * Subtracts the components of some BaseVector<T> from this BaseVector<T>.
	 *
	 * @param v the BaseVector<T> to subtract the components of
	 * @return the resulting BaseVector<T> with subtracted components
	 */
	public T subtract(T v) {
		return newVector(computeComponents(
				v,
				"subtract(BaseVector<T>)",
				(args) -> (double) args[0] - (double) args[1]));
	}

	/**
	 * Overload: {@code subtract}
	 *
	 * Subtract a common integer scalar value from this BaseVector<T>
	 *
	 * @param scalar the integer scalar value to subtract from all components
	 * @return the resulting BaseVector<T> with components subtracted by
	 *         {@code scalar}
	 */
	public T subtract(int scalar) {
		return newVector(computeComponents(
				(args) -> (double) args[0] - scalar));
	}

	/**
	 * Multiply all components of this BaseVector<T> by the components of some
	 * other BaseVector<T>
	 *
	 * @param v the BaseVector<T> that multiplies against this BaseVector<T>
	 * @return the resulting product of both BaseVector<T> components
	 */
	public T multiply(T v) {
		return newVector(computeComponents(
				v,
				"multiply(BaseVector<T>)",
				(args) -> (double) args[0] * (double) args[1]));
	}

	/**
	 * Multiply all components of this BaseVector<T> by a common integer scalar
	 *
	 * @param scalar the integer to multiply all components of this
	 *               BaseVector<T> by
	 * @return the resulting BaseVector<T> with it's components multiplied by
	 *         {@code scalar}
	 */
	public T multiply(int scalar) {
		return newVector(computeComponents(
				(args) -> (double) args[0] * scalar));
	}

	@Override
	public String toString() {
		return Console.filterConsoleColors(this.getClass()
				.getSimpleName()
				+ "<" + Formatter.concatArray(componentsArray()) + ">");
	}
}
