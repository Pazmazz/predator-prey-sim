/*
 * @Written: 3/29/2025
 * 
 * class Formatter:
 * 
 * Provides a library of format methods for handling general
 * string format needs, or converting complex data into
 * a formatted string
 */

package classes.util;

import classes.abstracts.Application;

public class Formatter extends Application {
	/*
	 * concatArray(Object[] array, String separator):
	 * 
	 * Takes any array and returns a string separating the array
	 * contents by some separator string
	 * 
	 * Ex:
	 * 
	 * concatArray(new String[] {"a", "b", "c"}, ", ") => "a, b, c"
	 */
	static String concatArray(Object[] array, String separator) {
		String out = "";

		for (int index = 0; index < array.length; index++) {
			out += array[index].toString();
			if (index < array.length - 1) {
				out += separator;
			}
		}

		return out;
	}

	/*
	 * concatArray(Object[] array)
	 * 
	 * Default the separator parameter to ", "
	 */
	static String concatArray(Object[] array) {
		return concatArray(array, ", ");
	}
}
