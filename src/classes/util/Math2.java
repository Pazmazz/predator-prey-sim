/*
 * @written 4/3/2025
 */
package classes.util;

/**
 * Provides additional math utility functions that are not natively part of the
 * Java Math library
 */
public class Math2 {

	/**
	 * Linear interpolation bewtween {@code a} and {@code b} using some value >=
	 * 1 {@code t} >= 0
	 *
	 * @param t paramaterized variable for {@code a} and {@code b}
	 * @param a initial value
	 * @param b final value
	 * @return the interpolation between {@code a} and {@code b} using t
	 */
	public static double lerp(double t, double a, double b) {
		return a + t * (b - a);
	}
}
