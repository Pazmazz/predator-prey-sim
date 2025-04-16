/*
 * @written 3/29/2025
 */
package classes.util;

/**
 * Provides a library of format methods for handling general string format
 * needs, or converting complex data into a formatted string
 */
public class Formatter {

	/**
	 * Takes any array and returns a string separating the array contents by
	 * some separator string.
	 *
	 * @param array     the array to concat
	 * @param separator the string to separate each array element by in the
	 *                  returned string
	 *
	 * @return the returned string of the concatenated array
	 * 
	 * @see #concatArray(Object[], String)
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
	 * An overload of {@link #concatArray(Object[], String)} which defaults the
	 * {@code separator} parameter to {@code ", "}
	 *
	 * @param array the array to concat
	 * @return the returned string of the concatenated array
	 * 
	 * @see #concatArray(Object[])
	 */
	public static String concatArray(Object[] array) {
		return concatArray(array, ", ");
	}
}
