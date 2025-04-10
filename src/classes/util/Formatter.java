/*
 * @written 3/29/2025
 */
package classes.util;

import classes.abstracts.Application;

/**
 * Provides a library of format methods for handling general string format
 * needs, or converting complex data into a formatted string
 */
public class Formatter extends Application {

	/**
	 * Takes any array and returns a string separating the array contents by
	 * some separator string.
	 *
	 * @param array     the array to concat
	 * @param separator the string to separate each array element by in the
	 *                  returned string
	 *
	 * @return the returned string of the concatenated array
	 */
	public static String concatArray(Object[] array, String separator) {
		String out = "";

		for (int index = 0; index < array.length; index++) {
			if (array[index] == null)
				out += "null";
			else
				out += array[index].toString();

			if (index < array.length - 1)
				out += separator;
		}

		return out;
	}

	/**
	 * @Overload: {@code concatArray}
	 *
	 *            Calls the root method but uses {@code ", "} as a default delimiter
	 *
	 * @param array ...
	 * @return ...
	 */
	public static String concatArray(Object[] array) {
		return concatArray(array, ", ");
	}
}
