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
	 * Linear interpolation between {@code a} and {@code b} using some value
	 * 1 >= {@code t} >= 0
	 *
	 * @param t parameterized variable for {@code a} and {@code b}
	 * @param a initial value
	 * @param b final value
	 * @return the interpolation between {@code a} and {@code b} using t
	 */
	public static double lerp(double a, double b, double t) {
		return a + t * (b - a);
	}

	/**
	 * Takes a {@code double} and converts it to the multiplicative identity of the
	 * same sign. If {@code n} is zero then the result stays zero.
	 * 
	 * @param n
	 * @return the signed multiplicative identity of {@code n}
	 */
	public static double toSign(double n) {
		if (n == 0)
			return n;

		return Math.abs(n) / n;
	}

	public static int randInt(int range) {
		return (int) (Math.random() * range);
	}
}
