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

public abstract class Vector<T extends Vector> extends BaseVector<T> {
	public T divide(T v) {
		return newVector(computeComponents(
			v,
			"divide(Vector<T>)",
			(args) -> (Double) args[0] / (Double) args[1]
		));
	}

	public T divide(double scalar) {
		return newVector(computeComponents(
			"divide(int scalar)",
			(args) -> (Double) args[0] / scalar
		));
	}

	public T divide(int scalar) {
		return divide((double) scalar);
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
