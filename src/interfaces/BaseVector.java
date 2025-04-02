package interfaces;

import java.util.List;

public interface BaseVector<SubType> {
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
}
