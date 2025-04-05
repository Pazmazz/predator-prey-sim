/*
 * @Written: 4/2/2025
 * 
 * abstract class Vector:
 * 
 * A more specific sub-category of BaseVector which includes
 * implementation that does not assume that it's components
 * are necessarily strictly int values. Therefore, methods 
 * such as:
 * - multiply(
 */
package classes.abstracts;

import java.util.ArrayList;

public abstract class Vector<T extends Vector<T>> extends BaseVector<T> {
	private Double magnitude;
	private T unit;

	/*
	 * divide()
	 * 
	 * Return a new Vector<T> with all components
	 * of itself divided by all components of the operand vector.
	 * 
	 * Example:
	 * ```
	 * Vector2 v = new Vector2(4, 4);
	 * Console.println(v.divide(new Vector2(4, 4));
	 * ```
	 * Output: Vector2<1.0, 1.0>
	 */
	public T divide(T v) {
		return newVector(computeComponents(
			v,
			"divide(Vector<T>)",
			(args) -> (Double) args[0] / (Double) args[1]
		));
	}

	/*
	 * @overload: divide()
	 * 
	 * Divide all components of Vector<T> by some double value
	 */
	public T divide(double dec) {
		return newVector(computeComponents(
			"divide(int scalar)",
			(args) -> (Double) args[0] / dec
		));
	}

	/*
	 * @overload: divide()
	 * 
	 * Divide all components of Vector<T> by some scalar integer
	 */
	public T divide(int scalar) {
		return newVector(computeComponents(
			"divide(int scalar)",
			(args) -> (Double) args[0] / scalar
		));
	}

	/*
	 * @overload: multiply()
	 * 
	 * Multiply all components of Vector<T> by some double value
	 */
	public T multiply(double dec) {
		return newVector(computeComponents(
			"multiply(double scalar)",
			(args) -> (Double) args[0] * dec
		));
	}
	
	/*
	 * @overload: add()
	 */
	public T add(double dec) {
		return newVector(computeComponents(
			"add(double dec)",
			(args) -> (Double) args[0] + dec
		));
	}

	/*
	 * @overload: subtract()
	 */
	public T subtract(double dec) {
		return newVector(computeComponents(
			"subtract(double dec)",
			(args) -> (Double) args[0] - dec
		));
	}

	/*
	 * floor()
	 * 
	 * Floor all components of Vector<T>
	 */
	public T floor() {
		return newVector(computeComponents(
			"floor()",
			(args) -> Math.floor((Double) args[0])
		));
	}

	/*
	 * ceil()
	 * 
	 * Ceil all components of Vector<T>
	 */
	public T ceil() {
		return newVector(computeComponents(
				"ceil()",
				(args) -> Math.ceil((Double) args[0])));
	}

	/*
	 * magnitude()
	 * 
	 * Return the magnitude of Vector<T>
	 */
	public double magnitude() {
		if (this.magnitude != null) return this.magnitude;

		this.magnitude = 0.0;
		this.components.forEach(cn -> this.magnitude += cn * cn);
		this.magnitude = Math.sqrt(this.magnitude);

		return this.magnitude;
	}

	/*
	 * unit()
	 * 
	 * Return the unit vector of Vector<T>
	 */
	public T unit() {
		if (this.unit != null) return this.unit;
		this.unit = divide(magnitude());
		return this.unit;
	}

	/*
	 * getComponentArrayAsInt()
	 * 
	 * Return the component ArrayList as a native int array
	 */
	public Integer[] getComponentArrayAsInt() {
		return this.components.toArray(new Integer[size()]);
	}

	/*
	 * getComponentsAsInt()
	 * 
	 * Return the component ArrayList as an ArrayList of
	 * int values
	 */
	public ArrayList<Integer> getComponentsAsInt() {
		ArrayList<Integer> intComponents = new ArrayList<>();
		for (Double d : this.components) {
			intComponents.add(d.intValue());
		}
		return intComponents;
	}
}
