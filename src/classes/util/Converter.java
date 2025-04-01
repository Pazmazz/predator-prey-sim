package classes.util;

import java.lang.reflect.Array;

public class Converter {
	public static Object[] toObjectArray(Object array) {
		if (array.getClass().isArray()) {
			int length = Array.getLength(array);
			Object[] objArray = new Object[length];
				
			for (int i = 0; i < length; i++) {
				objArray[i] = Array.get(array, i);
			}

			return objArray;
		}
		
		throw new Error("toObjectArray() can only convert objects that are arrays");
	}
}
