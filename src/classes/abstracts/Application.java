/*
 * @written 3/29/2025
 */
package classes.abstracts;

import classes.entity.Game;

/**
 * The root application API from which all subclasses inherit. Responsible for
 * providing universal utility methods and/or cumulative runtime data.
 */
public abstract class Application {
	public Game game;

	public static long tick() {
		return System.nanoTime();
	}

	public void wait(double milliseconds) {
		try {
			Thread.sleep((long) milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
