package interfaces;

import java.util.List;

public interface StrictVector<SubType> {
	// Base
	SubType subtract(SubType v);
	SubType add(SubType v);
	SubType scale(int scalar);
	SubType negate();

	int size();
	boolean isSize(int comparedSize);
	Integer getInt(int index);
	Integer[] getComponentArrayAsInt();
	List<Integer> getComponentsAsInt();

	SubType newVector(Double[] components);

	// Strict
	SubType scale(double scalar);
	SubType multiply(SubType v);
	SubType divide(SubType v);

	Double[] getComponentArray();
	List<Double> getComponents();
	Double get(int index);
}
