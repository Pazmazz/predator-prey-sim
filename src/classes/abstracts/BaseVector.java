/*
 * @Written: 4/2/2025
 * 
 * abstract class BaseVector:
 * 
 * An abstract class that provides base functionality for any
 * vector-related object. This class assumes that all interactions
 * with vector components of it's subclasses are integers, however, 
 * it stores all components as double values. This is done so that
 * if a subclass wishes to restrict the manipulation of it's 
 * components to int types, it can extend this class and guarantee 
 * that all inherited methods will not generate a non-int value
 * as a result.
 * 
 * For example, methods such as:
 * - add()
 * - subtract()
 * - multiply(int x)
 * - multiply(T extends BaseVector)
 * 
 * Cannot not generate a decimal value if the components are initialized
 * as integers.
 */

package classes.abstracts;

import java.util.ArrayList;

import classes.util.Formatter;
import exceptions.VectorMismatchException;
import interfaces.Callback;

public abstract class BaseVector<T extends BaseVector> {
	protected abstract T newVector(Double[] components);
	protected ArrayList<Double> components = new ArrayList<>();
	private T inverted;

	/*
	 * computeComponents()
	 * 
	 * Accept a lambda function as the callback parameter to
	 * calculate what the new value of an individual component
	 * should be. The argument sent to the callback is the current
	 * value of the component. The return value of the callback
	 * is the updated component value.
	 */
	public Double[] computeComponents(String methodName, Callback callback) {
		Double[] resultComponents = new Double[size()];
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = (Double) callback.call(get(i));
		}
		return resultComponents;
	}

	/*
	 * @overload: computeComponents()
	 * 
	 * If a child of BaseVector is provided, then the lambda function
	 * is passed two arguments:
	 * - 1. Component from `this` BaseVector
	 * - 2. Component from the target BaseVector
	 * 
	 * The computed value with respect to both vector components should
	 * be returned, denoting the computed component in the newly returned
	 * vector result.
	 * 
	 * If attempting to compute the components of two vectors of mismatched
	 * size, an unchecked exception `VectorMismatchException` will be thrown.
	 */
	public Double[] computeComponents(T v, String methodName, Callback callback) {
		if (!v.isSize(size())) {
			throw new VectorMismatchException(methodName);
		}

		Double[] resultComponents = new Double[size()];
		for (int i = 0; i < size(); i++) {
			resultComponents[i] = (Double) callback.call(get(i), v.get(i));
		}
		return resultComponents;
	}

	/*
	 * getComponents()
	 * 
	 * Return the ArrayList consisting of the all of the
	 * components making up BaseVector<T>.
	 */
	public ArrayList<Double> getComponents() {
		return this.components;
	}

	/*
	 * size()
	 * 
	 * Return the size of the BaseVector<T>. The size
	 * represents the dimensions of the vector, i.e how
	 * many components it is comprised of.
	 */
	public int size() {
		return this.components.size();
	}

	/*
	 * isSize()
	 * 
	 * Return true or false by checking the size of itself
	 * (checks the number of components it has with a
	 * compared size).
	 */
	public boolean isSize(int comparedSize) {
		return size() == comparedSize;
	}

	/*
	 * getComponentArray()
	 * 
	 * Return the components as a native array of double values
	 */
	public Double[] getComponentArray() {
		return this.components.toArray(new Double[this.components.size()]);
	}

	/*
	 * get()
	 * 
	 * Return a component at a given index as a double
	 */
	public Double get(int index) {
		return this.components.get(index);
	}

	/*
	 * getInt()
	 * 
	 * Return a component at a given index as an int
	 */
	public Integer getInt(int index) {
		return get(index).intValue();
	}

	/*
	 * invert()
	 * 
	 * Return a new BaseVector<T> with inverted components.
	 * 
	 * Example:
	 * ```
	 * Vector2 v = new Vector2(1, 5);
	 * Console.println(v.invert())
	 * ```
	 * Output: Vector2<-1.0, -5.0>
	 */
	public T invert() {
		if (inverted != null) return inverted;

		inverted = newVector(computeComponents(
			"invert",
			(args) -> (Double) args[0] * -1
		));

		return inverted;
	}

	/*
	 * add()
	 * 
	 * Return a new BaseVector<T> with all components
	 * of itself added to all components of the operand vector.
	 * 
	 * Example:
	 * ```
	 * Vector2 v = new Vector2(4, 4);
	 * Console.println(v.add(new Vector2(1, 1));
	 * ```
	 * Output: Vector2<5.0, 5.0>
	 */
	public T add(T v) {
		return newVector(computeComponents(
			v,
			"add(BaseVector<T>)",
			(args) -> (Double) args[0] + (Double) args[1]
		));
	}

	/*
	 * @overload: add()
	 * 
	 * Add all components of BaseVector<T> by some scalar integer
	 */
	public T add(int scalar) {
		return newVector(computeComponents(
			"add(int scalar)",
			(args) -> (Double) args[0] + scalar
		));
	}

	/*
	 * subtract()
	 * 
	 * Return a new BaseVector<T> instance with all components
	 * of the target BaseVector<T> subtracted from itself.
	 * 
	 * Example:
	 * ```
	 * Vector2 v = new Vector2(4, 4);
	 * Console.println(v.subtract(new Vector2(1, 1));
	 * ```
	 * Output: Vector2<3.0, 3.0>
	 */
	public T subtract(T v) {
		return newVector(computeComponents(
			v,
			"subtract(BaseVector<T>)",
			(args) -> (Double) args[0] - (Double) args[1]
		));
	}

	/*
	 * @overload: subtract()
	 * 
	 * Subtract all components of BaseVector<T> by some scalar integer
	 */
	public T subtract(int scalar) {
		return newVector(computeComponents(
			"subtract(int scalar)",
			(args) -> (Double) args[0] - scalar
		));
	}

	/*
	 * @overload: multiply()
	 * 
	 * Return a new BaseVector<T> instance with all components
	 * of itself multiplied by all components of the operand
	 * BaseVector<T>.
	 * 
	 * Example:
	 * ```
	 * Vector2 v = new Vector2(4, 4);
	 * Console.println(v.multiply(new Vector2(2, 3)));
	 * ```
	 * Output: Vector2<8.0, 12.0>
	 */
	public T multiply(T v) {
		return newVector(computeComponents(
			v,
			"multiply(BaseVector<T>)",
			(args) -> (Double) args[0] * (Double) args[1]
		));
	}

	/*
	 * @overload: multiply()
	 * 
	 * Multiply all components of BaseVector<T> by some scalar integer
	 */
	public T multiply(int scalar) {
		return newVector(computeComponents(
			"multiply(int scalar)",
			(args) -> (Double) args[0] * scalar
		));
	}

	/*
	 * toString()
	 * 
	 * Return a name of the object in the form: BaseVector<c0, c1, ...>
	 */
	@Override
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
